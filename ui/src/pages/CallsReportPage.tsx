import React, { useEffect, useState } from 'react';
import { Badge, Card, Col, Container, Dropdown, ListGroup, Row, Table } from 'react-bootstrap';
import { CallTrace } from '../api/types';
import BackButton from '../components/BackButton';
import { toLocalDateTime } from '../utils/datetime';

interface CallReport {
  roots: CallTrace[];
}

const CallTraceTree: React.FC<{ trace: CallTrace }> = ({ trace }) => (
  <ListGroup>
    <ListGroup.Item>
      <strong>{trace.type}</strong> - {trace.invocationId}
      <Badge bg={trace.success ? 'success' : 'danger'}>
        {trace.success ? 'Success' : 'Fail'}
      </Badge>
      <div>Start: {toLocalDateTime(trace.startTime)}</div>
      <div>End: {toLocalDateTime(trace.endTime)}</div>
      {trace.parameters && (
        <div>
          <strong>Parameters:</strong>
          <ul>
            {Object.entries(trace.parameters).map(([key, value]) => (
              <li key={key}>
                {key}: {value}
              </li>
            ))}
          </ul>
        </div>
      )}
      {trace.children.length > 0 && (
        <div style={{ marginLeft: '20px', marginTop: '10px' }}>
          {trace.children.map((child) => (
            <CallTraceTree key={child.contextId} trace={child} />
          ))}
        </div>
      )}
    </ListGroup.Item>
  </ListGroup>
);

const CallsReportPage = () => {
  const [roots, setRoots] = useState<CallTrace[]>([]);

  useEffect(() => {
    // @ts-ignore
    const callsJson = window.callsJson || '{"roots":[]}';
    const report = JSON.parse(callsJson) as CallReport;
    setRoots(report.roots);
  }, []);

  const [selectedTrace, setSelectedTrace] = useState<CallTrace | null>(null);

  return (
    <Container>
      <Row className={'mt-4 mb-4'}>
        <h3><BackButton /> Calls Report</h3>
        <Col>
          <h2>Выбор корневого вызова</h2>
          <Dropdown>
            <Dropdown.Toggle variant="primary">Выберите корневой вызов</Dropdown.Toggle>
            <Dropdown.Menu>
              {roots.map((trace) => (
                <Dropdown.Item
                  key={trace.contextId}
                  onClick={() => setSelectedTrace(trace)}
                >
                  {trace.invocationId}
                </Dropdown.Item>
              ))}
            </Dropdown.Menu>
          </Dropdown>
        </Col>
      </Row>
      <Row>
        <Col>
          {selectedTrace ? (
            <Card style={{ marginTop: '20px' }}>
              <Card.Header>
                <h3>Дерево вызовов для {selectedTrace.invocationId}</h3>
              </Card.Header>
              <Card.Body>
                <CallTraceTree trace={selectedTrace} />
              </Card.Body>
            </Card>
          ) : (
            <p style={{ marginTop: '20px' }}>Выберите корневой вызов из списка.</p>
          )}
        </Col>
      </Row>
    </Container>
  );
};


export default CallsReportPage;
