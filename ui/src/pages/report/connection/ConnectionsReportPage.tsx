import React, { useContext, useEffect, useState } from 'react';
import { Alert, Col, Container, Form, Row } from 'react-bootstrap';
import Timeline from './Timeline';
import { Loader, SuggestiveInput } from '@sibdevtools/frontend-common';
import { Connection } from '../../../api/types';
import { RootReportContext } from '../../../context/RootReportProvider';
import { SuggestiveItem } from '@sibdevtools/frontend-common/dist/components/suggestive-input/types';

const MAX_CONNECTIONS_ON_PAGE = 25;

const ConnectionsReportPage: React.FC = () => {
  const { rootReport, isLoading } = useContext(RootReportContext);
  const [connections, setConnections] = useState<Connection[]>([]);
  const [showAlert, setShowAlert] = useState<boolean>(false);

  const [context, setContext] = useState<string>('');
  const [contexts, setContexts] = useState<SuggestiveItem[]>([]);
  const [pool, setPool] = useState<string>('');
  const [pools, setPools] = useState<SuggestiveItem[]>([]);
  const [minDuration, setMinDuration] = useState<number | null>(null);
  const [maxDuration, setMaxDuration] = useState<number | null>(null);

  useEffect(() => {
    const contexts = Object.keys(rootReport?.connections?.contexts ?? {})
      .map(it => ({
          key: it,
          value: it
        }
      ));

    setContexts(contexts);
  }, [rootReport]);

  useEffect(() => {
    const pools = Object.keys(rootReport?.connections?.contexts[context] ?? {})
      .map(it => ({
          key: it,
          value: it
        }
      ));

    setPools(pools);
  }, [context]);

  const handleFilterSubmit = () => {
    if(!context || !pool) {
      return
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
      <Row className="flex-grow-1">
        <Col md={3}>
          <Form className="p-2 shadow bg-body-tertiary rounded">
            <Form.Group controlId="formContext">
              <Form.Label>Context</Form.Label>
              <SuggestiveInput
                mode={'strict'}
                onChange={it => setContext(it.value)
                }
                suggestions={contexts}
                disabled={contexts.length === 0}
                required={true}
              />
            </Form.Group>
            <Form.Group controlId="formContext">
              <Form.Label>Pool</Form.Label>
              <SuggestiveInput
                mode={'strict'}
                onChange={it => setPool(it.value)
                }
                suggestions={pools}
                disabled={context === ''}
                required={true}
              />
            </Form.Group>
            <Form.Group controlId="formMinDuration">
              <Form.Label>Minimal Duration (ms)</Form.Label>
              <Form.Control
                type="number"
                min={0}
                value={minDuration === null ? '' : minDuration}
                onChange={(e) => setMinDuration(e.target.value ? parseInt(e.target.value, 10) : null)}
              />
            </Form.Group>
            <Form.Group controlId="formMaxDuration">
              <Form.Label>Maximum Duration (ms)</Form.Label>
              <Form.Control
                type="number"
                min={minDuration || 0}
                value={maxDuration === null ? '' : maxDuration}
                onChange={(e) => setMaxDuration(e.target.value ? parseInt(e.target.value, 10) : null)}
              />
            </Form.Group>
            <Form.Group controlId="formSubmit">
              <Form.Control
                type="button"
                value="Submit"
                disabled={isLoading || !context || !pool}
                onClick={handleFilterSubmit}
                className="btn btn-primary mt-2"
              />
            </Form.Group>
          </Form>
          {showAlert && (
            <Alert variant={'warning'}>
              Shown only first {MAX_CONNECTIONS_ON_PAGE} traces on the page.
            </Alert>
          )}
        </Col>
        <Col md={9}>
          <Loader loading={isLoading}>
            {connections.length === 0 ? (
              <Alert variant={'warning'}>
                There are no connections.
              </Alert>
            ) : <Timeline connections={connections} />
            }
          </Loader>
        </Col>
      </Row>
    </Container>
  );
};

export default ConnectionsReportPage;
