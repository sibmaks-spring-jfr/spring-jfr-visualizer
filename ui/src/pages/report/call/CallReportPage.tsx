import React, { useContext, useEffect, useState } from 'react';
import { Accordion, Alert, Badge, Button, Col, Container, Form, InputGroup, Row, Table } from 'react-bootstrap';
import { InvocationType } from '../../../api/types';
import { toISOString } from '../../../utils/datetime';
import { useNavigate, useParams } from 'react-router-dom';
import { Loader } from '@sibdevtools/frontend-common';
import { RootReportContext } from '../../../context/RootReportProvider';
import { CommonDto } from '../../../api/protobuf/common';
import { CallTrace } from '../../../api/protobuf/calls';

const MAX_CHILDREN_ON_PAGE = 25;

type Status = 'all' | 'success' | 'fail'

const getTraceStatusBadge = (trace: CallTrace) => {
  return (
    <Badge bg={trace.success ? 'success' : 'danger'}>
      {trace.success ? 'Success' : 'Fail'}
    </Badge>
  );
};

const getTraceName = (stringConstants: Record<number, string>, trace: CallTrace) => {
  return (
    <>
      <strong>{stringConstants[trace.type]}</strong>{' '} - {stringConstants[trace.className]}#{stringConstants[trace.methodName]}{' '}
      - {getTraceStatusBadge(trace)} - {trace.endTime - trace.startTime} ms
    </>
  );
};

function getCallTraceSystemDescription(stringConstants: Record<number, string>, trace: CallTrace) {
  return (
    <Row className={'mb-2'}>
      <Row>
        <Col md={2}>
          <strong>Thread Name:</strong>
        </Col>
        <Col md={10}>
          <code>{stringConstants[trace.threadName]}</code>
        </Col>
      </Row>
      <Row>
        <Col md={2}>
          <strong>Class Name:</strong>
        </Col>
        <Col md={10}>
          <code>{stringConstants[trace.className]}</code>
        </Col>
      </Row>
      <Row>
        <Col md={2}>
          <strong>Method Name:</strong>
        </Col>
        <Col md={10}>
          <code>{stringConstants[trace.methodName]}</code>
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

function getCallTraceDetails(stringConstants: Record<number, string>, trace: CallTrace) {
  if (Object.keys(trace.details).length <= 0) {
    return <></>;
  }
  return (
    <Row className={'mb-2'}>
      <Accordion className="mt-3">
        <Accordion.Item eventKey={`${trace.invocationId}-parameters`}>
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
                  <td>{stringConstants[+key]}</td>
                  <td>{stringConstants[value]}</td>
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

interface CallTraceTreeProps {
  common: CommonDto;
  trace: CallTrace;
}

const CallTraceTree: React.FC<CallTraceTreeProps> = ({
                                                       common,
                                                       trace
                                                     }) => {
  const [minDurationFilter, setMinDurationFilter] = useState<number | null>(null);
  const [maxDurationFilter, setMaxDurationFilter] = useState<number | null>(null);
  const [statusFilter, setStatusFilter] = useState<Status>('all');
  const [typeFilter, setTypeFilter] = useState<InvocationType>();

  let children = [...(trace.children ?? [])];

  if (minDurationFilter !== null) {
    children = children.filter(it => (it.endTime - it.startTime) >= minDurationFilter);
  }

  if (maxDurationFilter !== null) {
    children = children.filter(it => (it.endTime - it.startTime) <= maxDurationFilter);
  }

  if (statusFilter !== 'all') {
    children = children.filter(it => (it.success ? 'success' : 'fail') === statusFilter);
  }

  if (typeFilter) {
    children = children.filter(it => common.stringConstants[it.type] === typeFilter);
  }

  return (
    <Accordion.Item eventKey={`${trace.invocationId}`}>
      <Accordion.Header>
        {getTraceName(common.stringConstants, trace)}
      </Accordion.Header>
      <Accordion.Body>
        {getCallTraceSystemDescription(common.stringConstants, trace)}
        {getCallTraceDetails(common.stringConstants, trace)}
        {trace.children.length > 0 && (
          <>
            <Row className={'mb-2'}>
              <Form className="p-2 shadow bg-body-tertiary rounded">
                <Row className={'mb-2'}>
                  <h4>Children</h4>
                </Row>
                <Row className={'mb-2'}>
                  <Col xxl={6}>
                    <Row>
                      <Col xxl={2} md={12}>
                        <Form.Label htmlFor={'minDurationInput'}>Duration</Form.Label>
                      </Col>
                      <Col xxl={10} md={12}>
                        <InputGroup>
                          <InputGroup.Text><label htmlFor={'minDurationInput'}>from</label></InputGroup.Text>
                          <Form.Control
                            id={'minDurationInput'}
                            type="number"
                            min={0}
                            value={minDurationFilter === null ? '' : minDurationFilter}
                            onChange={(e) => setMinDurationFilter(e.target.value ? parseInt(e.target.value, 10) : null)}
                          />
                          <InputGroup.Text><label htmlFor={'maxDurationInput'}>to</label></InputGroup.Text>
                          <Form.Control
                            id={'maxDurationInput'}
                            type="number"
                            min={minDurationFilter || 0}
                            value={maxDurationFilter === null ? '' : maxDurationFilter}
                            onChange={(e) => setMaxDurationFilter(e.target.value ? parseInt(e.target.value, 10) : null)}
                          />
                          <InputGroup.Text>ms</InputGroup.Text>
                        </InputGroup>
                      </Col>
                    </Row>
                  </Col>
                  <Col xxl={3}>
                    <Row>
                      <Col xxl={2} md={12}>
                        <Form.Label htmlFor={'statusSelect'}>Status</Form.Label>
                      </Col>
                      <Col xxl={10} md={12}>
                        <Form.Select
                          id={'statusSelect'}
                          value={statusFilter}
                          onChange={(e) => setStatusFilter(e.target.value as Status)}
                        >
                          <option value="all">All</option>
                          <option value="success">Success</option>
                          <option value="fail">Fail</option>
                        </Form.Select>
                      </Col>
                    </Row>
                  </Col>
                  <Col xxl={3}>
                    <Row>
                      <Col xxl={2} md={12}>
                        <Form.Label htmlFor={'typeSelect'}>Type</Form.Label>
                      </Col>
                      <Col xxl={10} md={12}>
                        <Form.Select
                          id={'typeSelect'}
                          value={typeFilter}
                          onChange={(e) => setTypeFilter(e.target.value ? e.target.value as InvocationType : undefined)}
                        >
                          <option value={''}>All</option>
                          <option value="ASYNC">ASYNC</option>
                          <option value="JPA">JPA</option>
                          <option value="CONTROLLER">CONTROLLER</option>
                          <option value="SCHEDULED">SCHEDULED</option>
                          <option value="SERVICE">SERVICE</option>
                          <option value="COMPONENT">COMPONENT</option>
                        </Form.Select>
                      </Col>
                    </Row>
                  </Col>
                </Row>
              </Form>
            </Row>
            <Row>
              {children.length > MAX_CHILDREN_ON_PAGE && (
                <Row className="p-4">
                  <Alert variant={'warning'}>
                    Shown only first {MAX_CHILDREN_ON_PAGE} traces on the page.
                  </Alert>
                </Row>
              )}
              <Accordion>
                {children.slice(0, MAX_CHILDREN_ON_PAGE).map((child) => (
                  <CallTraceTree key={`${child.invocationId}`} common={common} trace={child} />
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
  const [callTrace, setCallTrace] = useState<CallTrace>();
  const [minDurationFilter, setMinDurationFilter] = useState<number | null>(null);
  const [maxDurationFilter, setMaxDurationFilter] = useState<number | null>(null);
  const [statusFilter, setStatusFilter] = useState<Status>('all');
  const [typeFilter, setTypeFilter] = useState<InvocationType>();

  useEffect(() => {
    if (!contextId || !callId) {
      navigate('/calls');
      return;
    }
    const contextIdNumber = +contextId;
    const callIdNumber = +callId;

    const callTrace = rootReport.calls?.contexts[contextIdNumber].callTraces.find(it => it.invocationId === callIdNumber);
    setCallTrace(callTrace);
  }, [rootReport]);

  if (!contextId || !callId) {
    navigate('/calls');
    return;
  }

  let children = [...(callTrace?.children ?? [])];

  if (minDurationFilter !== null) {
    children = children.filter(it => (it.endTime - it.startTime) >= minDurationFilter);
  }

  if (maxDurationFilter !== null) {
    children = children.filter(it => (it.endTime - it.startTime) <= maxDurationFilter);
  }

  if (statusFilter !== 'all') {
    children = children.filter(it => (it.success ? 'success' : 'fail') === statusFilter);
  }

  if (typeFilter) {
    children = children.filter(it => rootReport.common?.stringConstants[it.type] === typeFilter);
  }

  return (
    <Container>
      <Row className={'mt-4 mb-4'}>
        <h3>
          <Button variant={'outline-secondary'} onClick={() => navigate('/calls')}>Back</Button> Call Trace Report
        </h3>
      </Row>
      <Loader loading={isLoading}>
        {callTrace && getCallTraceSystemDescription(rootReport.common?.stringConstants ?? [], callTrace)}
        {callTrace && getCallTraceDetails(rootReport.common?.stringConstants ?? [], callTrace)}
        {(callTrace?.children ?? []).length > 0 && (
          <>
            <Row className={'mb-2'}>
              <Form className="p-2 shadow bg-body-tertiary rounded">
                <Row className={'mb-2'}>
                  <h4>Children</h4>
                </Row>
                <Row className={'mb-2'}>
                  <Col xxl={6}>
                    <Row>
                      <Col xxl={2} md={12}>
                        <Form.Label htmlFor={'minDurationInput'}>Duration</Form.Label>
                      </Col>
                      <Col xxl={10} md={12}>
                        <InputGroup>
                          <InputGroup.Text><label htmlFor={'minDurationInput'}>from</label></InputGroup.Text>
                          <Form.Control
                            id={'minDurationInput'}
                            type="number"
                            min={0}
                            value={minDurationFilter === null ? '' : minDurationFilter}
                            onChange={(e) => setMinDurationFilter(e.target.value ? parseInt(e.target.value, 10) : null)}
                          />
                          <InputGroup.Text><label htmlFor={'maxDurationInput'}>to</label></InputGroup.Text>
                          <Form.Control
                            id={'maxDurationInput'}
                            type="number"
                            min={minDurationFilter || 0}
                            value={maxDurationFilter === null ? '' : maxDurationFilter}
                            onChange={(e) => setMaxDurationFilter(e.target.value ? parseInt(e.target.value, 10) : null)}
                          />
                          <InputGroup.Text>ms</InputGroup.Text>
                        </InputGroup>
                      </Col>
                    </Row>
                  </Col>
                  <Col xxl={3}>
                    <Row>
                      <Col xxl={2} md={12}>
                        <Form.Label htmlFor={'statusSelect'}>Status</Form.Label>
                      </Col>
                      <Col xxl={10} md={12}>
                        <Form.Select
                          id={'statusSelect'}
                          value={statusFilter}
                          onChange={(e) => setStatusFilter(e.target.value as Status)}
                        >
                          <option value="all">All</option>
                          <option value="success">Success</option>
                          <option value="fail">Fail</option>
                        </Form.Select>
                      </Col>
                    </Row>
                  </Col>
                  <Col xxl={3}>
                    <Row>
                      <Col xxl={2} md={12}>
                        <Form.Label htmlFor={'typeSelect'}>Type</Form.Label>
                      </Col>
                      <Col xxl={10} md={12}>
                        <Form.Select
                          id={'typeSelect'}
                          value={typeFilter}
                          onChange={(e) => setTypeFilter(e.target.value ? e.target.value as InvocationType : undefined)}
                        >
                          <option value={''}>All</option>
                          <option value="ASYNC">ASYNC</option>
                          <option value="JPA">JPA</option>
                          <option value="CONTROLLER">CONTROLLER</option>
                          <option value="SCHEDULED">SCHEDULED</option>
                          <option value="SERVICE">SERVICE</option>
                          <option value="COMPONENT">COMPONENT</option>
                        </Form.Select>
                      </Col>
                    </Row>
                  </Col>
                </Row>
              </Form>
            </Row>
            <Row>
              <Accordion>
                {children.length > MAX_CHILDREN_ON_PAGE && (
                  <Row className="p-4">
                    <Alert variant={'warning'}>
                      Shown only first {MAX_CHILDREN_ON_PAGE} traces on the page.
                    </Alert>
                  </Row>
                )}
                {children.slice(0, MAX_CHILDREN_ON_PAGE).map((child) => (
                  <CallTraceTree key={`${child.invocationId}`}
                                 common={rootReport.common ?? { stringConstants: [] }}
                                 trace={child} />
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
