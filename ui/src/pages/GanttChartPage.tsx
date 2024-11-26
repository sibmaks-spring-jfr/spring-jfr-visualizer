import React, { useState } from 'react';
import {
  Button,
  ButtonGroup,
  Card,
  Container,
  Row
} from 'react-bootstrap';
import './GanttChartPage.css';

interface Span {
  id: string;
  start: number;
  end: number;
  label: string;
}

interface GanttChartPageProps {
  spans: Span[];
}

interface SkippedSpan extends Span {
  skippedSteps: number;
}

interface PositionedSpan extends SkippedSpan {
  level: number;
}

const assignLevelsToSpans = (spans: SkippedSpan[]): PositionedSpan[] => {
  const levels: Span[][] = [];

  return spans.map(span => {
    let level = 0;

    while (
      levels[level] &&
      levels[level].some(existingSpan =>
        existingSpan.start < span.end &&
        span.start < existingSpan.end
      )
      ) {
      level++;
    }

    if (!levels[level]) levels[level] = [];
    levels[level].push(span);

    return { ...span, level };
  });
};

const collapseEmptyIntervals = (spans: Span[], gridStep: number): SkippedSpan[] => {
  const collapsed: SkippedSpan[] = [];

  let skippedSteps = 0;
  let lastEnd = 0;

  spans.forEach((span, index) => {
    if (index === 0) {
      lastEnd = span.end;
      collapsed.push(({ ...span, skippedSteps }));
      return;
    }

    const emptySpace = (span.start - lastEnd) / gridStep;

    if (emptySpace >= 3) {
      skippedSteps += Math.floor(emptySpace - 3);
    }

    collapsed.push(({ ...span, skippedSteps }));
    lastEnd = span.end;
  });

  return collapsed;
};

const GanttChartPage: React.FC<GanttChartPageProps> = ({ spans }) => {
  const [scale, setScale] = useState(1);
  spans.sort((a, b) => a.start - b.start);

  const baseMinTime = Math.min(...spans.map(span => span.start));

  spans = spans.map(span => ({ ...span, start: span.start - baseMinTime, end: span.end - baseMinTime }));

  const gridWidth = 50;
  const gridStep = gridWidth * (1 / scale);

  const collapsedSpans = collapseEmptyIntervals(spans, gridStep);

  const positionedSpans = assignLevelsToSpans(collapsedSpans);
  const levels = new Set(positionedSpans.map(span => span.level));


  const minTime = Math.min(...spans.map(span => span.start));
  const maxTime = Math.max(...spans.map(span => span.end));

  interface EmptyGrid {
    type: 'empty';
    start: number;
    end: number;
    count: number;
  }

  interface ActiveGrid {
    type: 'active';
    time: number;
  }

  const isCellEmpty = (time: number, step: number) => {
    return !positionedSpans.some(
      span => {
        const leftBound = time - step;
        const rightBound = time + step;
        return span.start <= rightBound && span.end >= leftBound;
      }
    );
  };

  const combinedGrid: (ActiveGrid | EmptyGrid)[] = [];
  let emptyBlockStart: number | null = null;

  for (let time = minTime; time <= maxTime; time += gridStep) {
    if (isCellEmpty(time, gridStep)) {
      if (emptyBlockStart === null) {
        emptyBlockStart = time;
      }
      continue;
    }
    if (emptyBlockStart !== null) {
      const emptyBlockEnd = time - gridStep;
      const emptyCellsCount = Math.round((emptyBlockEnd - emptyBlockStart) / gridStep) + 1;
      combinedGrid.push({ type: 'empty', start: emptyBlockStart, end: emptyBlockEnd, count: emptyCellsCount });
      emptyBlockStart = null;
    }
    combinedGrid.push({ type: 'active', time });
  }

  if (emptyBlockStart !== null) {
    const emptyBlockEnd = maxTime;
    const emptyCellsCount = Math.round((emptyBlockEnd - emptyBlockStart) / gridStep) + 1;
    combinedGrid.push({ type: 'empty', start: emptyBlockStart, end: emptyBlockEnd, count: emptyCellsCount });
  }

  return (
    <Card>
      <Card.Header
        data-bs-toggle="collapse"
        data-bs-target="#gantCollapse"
        aria-expanded="false"
        aria-controls="gantCollapse"
        role="button"
      >
        <Card.Title>Gant Graph</Card.Title>
      </Card.Header>
      <div id="gantCollapse" className="collapse">
        <Row className={'m-2 h-100'}>
          <Container fluid>
            <Row className="mb-2">
              <ButtonGroup>
                <Button onClick={() => setScale(scale * 2)}>Zoom In</Button>
                <Button onClick={() => setScale(scale / 2 <= 0 ? 1 : scale / 2)}>Zoom Out</Button>
              </ButtonGroup>
            </Row>
            <Row className="mb-2">
              <p>Grid Step: {gridStep} ms</p>
              <p>Base Min Time: {baseMinTime} ms</p>
            </Row>
            <Container className="timeline">
              {/* Сетка */}
              <div className="timeline-grid mb-4">
                {combinedGrid.map((grid, index) => {
                  if (grid.type === 'empty') {
                    return (
                      <div
                        key={index}
                        className="grid-line empty-block"
                        style={{
                          left: `${(grid.start - minTime) * scale}px`,
                          width: `${(grid.end - grid.start) * scale}px`,
                          height: '100%',
                          position: 'absolute',
                          borderTop: '1px dashed #aaa',
                          backgroundColor: 'rgba(0, 0, 0, 0.05)',
                        }}
                      >
                        <span
                          className="grid-label"
                          style={{
                            position: 'absolute',
                            top: '0',
                            left: '50%',
                            transform: 'translateX(-50%)',
                            fontSize: '10px',
                            color: '#999',
                          }}
                        >
                          {`Empty (${grid.count} cells)`}
                        </span>
                      </div>
                    );
                  }
                  return (
                    <div
                      key={index}
                      className="grid-line active-cell"
                      style={{
                        left: `${(grid.time - minTime) * scale}px`,
                        height: '100%',
                        position: 'absolute',
                        borderLeft: '1px solid #ddd',
                      }}
                    >
                        <span
                          className="grid-label"
                          style={{
                            position: 'absolute',
                            top: '0',
                            transform: 'translateX(-50%)',
                            fontSize: '10px',
                            color: '#666',
                          }}
                        >
                          {grid.time}
                        </span>
                    </div>
                  );
                })}
              </div>
              {/* Контент */}
              {levels.keys().map(level => (
                <Row
                  className="timeline-content"
                >
                  {positionedSpans
                    .filter(span => span.level === level)
                    .map(span => {
                      const spanStart = (span.start - minTime) * scale;
                      const spanWidth = (span.end - span.start) * scale;
                      const spanOffset = span.skippedSteps * gridWidth;

                      return (
                        <div
                          key={span.id}
                          className="timeline-span"
                          style={{
                            left: `${spanStart - spanOffset}px`,
                            width: `${spanWidth}px`,
                          }}
                          title={span.label}
                        >
                          {span.label}
                        </div>
                      );
                    })}
                </Row>
              ))}
            </Container>
          </Container>
        </Row>
      </div>
    </Card>
  );
};

export default GanttChartPage;
