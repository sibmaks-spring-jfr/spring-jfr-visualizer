import React, { useContext, useEffect, useState } from 'react';
import { Accordion, Badge, Button, Col, Container, Row, Table } from 'react-bootstrap';
import { CallTrace } from '../../../api/types';
import { toISOString } from '../../../utils/datetime';
import { useNavigate, useParams } from 'react-router-dom';
import { Loader } from '@sibdevtools/frontend-common';
import { RootReportContext } from '../../../context/RootReportProvider';

const getTraceStatusBadge = (trace: CallTrace) => {
  return (
    <Badge bg={trace.success ? 'success' : 'danger'}>
      {trace.success ? 'Success' : 'Fail'}
    </Badge>
  );
};

const getTraceName = (trace: CallTrace) => {
  return (
    <>
      <strong>{trace.type}</strong>{' '} - {trace.className}#{trace.methodName}{' '}
      - {getTraceStatusBadge(trace)} - {trace.endTime - trace.startTime} ms
    </>
  );
};

function getCallTraceSystemDescription(trace: CallTrace) {
  return (
    <Row className={'mb-2'}>
      <Row>
        <Col md={2}>
          <strong>Thread Name:</strong>
        </Col>
        <Col md={10}>
          <code>{trace.threadName}</code>
        </Col>
      </Row>
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
      <Row>
        <Col md={2}>
          <strong>Status:</strong>
        </Col>
        <Col md={10}>
          {getTraceStatusBadge(trace)}
        </Col>
      </Row>
    </Row>
  );
}

function getCallTraceDetails(trace: CallTrace) {
  if (Object.keys(trace.details).length <= 0) {
    return <></>;
  }
  return (
    <Row className={'mb-2'}>
      <Accordion className="mt-3">
        <Accordion.Item eventKey={`${trace.contextId + trace.invocationId}-parameters`}>
          <Accordion.Header><strong>Details</strong></Accordion.Header>
          <Accordion.Body>
            <Table bordered={true}>
              <thead className={'table-dark'}>
              <tr>
                <th>Name</th>
                <th>Value</th>
              </tr>
              </thead>
              <tbody>
              {Object.entries(trace.details).map(([key, value]) => (
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
  );
}

const CallTraceTree: React.FC<{ trace: CallTrace }> = ({ trace }) => {
  return (
    <Accordion.Item eventKey={trace.contextId + trace.invocationId}>
      <Accordion.Header>
        {getTraceName(trace)}
      </Accordion.Header>
      <Accordion.Body>
        {getCallTraceSystemDescription(trace)}
        {getCallTraceDetails(trace)}
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
  const { rootReport, isLoading } = useContext(RootReportContext);

  const navigate = useNavigate();
  const { contextId, callId } = useParams();
  const [context2id2Trace, setContext2id2Trace] = useState<Map<string, Map<string, CallTrace>>>(new Map());

  useEffect(() => {
    const context2id2Trace = new Map<string, Map<string, CallTrace>>();
    for (let root of rootReport.calls.roots) {
      let id2Trace = context2id2Trace.get(root.contextId);
      if (!id2Trace) {
        id2Trace = new Map();
        context2id2Trace.set(root.contextId, id2Trace);
      }
      id2Trace.set(root.invocationId, root);
    }
    setContext2id2Trace(context2id2Trace);
  }, [rootReport]);

  if (!context2id2Trace || !contextId || !callId) {
    navigate('/calls');
    return;
  }

  const id2Trace = context2id2Trace.get(contextId);
  const callTrace = id2Trace?.get(callId);
  if (!callTrace) {
    navigate('/calls');
    return;
  }

  return (
    <Container>
      <Row className={'mt-4 mb-4'}>
        <h3>
          <Button variant={'outline-secondary'} onClick={() => navigate('/calls')}>Back</Button> Call Trace Report
        </h3>
      </Row>
      <Loader loading={isLoading}>
        {getCallTraceSystemDescription(callTrace)}
        {getCallTraceDetails(callTrace)}
        {callTrace.children.length > 0 && (
          <>
            <h4>Children</h4>
            <Row>
              <Accordion>
                {callTrace.children.map((child) => (
                  <CallTraceTree key={child.contextId + child.invocationId} trace={child} />
                ))}
              </Accordion>
            </Row>
          </>
        )}
      </Loader>
    </Container>
  );
};


export default CallReportPage;
