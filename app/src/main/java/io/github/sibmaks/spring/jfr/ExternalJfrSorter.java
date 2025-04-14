package io.github.sibmaks.spring.jfr;

import io.github.sibmaks.spring.jfr.dto.protobuf.processing.Event;
import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordingFile;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

@Slf4j
public class ExternalJfrSorter {

    public static void sort(Path input, Path outputPath, int batchSize) throws IOException {
        var tempFiles = new ArrayList<Path>();

        try (var recordingFile = new RecordingFile(input)) {
            var batch = new ArrayList<RecordedEvent>();
            while (recordingFile.hasMoreEvents()) {
                batch.add(recordingFile.readEvent());
                if (batch.size() >= batchSize) {
                    tempFiles.add(writeBatchToTempFile(batch));
                    batch.clear();
                }
            }
            if (!batch.isEmpty()) {
                tempFiles.add(writeBatchToTempFile(batch));
            }
        }

        mergeSortedTempFiles(tempFiles, outputPath);

        for (var temp : tempFiles) {
            Files.deleteIfExists(temp);
        }
    }

    private static Path writeBatchToTempFile(List<RecordedEvent> batch) throws IOException {
        batch.sort(Comparator.comparing(RecordedEvent::getStartTime));
        var tempFile = Files.createTempFile("jfr-batch-", ".pb");

        try (var out = Files.newOutputStream(tempFile)) {
            for (var event : batch) {
                var protoEvent = convertToProto(event);
                protoEvent.writeDelimitedTo(out);
            }
        }

        return tempFile;
    }

    private static Event convertToProto(RecordedEvent event) {
        var eventType = event.getEventType();

        var eventThread = event.getThread();

        var builder = Event.newBuilder()
                .setStartTime(event.getStartTime().toEpochMilli())
                .setEndTime(event.getEndTime().toEpochMilli())
                .setEventName(eventType.getName());

        if (eventThread != null) {
            var threadJavaName = eventThread.getJavaName();
            if (threadJavaName != null) {
                builder.setJavaThreadName(threadJavaName);
            }

            var threadOSName = eventThread.getOSName();
            if (threadOSName != null) {
                builder.setOsThreadName(threadOSName);
            }
        }

        for (var field : eventType.getFields()) {
            var value = event.getValue(field.getName());
            if (value == null) {
                continue;
            }
            if (value instanceof String) {
                builder.putStringFields(field.getName(), (String) value);
            } else if (value instanceof Integer) {
                builder.putInt32Fields(field.getName(), (Integer) value);
            } else if (value instanceof Long) {
                builder.putInt64Fields(field.getName(), (Long) value);
            } else if (value instanceof Boolean) {
                builder.putBoolFields(field.getName(), (Boolean) value);
            } else {
              //  log.warn("Unsupported field type: {}", field.getName());
            }
        }

        return builder.build();
    }

    private static void mergeSortedTempFiles(List<Path> tempFiles, Path outputPath) throws IOException {
        var pq = new PriorityQueue<BatchStream>();

        for (var path : tempFiles) {
            var in = Files.newInputStream(path);
            var bs = new BatchStream(in);
            if (bs.advance()) {
                pq.add(bs);
            }
        }

        try (var writer = Files.newOutputStream(outputPath)) {
            while (!pq.isEmpty()) {
                var current = pq.poll();
                var event = current.current;
                event.writeDelimitedTo(writer);

                if (current.advance()) {
                    pq.add(current);
                } else {
                    current.close();
                }
            }
        }
    }

    private static class BatchStream implements Comparable<BatchStream>, Closeable {
        private final InputStream in;
        private Event current;

        public BatchStream(InputStream in) {
            this.in = in;
        }

        public boolean advance() {
            try {
                current = Event.parseDelimitedFrom(in);
                return current != null;
            } catch (IOException e) {
                throw new RuntimeException("Event read error", e);
            }
        }

        @Override
        public int compareTo(BatchStream other) {
            return Long.compare(this.current.getStartTime(), other.current.getStartTime());
        }

        @Override
        public void close() throws IOException {
            in.close();
        }
    }
}
