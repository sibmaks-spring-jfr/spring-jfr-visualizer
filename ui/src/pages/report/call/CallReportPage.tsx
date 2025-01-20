import React, { useContext, useEffect, useState } from 'react';
import { Accordion, Badge, Button, Col, Container, Row, Table } from 'react-bootstrap';
import { CallTrace } from '../../../api/types';
import BackButton from '../../../components/BackButton';
import { toISOString } from '../../../utils/datetime';
import { useNavigate, useParams } from 'react-router-dom';
import { CallReportContext } from '../../../context/CallReportProvider';
import { Loader } from '../../../components/Loader';

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

function getCallTraceDetails(trace: CallTrace) {
  return (
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

function getCallTraceParameters(trace: CallTrace) {
  if (Object.keys(trace.parameters).length <= 0) {
    return <></>;
  }
  return (
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
  );
}

const CallTraceTree: React.FC<{ trace: CallTrace }> = ({ trace }) => {
  return (
    <Accordion.Item eventKey={trace.contextId + trace.invocationId}>
      <Accordion.Header>
        {getTraceName(trace)}
      </Accordion.Header>
      <Accordion.Body>
        {getCallTraceDetails(trace)}
        {getCallTraceParameters(trace)}
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
  const { context2id2Trace, isLoading } = useContext(CallReportContext);

  const navigate = useNavigate();
  const { contextId, callId } = useParams();

  if (isLoading) {
    return (
      <Loader />
    );
  }

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
          <Button variant={'outline-secondary'} onClick={() => navigate('/calls')}>Back</Button> Call
          Trace <code>{callTrace.invocationId}</code> Report
        </h3>
      </Row>
      {getCallTraceDetails(callTrace)}
      {getCallTraceParameters(callTrace)}
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
    </Container>
  );
};


export default CallReportPage;
