import React, { useContext, useEffect, useState } from 'react';
import { Button, Col, Container, Form, InputGroup, Row } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { CustomTable, Loader, SuggestiveInput } from '@sibdevtools/frontend-common';
import { RootReportContext } from '../../../context/RootReportProvider';
import { MaterialSymbolsSearchRounded } from '../../../icons';
import { SuggestiveItem } from '@sibdevtools/frontend-common/dist/components/suggestive-input/types';
import { KafkaConsumer } from '../../../api/protobuf/kafka.consumer';

const KafkaConsumersPage = () => {
  const { rootReport, isLoading } = useContext(RootReportContext);
  const navigate = useNavigate();
  const [filteredRoots, setFilteredRoots] = useState<KafkaConsumer[]>([]);

  const [context, setContext] = useState<number>(-1);
  const [contexts, setContexts] = useState<SuggestiveItem[]>([]);

  useEffect(() => {
    const contextConsumers = Object.keys(rootReport.kafkaConsumers?.contexts ?? {});

    const contexts = Array.from(new Set([...contextConsumers]))
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
    let filtered = rootReport.kafkaConsumers?.contexts[context]?.consumers ?? {};

    setFilteredRoots(Object.values(filtered));
  };

  return (
    <Container fluid className="d-flex flex-column" style={{ minHeight: '100vh' }}>
      <Row className="mb-4 ms-2 me-2">
        <Form className="p-2 shadow bg-body-tertiary rounded">
          <Row className={'mb-2'}>
            <Form.Group controlId="formContext">
              <Row>
                <Col xxl={2} xs={12}>
                  <Form.Label>Context</Form.Label>
                </Col>
                <Col xxl={10} xs={12}>
                  <InputGroup>
                    <SuggestiveInput
                      mode={'strict'}
                      onChange={it => setContext(it.key ? +it.key : -1)}
                      suggestions={contexts}
                      disabled={contexts.length === 0}
                      required={true}
                    />
                    <Button
                      variant={'primary'}
                      onClick={handleFilterSubmit}
                      disabled={isLoading || context === -1}
                    >
                      <MaterialSymbolsSearchRounded />
                    </Button>
                  </InputGroup>
                </Col>
              </Row>
            </Form.Group>
          </Row>
        </Form>
      </Row>
      <Row>
        <Loader loading={isLoading}>
          <CustomTable
            table={{
              striped: true,
              hover: true,
              responsive: true
            }}
            thead={{
              className: 'table-dark',
              columns: {
                consumerFactory: {
                  label: 'Consumer Factory',
                  sortable: true,
                  filterable: true,
                  className: 'text-center'
                },
                consumerGroup: {
                  label: 'Consumer Group',
                  sortable: true,
                  filterable: true,
                  className: 'text-center'
                },
                commits: {
                  label: 'Commits',
                  sortable: true,
                  filterable: true,
                  className: 'text-center'
                },
              },
              defaultSort: {
                column: 'consumerGroup',
                direction: 'desc'
              }
            }}
            tbody={{
              data: filteredRoots.map(it => {
                return {
                  consumerId: it.consumerId,
                  consumerFactory: stringConstants[it.consumerFactory],
                  consumerGroup: stringConstants[it.consumerGroup],
                  commits: `${it.stats?.commits ?? 0}`,
                };
              }),
              rowBehavior: {
                handler: (row) => {
                  navigate(`/kafka-consumers/${context}/${row.consumerId}`);
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


export default KafkaConsumersPage;
