import React, { useContext, useEffect, useState } from 'react';
import { Col, Container, Form, Row } from 'react-bootstrap';
import BeansStatistic from './parts/BeansStatistic';
import BeanInitializationGantChart from './parts/BeanInitializationGantChart';
import GraphPage from '../../GraphPage';
import BeanDefinitions from './parts/BeanDefinitions';
import { RootReportContext } from '../../../context/RootReportProvider';
import { Loader, SuggestiveInput } from '@sibdevtools/frontend-common';
import { Bean, BeanDefinition } from '../../../api/types';
import { SuggestiveItem } from '@sibdevtools/frontend-common/dist/components/suggestive-input/types';

const BeansReportPage = () => {
  const { rootReport, isLoading } = useContext(RootReportContext);

  const [beans, setBeans] = useState<Bean[]>([]);
  const [beanDefinitions, setBeanDefinitions] = useState<BeanDefinition[]>([]);

  const [context, setContext] = useState<string>('');
  const [contexts, setContexts] = useState<SuggestiveItem[]>([]);

  useEffect(() => {
    const beansContexts = rootReport.beans.beans.map(it => it.contextId);
    const beanDefinitionsContexts = Object.keys(rootReport.beans.beanDefinitions);

    const contexts = Array.from(new Set([...beansContexts, ...beanDefinitionsContexts]))
      .map(it => ({
          key: it,
          value: it
        }
      ));

    setContexts(contexts);
  }, [rootReport]);

  const handleFilterSubmit = () => {
    if (!context) {
      setBeans([]);
      setBeanDefinitions([]);
      return;
    }
    const beans = rootReport.beans.beans.filter(it => it.contextId === context);
    const beanDefinitions = rootReport.beans.beanDefinitions[context] ?? [];

    setBeans(beans);
    setBeanDefinitions(beanDefinitions);
  };

  return (
    <Container fluid className="d-flex flex-column" style={{ minHeight: '100vh' }}>
      <Row className="flex-grow-1">
        <Col md={3}>
          <Form className="p-2 shadow bg-body-tertiary rounded">
            <Form.Group controlId="formRightTimeBound">
              <Form.Label>Context</Form.Label>
              <SuggestiveInput
                mode={'strict'}
                onChange={it => setContext(it.value)
                }
                suggestions={contexts}
                required={true}
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
        </Col>
        <Col md={9} className="d-flex flex-column">
          <Loader loading={isLoading}>
            <Row className="mb-4">
              <Col>
                <BeanDefinitions beanDefinitions={beanDefinitions} />
              </Col>
            </Row>
            <Row className="mb-4">
              <Col>
                <BeansStatistic beans={beans} />
              </Col>
            </Row>
            <Row className="mb-4">
              <Col>
                <BeanInitializationGantChart beans={beans} />
              </Col>
            </Row>
            <Row className="mb-4">
              <Col>
                <GraphPage beanDefinitions={beanDefinitions} />
              </Col>
            </Row>
          </Loader>
        </Col>
      </Row>
    </Container>
  );
};


export default BeansReportPage;
