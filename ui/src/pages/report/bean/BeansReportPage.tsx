import React, { useContext, useEffect, useState } from 'react';
import { Button, Col, Container, Form, InputGroup, Row } from 'react-bootstrap';
import BeansStatistic from './parts/BeansStatistic';
import BeanInitializationGantChart from './parts/BeanInitializationGantChart';
import GraphPage from '../../GraphPage';
import BeanDefinitions from './parts/BeanDefinitions';
import { RootReportContext } from '../../../context/RootReportProvider';
import { Loader, SuggestiveInput } from '@sibdevtools/frontend-common';
import { Bean, BeanDefinition, Common } from '../../../api/types';
import { SuggestiveItem } from '@sibdevtools/frontend-common/dist/components/suggestive-input/types';
import { MaterialSymbolsSearchRounded } from '../../../icons';

const BeansReportPage = () => {
  const { rootReport, isLoading } = useContext(RootReportContext);

  const [common, setCommon] = useState<Common>({
    stringConstants: {}
  });
  const [beans, setBeans] = useState<Bean[]>([]);
  const [beanDefinitions, setBeanDefinitions] = useState<BeanDefinition[]>([]);

  const [context, setContext] = useState<number>();
  const [contexts, setContexts] = useState<SuggestiveItem[]>([]);

  useEffect(() => {
    const beansContexts = rootReport.beans.beans.map(it => it.contextId);
    const beanDefinitionsContexts = Object.keys(rootReport.beans.beanDefinitions).map(it => +it);

    const contexts = Array.from(new Set([...beansContexts, ...beanDefinitionsContexts]))
      .map(it => ({
          key: `${it}`,
          value: rootReport.common.stringConstants[+it]
        }
      ));

    setContexts(contexts);
  }, [rootReport]);

  const handleFilterSubmit = () => {
    if (!context) {
      setCommon({
        stringConstants: {}
      });
      setBeans([]);
      setBeanDefinitions([]);
      return;
    }
    const beans = rootReport.beans.beans.filter(it => it.contextId === context);
    const beanDefinitions = rootReport.beans.beanDefinitions[context] ?? [];

    setCommon(rootReport.common);
    setBeans(beans);
    setBeanDefinitions(beanDefinitions);
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
                      onChange={it => setContext(it.key ? +it.key : undefined)
                      }
                      suggestions={contexts}
                      disabled={contexts.length === 0}
                      required={true}
                    />
                    <Button
                      variant={'primary'}
                      onClick={handleFilterSubmit}
                      disabled={isLoading || !context}
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
          <Row className="mb-4">
            <Col>
              <BeanDefinitions common={common} beanDefinitions={beanDefinitions} />
            </Col>
          </Row>
          <Row className="mb-4">
            <Col>
              <BeansStatistic common={common} beans={beans} />
            </Col>
          </Row>
          <Row className="mb-4">
            <Col>
              <BeanInitializationGantChart common={common} beans={beans} />
            </Col>
          </Row>
          <Row className="mb-4">
            <Col>
              <GraphPage common={common} beanDefinitions={beanDefinitions} />
            </Col>
          </Row>
        </Loader>
      </Row>
    </Container>
  );
};


export default BeansReportPage;
