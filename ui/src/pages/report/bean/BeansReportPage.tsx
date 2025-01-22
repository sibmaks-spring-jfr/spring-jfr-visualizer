import React, { useContext } from 'react';
import { Col, Container, Row } from 'react-bootstrap';
import BeansStatistic from './parts/BeansStatistic';
import BeanInitializationGantChart from './parts/BeanInitializationGantChart';
import GraphPage from '../../GraphPage';
import BeanDefinitions from './parts/BeanDefinitions';
import BackButton from '../../../components/BackButton';
import { BeanReportContext } from '../../../context/BeanReportProvider';
import { Loader } from '../../../components/Loader';

const BeansReportPage = () => {
  const { beanReport, isLoading } = useContext(BeanReportContext);

  if (isLoading) {
    return <Loader />;
  }

  return (
    <Container>
      <Row className={'mt-4 mb-4'}>
        <h3><BackButton /> Beans Report</h3>
      </Row>
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
    </Container>
  );
};


export default BeansReportPage;
