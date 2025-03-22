import React from 'react';
import { Accordion, Badge, Col, Container, OverlayTrigger, Row, Tooltip } from 'react-bootstrap';
import {
  MaterialSymbolsLightCloseSmallOutlineRounded,
  MaterialSymbolsLightCommitRounded,
  MaterialSymbolsLightErrorOutlineRounded,
  MaterialSymbolsLightFiberNewOutlineRounded,
  MaterialSymbolsLightSettingsBackupRestoreRounded
} from '../../../icons';
import { Connection, ConnectionEvent, ConnectionException } from '../../../api/protobuf/connections';
import { CommonDto } from '../../../api/protobuf/common';


export interface TimelineProps {
  common: CommonDto;
  connections: Connection[];
}

interface TransactionIsolation {
  code: string;
  name: string;
}

const transactionIsolations = new Map<number, TransactionIsolation>();
transactionIsolations.set(0, { code: 'TRANSACTION_NONE', name: 'Transactions are not supported' });
transactionIsolations.set(1, { code: 'TRANSACTION_READ_UNCOMMITTED', name: 'Read uncommitted' });
transactionIsolations.set(2, { code: 'TRANSACTION_READ_COMMITTED', name: 'Read committed' });
transactionIsolations.set(4, { code: 'TRANSACTION_REPEATABLE_READ', name: 'Repeatable read' });
transactionIsolations.set(8, { code: 'TRANSACTION_SERIALIZABLE', name: 'Serializable' });


const Timeline: React.FC<TimelineProps> = ({
                                             common,
                                             connections
                                           }) => {
  const getIconTooltip = (id: string, tooltip: string, transactionIsolation?: number) => {
    const isolation = transactionIsolation ? (transactionIsolations.get(transactionIsolation)?.name ?? '') : '';

    return (
      <Tooltip id={`event-${id}-tooltip`}>{tooltip}{isolation ? ` - ${isolation}` : ''}</Tooltip>
    );
  };
  const getExceptionTooltip = (id: string, exception: ConnectionException) => {
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
    switch (common.stringConstants[event.action]) {
      case 'CREATE':
        return (
          <OverlayTrigger
            placement="left"
            delay={{ show: 0, hide: 250 }}
            overlay={getIconTooltip(connectionId, 'Create', event.transactionIsolation)}
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
            overlay={getIconTooltip(connectionId, 'Commit', event.transactionIsolation)}
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
            overlay={getIconTooltip(connectionId, 'Rollback', event.transactionIsolation)}
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
            overlay={getIconTooltip(connectionId, 'Close', event.transactionIsolation)}
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

  const getCreationThread = (connection: Connection) => {
    if (connection.events.length === 0) {
      return null;
    }
    const event = connection.events[0];
    if (!event || common.stringConstants[event.action] !== 'CREATE') {
      return null;
    }
    return event.threadName ?? null;
  };

  const getClosingThread = (connection: Connection) => {
    if (connection.events.length === 0) {
      return null;
    }
    const event = connection.events[connection.events.length - 1];
    if (!event || common.stringConstants[event.action] !== 'CLOSE') {
      return null;
    }
    return event.threadName ?? null;
  };

  return (
    <Container>
      {/* Список подключений */}
      <Accordion>
        {connections.map((connection) => {
          const creationThread = getCreationThread(connection);
          const closingThread = getClosingThread(connection);

          return (
            <Accordion.Item key={connection.id} eventKey={connection.id}>
              <Accordion.Header>
                <Container fluid={true}>
                  <Row>
                    <h5>Connection {connection.id}</h5>
                  </Row>
                  <Row
                    style={{
                      overflowX: 'auto',
                      padding: '8px 0',
                      whiteSpace: 'nowrap'
                    }}
                  >
                    <div style={{ display: 'flex', alignItems: 'center' }}>
                      {connection.events.map((event, index) => {
                        const transactionIsolation = event.transactionIsolation ? transactionIsolations.get(event.transactionIsolation) : null;

                        return (
                          <React.Fragment key={`timeline-${event.index}`}>
                            {index > 0 && (
                              <OverlayTrigger
                                placement="top"
                                delay={{ show: 50, hide: 250 }}
                                overlay={getLineTooltip(
                                  connection.id,
                                  +new Date(event.startedAt) - +new Date(connection.events[index - 1]?.startedAt ?? 0)
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
                              {transactionIsolation && (
                                <Badge
                                  bg="light"
                                  text="dark"
                                  className={'mb-2'}
                                  title={transactionIsolation.code}
                                >
                                  {transactionIsolation.name}
                                </Badge>
                              )}
                              {getEventIcon(connection.id, event)}
                              <Badge
                                bg="light"
                                text="dark"
                                className={'mt-2'}
                                title={common.stringConstants[event.action]}
                              >
                                {formatDate(event.startedAt)}
                              </Badge>
                            </div>
                          </React.Fragment>
                        );
                      })}
                    </div>
                  </Row>
                </Container>
              </Accordion.Header>
              <Accordion.Body>
                <Container fluid={true}>
                  <Row>
                    <Col xxl={2}>
                      Duration
                    </Col>
                    <Col xxl={2}>
                      {connection.duration} ms
                    </Col>
                    <Col xxl={2}>
                      Has exception
                    </Col>
                    <Col xxl={2}>
                      {connection.hasExceptions ? 'Yes' : 'No'}
                    </Col>
                    <Col xxl={2}>
                      Events
                    </Col>
                    <Col xxl={2}>
                      {connection.events.length}
                    </Col>
                  </Row>
                  <Row>
                    {creationThread && (
                      <>
                        <Col xxl={2}>
                          Creation Thread
                        </Col>
                        <Col xxl={2}>
                          {creationThread}
                        </Col>
                      </>
                    )}
                    {closingThread && (
                      <>
                        <Col xxl={2}>
                          Closing Thread
                        </Col>
                        <Col xxl={2}>
                          {closingThread}
                        </Col>
                      </>
                    )}
                  </Row>
                </Container>
              </Accordion.Body>
            </Accordion.Item>
          );
        })}
      </Accordion>
    </Container>
  );
};

export default Timeline;
