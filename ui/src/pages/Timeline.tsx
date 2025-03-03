import React from 'react';
import { Badge, Col, Container, ListGroup, Row } from 'react-bootstrap';
import {
  MaterialSymbolsLightCloseSmallOutlineRounded,
  MaterialSymbolsLightCommitRounded,
  MaterialSymbolsLightFiberNewOutlineRounded,
  MaterialSymbolsLightSettingsBackupRestoreRounded
} from '../icons';

export interface Event {
  type: 'create' | 'commit' | 'rollback' | 'close';
  date: Date;
  connectionId: string;
}

export interface Connection {
  id: string;
  events: Event[];
}

export interface TimelineProps {
  connections: Connection[];
}

const Timeline: React.FC<TimelineProps> = ({ connections }) => {
  // Собираем все события в один массив
  const allEvents: Event[] = connections.flatMap((connection) =>
    connection.events.map((event) => ({ ...event, connectionId: connection.id }))
  );

  // Сортируем события по времени
  allEvents.sort((a, b) => a.date.getTime() - b.date.getTime());

  // Находим минимальную и максимальную дату для масштабирования
  const minDate = allEvents[0]?.date || new Date();
  const maxDate = allEvents[allEvents.length - 1]?.date || new Date();

  // Функция для вычисления позиции события на шкале
  const getPosition = (date: Date) => {
    const totalDuration = maxDate.getTime() - minDate.getTime();
    const eventDuration = date.getTime() - minDate.getTime();
    return (eventDuration / totalDuration) * 100;
  };

  return (
    <Container>
      <Row>
        <Col>
          <div style={{ position: 'relative', height: '100px', margin: '20px 0' }}>
            {/* Линия временной шкалы */}
            <div
              style={{
                position: 'absolute',
                top: '50%',
                left: 0,
                right: 0,
                borderTop: '2px solid #ccc',
              }}
            />
            {/* События */}
            {allEvents.map((event, index) => (
              <div
                key={index}
                style={{
                  position: 'absolute',
                  top: '50%',
                  left: `${getPosition(event.date)}%`,
                  transform: 'translate(-50%, -50%)',
                  display: 'flex',
                  alignItems: 'center',
                }}
              >
                {event.type === 'create' && <MaterialSymbolsLightFiberNewOutlineRounded color="blue" />}
                {event.type === 'commit' && <MaterialSymbolsLightCommitRounded color="green" />}
                {event.type === 'rollback' && <MaterialSymbolsLightSettingsBackupRestoreRounded color="red" />}
                {event.type === 'close' && <MaterialSymbolsLightCloseSmallOutlineRounded color="gray" />}
                <Badge bg="light" text="dark" style={{ marginLeft: '5px' }}>
                  {event.date.toLocaleString()}
                </Badge>
              </div>
            ))}
          </div>
        </Col>
      </Row>
      {/* Список подключений */}
      <ListGroup>
        {connections.map((connection) => (
          <ListGroup.Item key={connection.id}>
            <h5>Connection {connection.id}</h5>
            <div style={{ display: 'flex', alignItems: 'center' }}>
              {connection.events.map((event, index) => (
                <React.Fragment key={index}>
                  {index > 0 && <div style={{ flex: 1, borderTop: '2px solid #ccc', margin: '0 10px' }} />}
                  <div style={{ display: 'flex', alignItems: 'center' }}>
                    {event.type === 'create' && <MaterialSymbolsLightFiberNewOutlineRounded color="blue" />}
                    {event.type === 'commit' && <MaterialSymbolsLightCommitRounded color="green" />}
                    {event.type === 'rollback' && <MaterialSymbolsLightSettingsBackupRestoreRounded color="red" />}
                    {event.type === 'close' && <MaterialSymbolsLightCloseSmallOutlineRounded color="gray" />}
                    <Badge bg="light" text="dark" style={{ marginLeft: '5px' }}>
                      {event.date.toLocaleString()}
                    </Badge>
                  </div>
                </React.Fragment>
              ))}
            </div>
          </ListGroup.Item>
        ))}
      </ListGroup>
    </Container>
  );
};

export default Timeline;
