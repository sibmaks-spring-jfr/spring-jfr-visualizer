import React from 'react';
import { Col, Container, Row } from 'react-bootstrap';
import BeanDefinitionsPage from './pages/BeanDefinitionsPage';
import BeansPage from './pages/BeansPage';
import GraphPage from './pages/GraphPage';
import { BeanDefinition } from './api/types';

import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.min.js';

const App: React.FC = () => {
  const beanDefinitions: BeanDefinition[] = [
    {
      scope: 'singleton',
      beanClassName: 'demo.class',
      beanName: 'bean',
      primary: 'true',
      dependencies: [
        'depend'
      ],
      generated: false
    },
    {
      scope: null,
      beanClassName: 'depend.class',
      beanName: 'depend',
      primary: null,
      dependencies: null,
      generated: true
    }
  ];

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
          <BeansPage beans={[]} />
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
