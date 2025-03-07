import React, { useContext, useState } from 'react';
import { Alert, Col, Container, Form, Row } from 'react-bootstrap';
import Timeline from './Timeline';
import { Loader } from '@sibdevtools/frontend-common';
import { Connection } from '../../../api/types';
import { RootReportContext } from '../../../context/RootReportProvider';

const MAX_CONNECTIONS_ON_PAGE = 25;

const ConnectionsReportPage: React.FC = () => {
  const { rootReport, isLoading } = useContext(RootReportContext);
  const [connections, setConnections] = useState<Connection[]>([]);
  const [showAlert, setShowAlert] = useState<boolean>(false);
  const [minDuration, setMinDuration] = useState<number | null>(null);
  const [maxDuration, setMaxDuration] = useState<number | null>(null);

  const handleFilterSubmit = () => {
    let filtered = [...rootReport.connections.connections];

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
                disabled={isLoading}
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
