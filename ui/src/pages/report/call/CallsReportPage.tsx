import React, { useContext, useEffect, useState } from 'react';
import { Alert, Badge, Button, Col, Container, Form, InputGroup, Row } from 'react-bootstrap';
import { toISOString } from '../../../utils/datetime';
import { useNavigate } from 'react-router-dom';
import { CustomTable, Loader, SuggestiveInput } from '@sibdevtools/frontend-common';
import { RootReportContext } from '../../../context/RootReportProvider';
import { MaterialSymbolsSearchRounded } from '../../../icons';
import { SuggestiveItem } from '@sibdevtools/frontend-common/dist/components/suggestive-input/types';
import { CallTrace } from '../../../api/protobuf/calls';

const MAX_TRACE_ON_PAGE = 25;

const getStatusRepresentation = (it: CallTrace) => {
  return (
    <Badge bg={it.success ? 'success' : 'danger'} className="ms-2">
      {it.success ? 'Success' : 'Fail'}
    </Badge>
  );
};

const CallsReportPage = () => {
  const { rootReport, isLoading } = useContext(RootReportContext);
  const navigate = useNavigate();
  const [filteredRoots, setFilteredRoots] = useState<CallTrace[]>([]);
  const [showAlert, setShowAlert] = useState<boolean>(false);

  const [context, setContext] = useState<number>(-1);
  const [contexts, setContexts] = useState<SuggestiveItem[]>([]);
  const [leftTimeBound, setLeftTimeBound] = useState<string>('');
  const [rightTimeBound, setRightTimeBound] = useState<string>('');
  const [minDuration, setMinDuration] = useState<number | null>(null);
  const [maxDuration, setMaxDuration] = useState<number | null>(null);
  const [statusFilter, setStatusFilter] = useState<'all' | 'success' | 'fail'>('all');

  useEffect(() => {
    const beansContexts = rootReport.beans?.beans.map(it => it.contextId) ?? [];
    const beanDefinitionsContexts = Object.keys(rootReport.beans?.beanDefinitions ?? {}).map(it => +it);

    const contexts = Array.from(new Set([...beansContexts, ...beanDefinitionsContexts]))
      .map(it => ({
          key: `${it}`,
          value: rootReport.common?.stringConstants[+it] ?? 'Unknown',
        }
      ));

    setContexts(contexts);
  }, [rootReport]);

  const stringConstants = rootReport.common?.stringConstants ?? [];

  const handleFilterSubmit = () => {
    if (context === -1) {
      setFilteredRoots([]);
      return;
    }
    let filtered = [...(rootReport.calls?.contexts[context]?.callTraces ?? [])];

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

    setFilteredRoots(filtered.slice(0, MAX_TRACE_ON_PAGE));
    setShowAlert(filtered.length > MAX_TRACE_ON_PAGE);
  };

  return (
    <Container fluid className="d-flex flex-column" style={{ minHeight: '100vh' }}>
      <Row className="mb-4 ms-2 me-2">
        <Form className="p-2 shadow bg-body-tertiary rounded">
          <Row className={'mb-2'}>
            <Col xxl={6}>
              <Row>
                <Col xxl={1} xs={12}>
                  <Form.Label htmlFor={'contextInput'}>Context</Form.Label>
                </Col>
                <Col xxl={11} xs={12}>
                    <SuggestiveInput
                      id={'contextInput'}
                      mode={'strict'}
                      onChange={it => setContext(it.key ? +it.key : -1)}
                      suggestions={contexts}
                      disabled={contexts.length === 0}
                      required={true}
                    />
                </Col>
              </Row>
            </Col>
            <Col xxl={6}>
              <Row>
                <Col xxl={1}>
                  <Form.Label>Time</Form.Label>
                </Col>
                <Col xxl={11}>
                  <InputGroup>
                    <InputGroup.Text><label htmlFor={'leftTimeBoundInput'}>from</label></InputGroup.Text>
                    <Form.Control
                      id={'leftTimeBoundInput'}
                      type="datetime-local"
                      value={leftTimeBound}
                      onChange={(e) => setLeftTimeBound(e.target.value)}
                    />
                    <InputGroup.Text><label htmlFor={'rightTimeBoundInput'}>to</label></InputGroup.Text>
                    <Form.Control
                      id={'rightTimeBoundInput'}
                      type="datetime-local"
                      value={rightTimeBound}
                      onChange={(e) => setRightTimeBound(e.target.value)}
                    />
                    <Button
                      variant={'primary'}
                      onClick={handleFilterSubmit}
                      disabled={isLoading}
                    >
                      <MaterialSymbolsSearchRounded />
                    </Button>
                  </InputGroup>
                </Col>
              </Row>
            </Col>
          </Row>
          <Row className={'mb-2'}>
            <Col xxl={6}>
              <Row>
                <Col xxl={1} md={12}>
                  <Form.Label htmlFor={'minDurationInput'}>Duration</Form.Label>
                </Col>
                <Col xxl={11} md={12} className={'d-none d-md-block'}>
                  <InputGroup>
                    <InputGroup.Text><label htmlFor={'minDurationInput'}>from</label></InputGroup.Text>
                    <Form.Control
                      id={'minDurationInput'}
                      type="number"
                      min={0}
                      value={minDuration === null ? '' : minDuration}
                      onChange={(e) => setMinDuration(e.target.value ? parseInt(e.target.value, 10) : null)}
                    />
                    <InputGroup.Text><label htmlFor={'maxDurationInput'}>to</label></InputGroup.Text>
                    <Form.Control
                      id={'maxDurationInput'}
                      type="number"
                      min={minDuration || 0}
                      value={maxDuration === null ? '' : maxDuration}
                      onChange={(e) => setMaxDuration(e.target.value ? parseInt(e.target.value, 10) : null)}
                    />
                    <InputGroup.Text>ms</InputGroup.Text>
                  </InputGroup>
                </Col>
                <Col xs={12} className={'d-md-none'}>
                  <Form.Label htmlFor={'minDurationXSInput'}>From</Form.Label>
                  <InputGroup>
                    <Form.Control
                      id={'minDurationXSInput'}
                      type="number"
                      min={0}
                      value={minDuration === null ? '' : minDuration}
                      onChange={(e) => setMinDuration(e.target.value ? parseInt(e.target.value, 10) : null)}
                    />
                    <InputGroup.Text>ms</InputGroup.Text>
                  </InputGroup>
                </Col>
                <Col xs={12} className={'d-md-none'}>
                  <Form.Label htmlFor={'maxDurationXSInput'}>To</Form.Label>
                  <InputGroup>
                    <Form.Control
                      id={'maxDurationXSInput'}
                      type="number"
                      min={minDuration || 0}
                      value={maxDuration === null ? '' : maxDuration}
                      onChange={(e) => setMaxDuration(e.target.value ? parseInt(e.target.value, 10) : null)}
                    />
                    <InputGroup.Text>ms</InputGroup.Text>
                  </InputGroup>
                </Col>
              </Row>
            </Col>
            <Col xxl={6}>
              <Row>
                <Col xxl={1}>
                  <Form.Label htmlFor={'statusSelect'}>Status</Form.Label>
                </Col>
                <Col xxl={11}>
                  <Form.Select
                    id={'statusSelect'}
                    value={statusFilter}
                    onChange={(e) => setStatusFilter(e.target.value as 'all' | 'success' | 'fail')}
                  >
                    <option value="all">All</option>
                    <option value="success">Success</option>
                    <option value="fail">Fail</option>
                  </Form.Select>
                </Col>
              </Row>
            </Col>
          </Row>
        </Form>
      </Row>
      <Row>
        <Loader loading={isLoading}>
          {showAlert && (
            <Row className="p-4">
              <Alert variant={'warning'}>
                Shown only first {MAX_TRACE_ON_PAGE} traces on the page.
              </Alert>
            </Row>
          )}
          <CustomTable
            table={{
              striped: true,
              hover: true,
              responsive: true
            }}
            thead={{
              className: 'table-dark',
              columns: {
                qualifier: {
                  label: 'Qualifier',
                  sortable: true,
                  filterable: true,
                  className: 'text-center'
                },
                callStartedAt: {
                  label: 'Call Started At',
                  sortable: true,
                  filterable: true,
                  className: 'text-center'
                },
                timing: {
                  label: 'Timing',
                  sortable: true,
                  filterable: true,
                  className: 'text-center'
                },
                status: {
                  label: 'Status',
                  sortable: true,
                  filterable: true,
                  className: 'text-center'
                },
              },
              defaultSort: {
                column: 'callStartedAt',
                direction: 'desc'
              }
            }}
            tbody={{
              data: filteredRoots.map(it => {
                return {
                  invocationId: it.invocationId,
                  qualifier: `${stringConstants[it.className]}#${stringConstants[it.methodName]}`,
                  callStartedAt: {
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
              }),
              rowBehavior: {
                handler: (row) => {
                  navigate(`/calls/${context}/${row.invocationId}`);
                }
              }
            }}
            loading={isLoading}
          />
        </Loader>
      </Row>
    </Container>
  );
};


export default CallsReportPage;
