import React from 'react';
import { Col, Container, Row } from 'react-bootstrap';
import BeanDefinitionsPage from './pages/BeanDefinitionsPage';
import BeansPage from './pages/BeansPage';
import GraphPage from './pages/GraphPage';


const App: React.FC = () => {
  return (
    <Container>
      <Row className={'mt-4 mb-4'}>
        <h3>Spring JavaFlightRecorder - Beans Report</h3>
      </Row>
      <Row className="mb-4">
        <Col>
          <BeanDefinitionsPage beanDefinitions={[]} />
        </Col>
      </Row>
      <Row className="mb-4">
        <Col>
          <BeansPage beans={[]} />
        </Col>
      </Row>
      <Row className="mb-4">
        <Col>
          <GraphPage beanDependencies={{
            "key": ["test"]
          }} />
        </Col>
      </Row>
    </Container>
  );
};

export default App;
