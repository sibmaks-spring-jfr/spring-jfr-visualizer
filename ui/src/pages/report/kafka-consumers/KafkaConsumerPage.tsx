import React, { useContext, useEffect, useState } from 'react';
import { Badge, Card, Col, Container, ListGroup, ProgressBar, Row, Table } from 'react-bootstrap';
import { useNavigate, useParams } from 'react-router-dom';
import { Loader } from '@sibdevtools/frontend-common';
import { RootReportContext } from '../../../context/RootReportProvider';
import { KafkaConsumer, KafkaConsumerPartitionEventType } from '../../../api/protobuf/kafka.consumer';
import { toISOString } from '../../../utils/datetime';

const KafkaConsumerPage = () => {
  const { rootReport, isLoading } = useContext(RootReportContext);
  const navigate = useNavigate();
  const { contextId, consumerId } = useParams();

  const [kafkaConsumer, setKafkaConsumer] = useState<KafkaConsumer>();

  useEffect(() => {
    if (!contextId || !consumerId) {
      navigate('/kafka-consumers');
      return;
    }
    if (isLoading) {
      return;
    }
    const contextIdNumber = +contextId;
    const consumerIdNumber = +consumerId;

    const consumer = rootReport.kafkaConsumers?.contexts[contextIdNumber].consumers[consumerIdNumber];
    setKafkaConsumer(consumer);
  }, [rootReport]);

  if (!contextId || !consumerId) {
    navigate('/kafka-consumers');
    return;
  }

  const stringConstants = rootReport.common?.stringConstants ?? [];

  if (!kafkaConsumer) {
    return (
      <Container fluid className="d-flex flex-column" style={{ minHeight: '100vh' }}>
        <Row>
          <Loader loading={isLoading}>
          </Loader>
        </Row>
      </Container>
    );
  }

  return (
    <Container fluid className="d-flex flex-column" style={{ minHeight: '100vh' }}>
      <Row>
        <Loader loading={isLoading}>
          <Row className={'mb-4'}>
            <Col xs={12}>
              <Card className={'shadow-sm'}>
                <Card.Header className={'bg-primary text-white'}>
                  <h5>Consumer Configuration</h5>
                </Card.Header>
                <Card.Body>
                  <Row>
                    <Col sm={3}>Brokers:</Col>
                    <Col sm={9}>{stringConstants[kafkaConsumer.bootstrapServers]}</Col>

                    <Col sm={3}>Consumer Group:</Col>
                    <Col sm={9}>{stringConstants[kafkaConsumer.consumerGroup]}</Col>

                    <Col sm={3}>Topics:</Col>
                    <Col sm={9}>
                      {kafkaConsumer.topics.map((topic) => (
                        <Badge bg="secondary" key={`topic-${topic}`}>{stringConstants[topic]}</Badge>
                      ))}
                    </Col>
                  </Row>
                </Card.Body>
              </Card>
            </Col>
          </Row>
          <Row className={'g-4 mb-4'}>
            <Col xs={6}>
              <Card className={'shadow-sm'}>
                <Card.Header className={'bg-info text-white'}>
                  <h5>Consumer Offsets</h5>
                </Card.Header>
                <Card.Body>
                  <Table variant={'sm'}>
                    <thead>
                    <tr>
                      <th>Partition</th>
                      <th>Current offset</th>
                      <th>Last commit</th>
                    </tr>
                    </thead>
                    <tbody>
                    {Object.entries(kafkaConsumer.partitions).map(([partition, offsets]) => (
                      <tr key={`partition-${partition}`}>
                        <td>{stringConstants[+partition]}</td>
                        <td>{offsets.currentOffset}</td>
                        <td>{offsets.lastCommit}</td>
                      </tr>
                    ))}
                    </tbody>
                  </Table>
                </Card.Body>
              </Card>
            </Col>
            <Col xs={6}>
              <Card className={'shadow-sm'}>
                <Card.Header className={'bg-success text-white'}>
                  <h5>Commits</h5>
                </Card.Header>
                <Card.Body>
                  <Row className="mb-3">
                    <Col xs={6}>
                      <div className="text-center">
                        <div className="fs-2">{kafkaConsumer.stats?.commits ?? 0}</div>
                        <small className="text-muted">Commits</small>
                      </div>
                    </Col>
                    <Col xs={6}>
                      <div className="text-center">
                        <div
                          className="fs-2">{(kafkaConsumer.stats?.commited ?? 0) / (kafkaConsumer.stats?.commits ?? 1)}</div>
                        <small className="text-muted">Successfully</small>
                      </div>
                    </Col>
                  </Row>
                  <ProgressBar className="mb-2"
                               min={0}
                               now={kafkaConsumer.stats?.commited ?? 0}
                               max={kafkaConsumer.stats?.commits ?? 0}
                               variant={'success'}>
                  </ProgressBar>
                  {kafkaConsumer.stats?.lastCommitAt && (
                    <small className="text-muted">Last commit
                      at: {toISOString(kafkaConsumer.stats?.lastCommitAt)}</small>
                  )
                  }
                </Card.Body>
              </Card>
            </Col>
          </Row>
          <Row className={'mb-4'}>
            <Col xs={12}>
              <Card className={'shadow-sm'}>
                <Card.Header className={'bg-warning'}>
                  <h5>Partitions Event</h5>
                </Card.Header>
                <Card.Body>
                  <ListGroup variant={'flush'}>
                    {kafkaConsumer.partitionsEvents.map((it, index) => {
                      let caption = 'Added new partitions';
                      switch (it.eventType) {
                        case KafkaConsumerPartitionEventType.LOST: {
                          caption = 'Partitions Lost';
                          break;
                        }
                        case KafkaConsumerPartitionEventType.REVOKED: {
                          caption = 'Partitions Revoked';
                          break;
                        }
                      }

                      return (
                        <ListGroup.Item key={`partition-event-${index}`} className=" d-flex justify-content-between align-items-center">
                          {toISOString(it.at)} - {caption}
                          <span className="badge bg-primary">{
                            it.partitions.map(
                              partition => stringConstants[partition]
                            ).join(', ')
                          }
                        </span>
                        </ListGroup.Item>
                      );
                    })}
                  </ListGroup>
                </Card.Body>
              </Card>
            </Col>
          </Row>
        </Loader>
      </Row>
    </Container>
  );
};


export default KafkaConsumerPage;
