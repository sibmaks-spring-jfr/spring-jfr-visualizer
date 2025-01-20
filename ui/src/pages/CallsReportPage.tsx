import React, { useEffect, useState } from 'react';
import { Alert, Badge, Col, Container, Form, Row, Spinner } from 'react-bootstrap';
import { CallTrace } from '../api/types';
import BackButton from '../components/BackButton';
import CustomTable from '../components/CustomTable';
import { toLocalDateTime } from '../utils/datetime';

interface CallReport {
  roots: CallTrace[];
}

const MAX_TRACE_ON_PAGE = 25;

const getStatusRepresentation = (it: CallTrace) => {
  return (
    <Badge bg={it.success ? 'success' : 'danger'} className="ms-2">
      {it.success ? 'Success' : 'Fail'}
    </Badge>
  );
};

const CallsReportPage = () => {
  const [roots, setRoots] = useState<CallTrace[]>([]);
  const [loaded, setLoaded] = useState(false);
  const [filteredRoots, setFilteredRoots] = useState<CallTrace[]>([]);
  const [leftTimeBound, setLeftTimeBound] = useState<string>('');
  const [rightTimeBound, setRightTimeBound] = useState<string>('');
  const [minDuration, setMinDuration] = useState<number | null>(null);
  const [maxDuration, setMaxDuration] = useState<number | null>(null);
  const [statusFilter, setStatusFilter] = useState<'all' | 'success' | 'fail'>('all');

  useEffect(() => {
    if (loaded) {
      return;
    }
    // @ts-ignore
    const callsJson = window.callsJson || '{"roots":[]}';
    const report = JSON.parse(callsJson) as CallReport;
    setRoots(report.roots);
    setLoaded(true);
    setFilteredRoots([...report.roots]);
  }, []);

  const handleFilterSubmit = () => {
    let filtered = [...roots];

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
      <Row>
        <Col md={3}>
          {!loaded && (
            <div className="d-flex justify-content-center align-items-center" style={{ height: '100vh' }}>
              <Spinner animation="border" role="status">
                <span className="visually-hidden">Loading...</span>
              </Spinner>
            </div>
          )}
          {loaded && (
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
          )
          }
        </Col>
        <Col md={9} className="d-flex flex-column">
          <CustomTable
            className={'table table-striped table-hover flex-grow-1'}
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
                qualifier: `${it.className}#${it.methodName}`,
                call_started_at: {
                  representation: <code>{toLocalDateTime(it.startTime)}</code>,
                  value: toLocalDateTime(it.startTime),
                  className: 'text-center',
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
          />
        </Col>
      </Row>
    </Container>
  );
};


export default CallsReportPage;
