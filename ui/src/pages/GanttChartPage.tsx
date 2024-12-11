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

interface LeveledSpan extends Span {
  level: number;
}

interface GanttChartPageProps {
  spans: Span[];
}

const assignLevelsToSpans = (spans: Span[]): LeveledSpan[] => {
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

const GanttChartPage: React.FC<GanttChartPageProps> = ({ spans }) => {
  const [scale, setScale] = useState(1);
  spans.sort((a, b) => a.start - b.start);

  const baseMinTime = Math.min(...spans.map(span => span.start));
  const baseMaxTime = Math.max(...spans.map(span => span.start));

  spans = spans.map(span => ({ ...span, start: span.start - baseMinTime, end: span.end - baseMinTime }));

  const gridWidth = 50;
  const gridStep = gridWidth * (1 / scale);

  const leveledSpans = assignLevelsToSpans(spans);
  const levels = new Set(leveledSpans.map(span => span.level));

  const minTime = 0;
  const maxTime = baseMaxTime - baseMinTime;

  const gridLines = [];
  for (let time = minTime; time <= maxTime; time += gridStep) {
    gridLines.push(time);
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
        <Card.Title>Gantt Graph</Card.Title>
      </Card.Header>
      <div id="gantCollapse" className="collapse">
        <Row className="m-2 h-100">
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
                {gridLines.map((time, index) => (
                  <div
                    key={index}
                    className="grid-line"
                    style={{
                      left: `${(time - minTime) * scale}px`,
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
                      {time}
                    </span>
                  </div>
                ))}
              </div>
              {/* Контент */}
              {levels.keys().map(level => (
                <Row
                  key={`level-${level}`}
                  className="timeline-content"
                >
                  {leveledSpans
                    .filter(span => span.level === level)
                    .map(span => {
                      const spanStart = (span.start - minTime) * scale;
                      const spanWidth = (span.end - span.start) * scale;

                      return (
                        <div
                          key={span.id}
                          className="timeline-span"
                          style={{
                            left: `${spanStart}px`,
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
