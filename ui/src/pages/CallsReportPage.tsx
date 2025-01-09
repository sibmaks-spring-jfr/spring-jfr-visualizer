import React, { useEffect, useState } from 'react';
import { Col, Container, Row, Table } from 'react-bootstrap';
import { CallTrace } from '../api/types';
import BackButton from '../components/BackButton';

interface CallReport {
  roots: CallTrace[];
}

const CallsReportPage = () => {
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
        <h3><BackButton /> Calls Report</h3>
      </Row>
      <Row className="mb-4">
        <Col>
          <Table bordered={true}>
            <tbody>
            {
              roots.map((root) => (
                <tr>
                  <td>{root.contextId}</td>
                  <td>{root.correlationId}</td>
                  <td>{root.invocationId}</td>
                  <td>{root.success ? 'Success' : 'Failure'}</td>
                  <td>{root.type}</td>
                  <td>{root.startTime}</td>
                  <td>{root.endTime}</td>
                </tr>
              ))
            }
            </tbody>

          </Table>
        </Col>
      </Row>
    </Container>
  );
};


export default CallsReportPage;
