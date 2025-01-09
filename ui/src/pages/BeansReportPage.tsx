import React, { useEffect, useState } from 'react';
import { Col, Container, Row } from 'react-bootstrap';
import { Bean, BeanDefinition } from '../api/types';
import BeansPage from './BeansPage';
import BeanInitializationGantChartPage from './BeanInitializationGantChartPage';
import GraphPage from './GraphPage';
import BeanDefinitionsPage from './BeanDefinitionsPage';
import BackButton from '../components/BackButton';

interface BeanReport {
  beans: Bean[];
  beanDefinitions: BeanDefinition[];
}

const BeansReportPage = () => {
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
        <h3><BackButton /> Beans Report</h3>
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
          <BeanInitializationGantChartPage beans={beans} />
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


export default BeansReportPage;
