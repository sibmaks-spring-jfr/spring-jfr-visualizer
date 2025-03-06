import React, { useContext } from 'react';
import { Col, Container, Row } from 'react-bootstrap';
import BeansStatistic from './parts/BeansStatistic';
import BeanInitializationGantChart from './parts/BeanInitializationGantChart';
import GraphPage from '../../GraphPage';
import BeanDefinitions from './parts/BeanDefinitions';
import { BeanReportContext } from '../../../context/BeanReportProvider';
import { Loader } from '@sibdevtools/frontend-common';

const BeansReportPage = () => {
  const { beanReport, isLoading } = useContext(BeanReportContext);

  return (
    <Container>
      <Loader loading={isLoading}>
        <Row className="mb-4">
          <Col>
            <BeanDefinitions contextBeanDefinitions={beanReport.beanDefinitions} />
          </Col>
        </Row>
        <Row className="mb-4">
          <Col>
            <BeansStatistic beans={beanReport.beans} />
          </Col>
        </Row>
        <Row className="mb-4">
          <Col>
            <BeanInitializationGantChart beans={beanReport.beans} />
          </Col>
        </Row>
        <Row className="mb-4">
          <Col>
            <GraphPage contextBeanDefinitions={beanReport.beanDefinitions} />
          </Col>
        </Row>
      </Loader>
    </Container>
  );
};


export default BeansReportPage;
