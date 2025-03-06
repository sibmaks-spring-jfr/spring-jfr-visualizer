import React from 'react';
import { Badge, Container, ListGroup, OverlayTrigger, Tooltip } from 'react-bootstrap';
import {
  MaterialSymbolsLightCloseSmallOutlineRounded,
  MaterialSymbolsLightCommitRounded,
  MaterialSymbolsLightErrorOutlineRounded,
  MaterialSymbolsLightFiberNewOutlineRounded,
  MaterialSymbolsLightSettingsBackupRestoreRounded
} from '../../../icons';
import { Connection, ConnectionEvent, Exception } from '../../../api/types';


export interface TimelineProps {
  connections: Connection[];
}

const Timeline: React.FC<TimelineProps> = ({ connections }) => {
  const getIconTooltip = (id: string, tooltip: string) => {
    return (
      <Tooltip id={`event-${id}-tooltip`}>{tooltip}</Tooltip>
    );
  };
  const getExceptionTooltip = (id: string, exception: Exception) => {
    return (
      <Tooltip id={`event-${id}-exception-tooltip`}>{exception.type}: {exception.message}</Tooltip>
    );
  };
  const getLineTooltip = (id: string, duration: number) => {
    return (
      <Tooltip id={`event-${id}-line-tooltip`}>{duration} ms</Tooltip>
    );
  };

  const getIcon = (connectionId: string, event: ConnectionEvent) => {
    switch (event.type) {
      case 'CREATE':
        return (
          <OverlayTrigger
            placement="left"
            delay={{ show: 50, hide: 250 }}
            overlay={getIconTooltip(connectionId, 'Create')}
          >
            <div>
              <MaterialSymbolsLightFiberNewOutlineRounded color="blue" />
            </div>
          </OverlayTrigger>
        );
      case 'COMMIT':
        return (
          <OverlayTrigger
            placement="left"
            delay={{ show: 50, hide: 250 }}
            overlay={getIconTooltip(connectionId, 'Commit')}
          >
            <div>
              <MaterialSymbolsLightCommitRounded color="green" />
            </div>
          </OverlayTrigger>
        );
      case 'ROLLBACK':
        return (
          <OverlayTrigger
            placement="left"
            delay={{ show: 50, hide: 250 }}
            overlay={getIconTooltip(connectionId, 'Rollback')}
          >
            <div>
              <MaterialSymbolsLightSettingsBackupRestoreRounded color="red" />
            </div>
          </OverlayTrigger>
        );
      case 'CLOSE':
        return (
          <OverlayTrigger
            placement="left"
            delay={{ show: 50, hide: 250 }}
            overlay={getIconTooltip(connectionId, 'Close')}
          >
            <div>
              <MaterialSymbolsLightCloseSmallOutlineRounded color="gray" />
            </div>
          </OverlayTrigger>
        );
      default:
        return <></>;
    }
  };

  const getEventIcon = (connectionId: string, event: ConnectionEvent) => {
    if (!event.exception) {
      return (
        <>
          {getIcon(connectionId, event)}
        </>
      );
    }
    return (
      <>
        {getIcon(connectionId, event)}
        <OverlayTrigger
          placement="left"
          delay={{ show: 50, hide: 250 }}
          overlay={getExceptionTooltip(connectionId, event.exception)}
        >
          <div>
            <MaterialSymbolsLightErrorOutlineRounded color={'red'} />
          </div>
        </OverlayTrigger>
      </>
    );
  };

  const formatDate = (date: number) => {
    return new Date(date).toISOString();
  };

  return (
    <Container>
      {/* Список подключений */}
      <ListGroup>
        {connections.map((connection) => (
          <ListGroup.Item key={connection.id}>
            <h5>Connection {connection.id}</h5>
            <div
              style={{
                overflowX: 'auto',
                padding: '8px 0',
                whiteSpace: 'nowrap'
              }}
            >
              <div style={{ display: 'flex', alignItems: 'center' }}>
                {connection.events.map((event, index) => (
                  <React.Fragment key={index}>
                    {index > 0 && (
                      <OverlayTrigger
                        placement="top"
                        delay={{ show: 50, hide: 250 }}
                        overlay={getLineTooltip(
                          connection.id,
                          +new Date(event.date) - +new Date(connection.events[index - 1]?.date ?? 0)
                        )}
                      >
                        <div
                          style={{
                            borderTop: '3px solid #ccc',
                            margin: '0 16px',
                            minWidth: '24px',
                            flex: 1
                          }}
                        />
                      </OverlayTrigger>
                    )}
                    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                      {getEventIcon(connection.id, event)}
                      <Badge bg="light" text="dark" style={{ marginTop: '5px' }} title={event.type}>
                        {formatDate(event.date)}
                      </Badge>
                    </div>
                  </React.Fragment>
                ))}
              </div>
            </div>
          </ListGroup.Item>
        ))}
      </ListGroup>
    </Container>
  );
};

export default Timeline;
