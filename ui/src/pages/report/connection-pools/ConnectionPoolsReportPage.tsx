import React, { useContext, useEffect, useState } from 'react';
import { Alert, Button, Col, Container, Form, InputGroup, Row } from 'react-bootstrap';
import Timeline from './Timeline';
import { Loader, SuggestiveInput } from '@sibdevtools/frontend-common';
import { Connection } from '../../../api/types';
import { RootReportContext } from '../../../context/RootReportProvider';
import { SuggestiveItem } from '@sibdevtools/frontend-common/dist/components/suggestive-input/types';
import { MaterialSymbolsSearchRounded } from '../../../icons';

const MAX_CONNECTIONS_ON_PAGE = 25;

const ConnectionPoolsReportPage: React.FC = () => {
  const { rootReport, isLoading } = useContext(RootReportContext);
  const [connections, setConnections] = useState<Connection[]>([]);
  const [showAlert, setShowAlert] = useState<boolean>(false);

  const [context, setContext] = useState<number>(-1);
  const [contexts, setContexts] = useState<SuggestiveItem[]>([]);
  const [pool, setPool] = useState<number>(-1);
  const [pools, setPools] = useState<SuggestiveItem[]>([]);
  const [minDuration, setMinDuration] = useState<number | null>(null);
  const [maxDuration, setMaxDuration] = useState<number | null>(null);

  useEffect(() => {
    const contexts = Object.keys(rootReport?.connections?.contexts ?? {})
      .map(it => ({
          key: it,
          value: rootReport.common.stringConstants[+it]
        }
      ));

    setContexts(contexts);
  }, [rootReport]);

  useEffect(() => {
    if (context === -1) {
      return;
    }

    const pools = Object.keys(rootReport?.connections?.contexts[context] ?? {})
      .map(it => ({
          key: it,
          value: rootReport.common.stringConstants[+it]
        }
      ));

    setPools(pools);
  }, [context]);

  const handleFilterSubmit = () => {
    if (context === -1 || pool === -1) {
      return;
    }

    let filtered = [...rootReport.connections.contexts[context][pool]];

    if (minDuration !== null) {
      filtered = filtered.filter(it => it.duration >= minDuration);
    }

    if (maxDuration !== null) {
      filtered = filtered.filter(it => it.duration <= maxDuration);
    }

    setConnections(filtered.slice(0, MAX_CONNECTIONS_ON_PAGE));
    setShowAlert(filtered.length > MAX_CONNECTIONS_ON_PAGE);
  };

  return (
    <Container fluid className="d-flex flex-column" style={{ minHeight: '100vh' }}>
      <Row className="mb-4 ms-2 me-2">
        <Form className="p-2 shadow bg-body-tertiary rounded">
          <Row className={'mb-2'}>
            <Col xl={6}>
              <Form.Group controlId="formContext">
                <Row>
                  <Col xxl={2} xs={12}>
                    <Form.Label>Context</Form.Label>
                  </Col>
                  <Col xxl={10} xs={12}>
                    <SuggestiveInput
                      mode={'strict'}
                      onChange={it => setContext(it.key ? +it.key : -1)}
                      suggestions={contexts}
                      disabled={contexts.length === 0}
                      required={true}
                    />
                  </Col>
                </Row>
              </Form.Group>
            </Col>
            <Col xl={6}>
              <Form.Group controlId="formPool">
                <Row>
                  <Col xxl={2} xs={12}>
                    <Form.Label>Pool</Form.Label>
                  </Col>
                  <Col xxl={10} xs={12}>
                    <InputGroup>
                      <SuggestiveInput
                        mode={'strict'}
                        onChange={it => setPool(it.key ? +it.key : -1)}
                        suggestions={pools}
                        disabled={context === -1}
                        required={true}
                      />
                      <Button
                        variant={'primary'}
                        onClick={handleFilterSubmit}
                        disabled={isLoading || context === -1 || pool === -1}
                      >
                        <MaterialSymbolsSearchRounded />
                      </Button>
                    </InputGroup>
                  </Col>
                </Row>
              </Form.Group>
            </Col>
          </Row>
          <Row>
            <Col xxl={6}>
              <Form.Group>
                <Row>
                  <Col xxl={2} md={12}>
                    <Form.Label htmlFor={'minDurationInput'}>Duration</Form.Label>
                  </Col>
                  <Col xxl={10} md={12} className={'d-none d-md-block'}>
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
              </Form.Group>
            </Col>
          </Row>
        </Form>
      </Row>
      <Row className="mb-4">
        <Loader loading={isLoading}>
          {showAlert && (
            <Row className="p-4">
              <Alert variant={'warning'}>
                Shown only first {MAX_CONNECTIONS_ON_PAGE} traces on the page.
              </Alert>
            </Row>
          )}
          {connections.length === 0 ? (
            <Row className="p-4">
              <Alert variant={'warning'}>
                There are no connections.
              </Alert>
            </Row>
          ) : <Timeline common={rootReport.common} connections={connections} />
          }
        </Loader>
      </Row>
    </Container>
  );
};

export default ConnectionPoolsReportPage;
