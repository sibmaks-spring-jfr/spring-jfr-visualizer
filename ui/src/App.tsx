import React, { useEffect, useState } from 'react';
import { Col, Container, Row } from 'react-bootstrap';
import BeanDefinitionsPage from './pages/BeanDefinitionsPage';
import BeansPage from './pages/BeansPage';
import GanttChartPage from './pages/GanttChartPage';
import GraphPage from './pages/GraphPage';
import { Bean, BeanDefinition } from './api/types';

import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.min.js';

interface BeanReport {
  beans: Bean[];
  beanDefinitions: BeanDefinition[];
}

const App: React.FC = () => {
  const [beans, setBeans] = useState<Bean[]>([]);
  const [beanDefinitions, setBeanDefinitions] = useState<BeanDefinition[]>([]);

  useEffect(() => {
    // @ts-ignore
    const beansJson = window.beansJson || '{}';
    const beans = JSON.parse(beansJson) as BeanReport;
    setBeans(beans.beans);
    setBeanDefinitions(beans.beanDefinitions);
  }, []);

  return (
    <Container>
      <Row className={'mt-4 mb-4'}>
        <h3>Spring JavaFlightRecorder - Beans Report</h3>
      </Row>
      <Row className="mb-4">
        <Col>
          <BeanDefinitionsPage beanDefinitions={beanDefinitions} />
        </Col>
      </Row>
      <Row className="mb-4">
        <Col>
          <BeansPage beans={beans} />
        </Col>
      </Row>
      <Row className="mb-4">
        <Col>
          <GanttChartPage spans={[
            {
              id: '1',
              start: 5000,
              end: 100000,
              label: 'Bean A'
            },
            {
              id: '2',
              start: 50000,
              end: 100005,
              label: 'Bean B'
            },
            {
              id: '3',
              start: 100000,
              end: 200000,
              label: 'Bean C'
            }
          ]}/>
        </Col>
      </Row>
      <Row className="mb-4">
        <Col>
          <GraphPage beanDefinitions={beanDefinitions} />
        </Col>
      </Row>
    </Container>
  );
};

export default App;
