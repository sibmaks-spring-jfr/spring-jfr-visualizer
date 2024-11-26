import React, { useState } from 'react';
import {
  Button,
  ButtonGroup,
  Card,
  Col,
  Container,
  Form,
  FormLabel,
  FormSelect,
  InputGroup,
  Row
} from 'react-bootstrap';
import './GanttChartPage.css';

interface Span {
  id: string;
  start: number; // Start time in milliseconds
  end: number; // End time in milliseconds
  label: string; // Caption
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

    // Найти первый доступный уровень
    while (
      levels[level] &&
      levels[level].some(existingSpan =>
        // Проверяем пересечение по времени
        existingSpan.start < span.end &&
        span.start < existingSpan.end
      )
      ) {
      level++;
    }

    // Добавляем спан на найденный уровень
    if (!levels[level]) levels[level] = [];
    levels[level].push(span);

    return { ...span, level };
  });
};

const GanttChartPage: React.FC<GanttChartPageProps> = ({ spans }) => {
  const [scale, setScale] = useState(1);
  const positionedSpans = assignLevelsToSpans(spans);
  const levels = new Set(positionedSpans.map(span => span.level));

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
                <Button onClick={() => setScale(scale / 2)}>Zoom Out</Button>
              </ButtonGroup>
            </Row>
            <Container className="timeline">
              {levels.keys().map(level => (
                <Row
                  className="timeline-content"
                >
                  {positionedSpans
                    .filter(span => span.level === level)
                    .map(span => {
                      const spanStart = span.start * scale;
                      const spanWidth = (span.end - span.start) * scale;

                      return (
                        <div
                          key={span.id}
                          className="timeline-span"
                          style={{
                            left: `${spanStart}px`,
                            width: `${spanWidth}px`,
                            top: '0',
                            height: '30px',
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
