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

interface PositionedSpan extends Span {
  level: number;
}

const assignLevelsToSpans = (spans: Span[]): PositionedSpan[] => {
  spans.sort((a, b) => a.start - b.start);

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
  const positionedSpans = assignLevelsToSpans(spans);
  const levels = new Set(positionedSpans.map(span => span.level));

  const gridStep = 50 * (1 / scale);

  const minTime = Math.min(...positionedSpans.map(span => span.start));
  const maxTime = Math.max(...positionedSpans.map(span => span.end));
  console.log(minTime, maxTime)

  const gridLines = [];
  for (let time = minTime; time <= maxTime; time += gridStep) {
    gridLines.push(time);
  }

  return (
    <Card>
      <Card.Header data-bs-toggle="collapse"
                   data-bs-target="#gantCollapse"
                   aria-expanded="false"
                   aria-controls="gantCollapse"
                   role="button">
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
              Grid Step: {gridStep} ms
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
                  className="timeline-content"
                >
                  {positionedSpans
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
                            width: `${spanWidth}px`
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
