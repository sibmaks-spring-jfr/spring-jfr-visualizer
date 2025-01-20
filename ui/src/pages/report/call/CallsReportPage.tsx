import React, { useContext, useState } from 'react';
import { Alert, Badge, Col, Container, Form, Row } from 'react-bootstrap';
import { CallTrace } from '../../../api/types';
import BackButton from '../../../components/BackButton';
import CustomTable from '../../../components/CustomTable';
import { toISOString } from '../../../utils/datetime';
import { Loader } from '../../../components/Loader';
import { CallReportContext } from '../../../context/CallReportProvider';
import { useNavigate } from 'react-router-dom';

const MAX_TRACE_ON_PAGE = 25;

const getStatusRepresentation = (it: CallTrace) => {
  return (
    <Badge bg={it.success ? 'success' : 'danger'} className="ms-2">
      {it.success ? 'Success' : 'Fail'}
    </Badge>
  );
};

const CallsReportPage = () => {
  const { callReport, isLoading } = useContext(CallReportContext);
  const navigate = useNavigate();
  const [filteredRoots, setFilteredRoots] = useState<CallTrace[]>([...callReport.roots]);
  const [leftTimeBound, setLeftTimeBound] = useState<string>('');
  const [rightTimeBound, setRightTimeBound] = useState<string>('');
  const [minDuration, setMinDuration] = useState<number | null>(null);
  const [maxDuration, setMaxDuration] = useState<number | null>(null);
  const [statusFilter, setStatusFilter] = useState<'all' | 'success' | 'fail'>('all');

  if (isLoading) {
    return (
      <Loader />
    );
  }

  const handleFilterSubmit = () => {
    let filtered = [...callReport.roots];

    if (leftTimeBound) {
      const leftBound = new Date(leftTimeBound).getTime();
      filtered = filtered.filter(it => it.startTime >= leftBound);
    }

    if (rightTimeBound) {
      const rightBound = new Date(rightTimeBound).getTime();
      filtered = filtered.filter(it => it.startTime <= rightBound);
    }

    if (minDuration !== null) {
      filtered = filtered.filter(it => (it.endTime - it.startTime) >= minDuration);
    }

    if (maxDuration !== null) {
      filtered = filtered.filter(it => (it.endTime - it.startTime) <= maxDuration);
    }

    if (statusFilter !== 'all') {
      filtered = filtered.filter(it => (it.success ? 'success' : 'fail') === statusFilter);
    }

    setFilteredRoots(filtered);
  };

  return (
    <Container fluid className="d-flex flex-column" style={{ minHeight: '100vh' }}>
      <Row className={'mt-4 mb-4'}>
        <h3><BackButton /> Calls Report</h3>
      </Row>
      {(filteredRoots.length > MAX_TRACE_ON_PAGE) && (
        <Alert variant={'warning'}>
          Shown only first {MAX_TRACE_ON_PAGE} traces on the page.
        </Alert>
      )}
      <Row className="flex-grow-1">
        <Col md={3}>
          <Form className="p-2 shadow bg-body-tertiary rounded">
            <Form.Group controlId="formLeftTimeBound">
              <Form.Label>Left Time Bound</Form.Label>
              <Form.Control
                type="datetime-local"
                value={leftTimeBound}
                onChange={(e) => setLeftTimeBound(e.target.value)}
              />
            </Form.Group>
            <Form.Group controlId="formRightTimeBound">
              <Form.Label>Right Time Bound</Form.Label>
              <Form.Control
                type="datetime-local"
                value={rightTimeBound}
                onChange={(e) => setRightTimeBound(e.target.value)}
              />
            </Form.Group>
            <Form.Group controlId="formMinDuration">
              <Form.Label>Minimal Duration (ms)</Form.Label>
              <Form.Control
                type="number"
                value={minDuration === null ? '' : minDuration}
                onChange={(e) => setMinDuration(e.target.value ? parseInt(e.target.value, 10) : null)}
              />
            </Form.Group>
            <Form.Group controlId="formMaxDuration">
              <Form.Label>Maximum Duration (ms)</Form.Label>
              <Form.Control
                type="number"
                value={maxDuration === null ? '' : maxDuration}
                onChange={(e) => setMaxDuration(e.target.value ? parseInt(e.target.value, 10) : null)}
              />
            </Form.Group>
            <Form.Group controlId="formStatusFilter">
              <Form.Label>Status</Form.Label>
              <Form.Select
                value={statusFilter}
                onChange={(e) => setStatusFilter(e.target.value as 'all' | 'success' | 'fail')}
              >
                <option value="all">All</option>
                <option value="success">Success</option>
                <option value="fail">Fail</option>
              </Form.Select>
            </Form.Group>
            <Form.Group controlId="formSubmit">
              <Form.Control
                type="button"
                value="Submit"
                onClick={handleFilterSubmit}
                className="btn btn-primary mt-2"
              />
            </Form.Group>
          </Form>
        </Col>
        <Col md={9} className="d-flex flex-column">
          <CustomTable
            className={'overflow-scroll table-striped table-hover'}
            thead={{ className: 'table-dark' }}
            columns={[
              {
                key: 'qualifier',
                label: 'Qualifier'
              },
              {
                key: 'call_started_at',
                label: 'Call Started At'
              },
              {
                key: 'timing',
                label: 'Timing'
              },
              {
                key: 'status',
                label: 'Status'
              },
            ]}
            data={filteredRoots.slice(0, MAX_TRACE_ON_PAGE).map(it => {
              return {
                contextId: it.contextId,
                invocationId: it.invocationId,
                qualifier: `${it.className}#${it.methodName}`,
                call_started_at: {
                  representation: <code>{toISOString(it.startTime)}</code>,
                  value: toISOString(it.startTime),
                  className: 'text-center text-nowrap',
                },
                timing: {
                  representation: `${it.endTime - it.startTime} ms`,
                  value: it.endTime - it.startTime,
                  className: 'text-center',
                },
                status: {
                  representation: getStatusRepresentation(it),
                  value: it.success ? 'Success' : 'Fail',
                  className: 'text-center',
                },
              };
            })}
            filterableColumns={[
              'qualifier',
              'call_started_at',
              'timing',
              'status',
            ]}
            sortableColumns={[
              'qualifier',
              'call_started_at',
              'timing',
              'status',
            ]}
            onRowClick={(row) => {
              navigate(`/calls/${row.contextId}/${row.invocationId}`);
            }}
          />
        </Col>
      </Row>
    </Container>
  );
};


export default CallsReportPage;
