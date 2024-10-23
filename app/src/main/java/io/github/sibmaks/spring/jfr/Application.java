package io.github.sibmaks.spring.jfr;

import jdk.jfr.consumer.RecordingFile;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public class Application {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: visualizer.jar <recording-file-path>");
            return;
        }
        var path = Path.of(args[0]);
        try (var recordingFile = new RecordingFile(path)) {
            System.out.println("Reading events one by one");
            System.out.println("=========================");
            while (recordingFile.hasMoreEvents()) {
                var event = recordingFile.readEvent();
                var eventName = event.getEventType().getName();
                System.out.println("Name: " + eventName);
            }
            System.out.println();
        }
        System.out.println();

        System.out.println("Reading all events at once");
        System.out.println("==========================");

        for (var e : RecordingFile.readAllEvents(path)) {
            String eventName = e.getEventType().getName();
            System.out.println("Name: " + eventName);
        }
        System.out.println();
    }
}
