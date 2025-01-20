import React, { useState } from 'react';
import {
  Button,
  ButtonGroup,
  Card,
  Container,
  Row
} from 'react-bootstrap';
import './GanttChartPage.css';
import { format } from 'date-fns';
import { v4 as uuidv4 } from 'uuid';

interface Span {
  id: string;
  start: number;
  end: number;
  label: string;
}

interface UniqueSpan extends Span {
  uuid: string;
}

interface LeveledSpan extends UniqueSpan {
  level: number;
}

interface GanttChartPageProps {
  spans: Span[];
}

const assignLevelsToSpans = (spans: UniqueSpan[]): LeveledSpan[] => {
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
  const [tooltip, setTooltip] = useState<{ span: UniqueSpan | null; x: number; y: number }>({ span: null, x: 0, y: 0 });

  spans.sort((a, b) => a.start - b.start);

  const baseMinTime = Math.min(...spans.map(span => span.start));
  const baseMaxTime = Math.max(...spans.map(span => span.start));

  const uniqueSpans = spans.map(
    span => (
      {
        ...span,
        uuid: uuidv4(),
        start: span.start - baseMinTime,
        end: span.end - baseMinTime
      }
    )
  );

  const gridWidth = 50;
  const gridStep = gridWidth * (1 / scale);

  const leveledSpans = assignLevelsToSpans(uniqueSpans);
  const levels = new Set(leveledSpans.map(span => span.level));

  const minTime = 0;
  const maxTime = baseMaxTime - baseMinTime;

  const gridLines = [];
  for (let time = minTime; time <= maxTime; time += gridStep) {
    gridLines.push(time);
  }

  const handleMouse = (span: UniqueSpan, e: React.MouseEvent) => {
    e.stopPropagation();
    if (tooltip.span?.uuid !== span.uuid) {
      setTooltip({ span, x: e.clientX, y: e.clientY });
    } else {
      setTooltip({ span: null, x: 0, y: 0 });
    }
  };

  window.onmouseup = () => {
    setTooltip({ span: null, x: 0, y: 0 });
  };

  const formatZonedDateTime = (time: number) => {
    return format(new Date(time), 'yyyy-MM-dd\'T\'HH:mm:ss.SSS');
  };

  return (
    <>
      {tooltip.span && (
        <div
          className="tooltip-window"
          style={{
            top: tooltip.y,
            left: tooltip.x,
          }}
        >
          <p><strong>Label:</strong> <code>{tooltip.span.label}</code></p>
          <p><strong>Start Time:</strong> {formatZonedDateTime(tooltip.span.start + baseMinTime)}</p>
          <p><strong>End Time:</strong> {formatZonedDateTime(tooltip.span.end + baseMinTime)}</p>
          <p><strong>Duration:</strong> {tooltip.span.end - tooltip.span.start} ms</p>
        </div>
      )}
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
                  <Button variant={'outline-primary'} onClick={() => setScale(scale * 2)}>Zoom In</Button>
                  <Button variant={'outline-primary'} onClick={() => setScale(scale / 2 <= 0 ? 1 : scale / 2)}>Zoom Out</Button>
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
                        left: `${(time - minTime) * scale}px`
                      }}
                    >
                      <span className="grid-label">{time}</span>
                    </div>
                  ))}
                </div>
                {/* Контент */}
                {Array.from(levels).map(level => (
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
                            key={span.uuid}
                            className="timeline-span"
                            style={{
                              left: `${spanStart}px`,
                              width: `${spanWidth}px`,
                            }}
                            title={span.label}
                            onMouseUp={(e) => handleMouse(span, e)}
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
    </>
  );
};

export default GanttChartPage;
