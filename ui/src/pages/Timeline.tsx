import React from 'react';
import { Badge, Col, Container, ListGroup } from 'react-bootstrap';
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
  return (
    <Container>
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
                      {event.date.toISOString()}
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
