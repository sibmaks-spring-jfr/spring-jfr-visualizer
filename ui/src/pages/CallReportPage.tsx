import React, { useEffect, useState } from 'react';
import { Accordion, Badge, Col, Container, Row, Table } from 'react-bootstrap';
import { CallTrace } from '../api/types';
import BackButton from '../components/BackButton';
import { toISOString } from '../utils/datetime';

interface CallReport {
  roots: CallTrace[];
}

const getTraceName = (trace: CallTrace) => {
  return (
    <>
      <strong>{trace.type}</strong>{' '} - {trace.className}#{trace.methodName}{' '}
      <Badge bg={trace.success ? 'success' : 'danger'} className="ms-2">
        {trace.success ? 'Success' : 'Fail'}
      </Badge>
      <span className={'float-end'}>{' '}{trace.endTime - trace.startTime} ms</span>
    </>
  );
};

const CallTraceTree: React.FC<{ trace: CallTrace }> = ({ trace }) => {
  return (
    <Accordion.Item eventKey={trace.contextId + trace.invocationId}>
      <Accordion.Header>
        {getTraceName(trace)}
      </Accordion.Header>
      <Accordion.Body>
        <Row className={'mb-2'}>
          <Row>
            <Col md={2}>
              <strong>Class Name:</strong>
            </Col>
            <Col md={10}>
              <code>{trace.className}</code>
            </Col>
          </Row>
          <Row>
            <Col md={2}>
              <strong>Method Name:</strong>
            </Col>
            <Col md={10}>
              <code>{trace.methodName}</code>
            </Col>
          </Row>
          <Row>
            <Col md={2}>
              <strong>Start:</strong>
            </Col>
            <Col md={10}>
              {toISOString(trace.startTime)}
            </Col>
          </Row>
          <Row>
            <Col md={2}>
              <strong>End:</strong>
            </Col>
            <Col md={10}>
              {toISOString(trace.endTime)}
            </Col>
          </Row>
          <Row>
            <Col md={2}>
              <strong>Duration:</strong>
            </Col>
            <Col md={10}>
              {trace.endTime - trace.startTime} ms
            </Col>
          </Row>
        </Row>
        {Object.keys(trace.parameters).length > 0 && (
          <Row className={'mb-2'}>
            <Accordion className="mt-3">
              <Accordion.Item eventKey={`${trace.contextId + trace.invocationId}-parameters`}>
                <Accordion.Header><strong>Parameters</strong></Accordion.Header>
                <Accordion.Body>
                  <Table bordered={true}>
                    <thead className={'table-dark'}>
                    <tr>
                      <th>Name</th>
                      <th>Value</th>
                    </tr>
                    </thead>
                    <tbody>
                    {Object.entries(trace.parameters).map(([key, value]) => (
                      <tr key={key}>
                        <td>{key}</td>
                        <td>{value}</td>
                      </tr>
                    ))}
                    </tbody>
                  </Table>
                </Accordion.Body>
              </Accordion.Item>
            </Accordion>
          </Row>
        )}
        {trace.children.length > 0 && (
          <>
            <h4>Children</h4>
            <Row>
              <Accordion>
                {trace.children.map((child) => (
                  <CallTraceTree key={child.contextId + child.invocationId} trace={child} />
                ))}
              </Accordion>
            </Row>
          </>
        )}
      </Accordion.Body>
    </Accordion.Item>
  );
};

const CallReportPage = () => {
  const [roots, setRoots] = useState<CallTrace[]>([]);

  useEffect(() => {
    // @ts-ignore
    const callsJson = window.callsJson || '{"roots":[]}';
    const report = JSON.parse(callsJson) as CallReport;
    setRoots(report.roots);
  }, []);

  return (
    <Container>
      <Row className={'mt-4 mb-4'}>
        <h3><BackButton /> Call Report</h3>
      </Row>
      <Row>
        <Col>
          <Accordion>
            {roots.map(root => (
              <CallTraceTree trace={root} />
            ))}
          </Accordion>
        </Col>
      </Row>
    </Container>
  );
};


export default CallReportPage;
