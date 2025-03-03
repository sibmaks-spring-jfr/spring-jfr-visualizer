import React, { useState } from 'react';
import { Button, Card, Col, FormLabel, FormSelect, InputGroup, Row } from 'react-bootstrap';
import { BeanDefinition } from '../../../../api/types';
import { CustomTable } from '@sibdevtools/frontend-common';
import { CustomTableParts } from '@sibdevtools/frontend-common/dist/components/custom-table/types';

interface BeanDefinitionDetailsProps {
  row: CustomTableParts.Row;
}

const BeanDefinitionDetails: React.FC<BeanDefinitionDetailsProps> = ({ row }) => {
  return (
    <Row>
      <Row>
        <Col className={'fw-bold'} md={3}>Scope:</Col>
        <Col md={9}>{typeof row.scope === 'string' ? row.scope : ''}</Col>
      </Row>
      <Row>
        <Col className={'fw-bold'} md={3}>Primary:</Col>
        <Col md={9}>{typeof row.primary === 'string' ? row.primary : ''}</Col>
      </Row>
      <Row>
        <Col className={'fw-bold'} md={3}>Generated:</Col>
        <Col md={9}>{typeof row.generated === 'string' ? row.generated : ''}</Col>
      </Row>
    </Row>
  );
};

export interface BeanDefinitionsPageProps {
  contextBeanDefinitions: Record<string, BeanDefinition[]>;
}

const BeanDefinitions: React.FC<BeanDefinitionsPageProps> = ({
                                                               contextBeanDefinitions
                                                             }) => {
  const [contextId, setContextId] = useState<string>('');
  const [filteredBeanDefinitions, setFilteredBeanDefinitions] = useState<BeanDefinition[]>([]);
  const contextIds = Object.keys(contextBeanDefinitions);

  const handleBuild = () => {
    setFilteredBeanDefinitions(contextBeanDefinitions[contextId] || []);
  };

  return (
    <Card>
      <Card.Header
        data-bs-toggle="collapse"
        data-bs-target="#beanDefinitionsCollapse"
        aria-expanded="false"
        aria-controls="beanDefinitionsCollapse"
        role={'button'}>
        <Card.Title className="h4">Bean Definitions</Card.Title>
      </Card.Header>
      <div id="beanDefinitionsCollapse" className="table-responsive collapse">
        <Row className="align-items-center m-2">
          <Col md={'auto'}>
            <FormLabel htmlFor={'beanName'}>Context</FormLabel>
          </Col>
          <Col md={'auto'}>
            <InputGroup>
              <FormSelect
                id={'beanDefinitionContextId'}
                value={contextId}
                onChange={e => setContextId(e.target.value)}
              >
                <option value={''}>*</option>
                {
                  contextIds
                    .map(it => <option key={it} value={it}>{it}</option>)
                }
              </FormSelect>
              <Button variant="outline-secondary" onClick={handleBuild}>
                Build Table
              </Button>
            </InputGroup>
          </Col>
        </Row>
        <Row className={'m-2 h-100'}>
          <CustomTable
            table={{
              className: 'card-body',
              striped: true,
              hover: true,
              responsive: true
            }}
            thead={{
              className: 'table-dark',
              columns: {
                beanClassName: {
                  label: 'Class Name',
                  sortable: true,
                  filterable: true,
                  className: 'text-center'
                },
                beanName: {
                  label: 'Bean Name',
                  sortable: true,
                  filterable: true,
                  className: 'text-center'
                },
                dependenciesCount: {
                  label: 'Dependencies Count',
                  sortable: true,
                  filterable: true,
                  className: 'text-center text-nowrap'
                },
                dependencies: {
                  label: 'Dependencies',
                  sortable: true,
                  filterable: true,
                  className: 'text-center'
                },
              }
            }}
            tbody={{
              data: filteredBeanDefinitions.map(it => {
                return {
                  scope: it.scope,
                  primary: it.primary === null ? 'Unknown' : (it.primary === 'true' ? 'Yes' : 'No'),
                  generated: it.generated ? 'Yes' : 'No',
                  beanClassName: {
                    representation: <div className="content-scroll">{it.beanClassName}</div>,
                    value: it.beanClassName,
                    className: 'td-512'
                  },
                  beanName: {
                    representation: <div className="content-scroll">{it.beanName}</div>,
                    value: it.beanName,
                    className: 'td-512'
                  },
                  dependenciesCount: {
                    representation: <code>{it.dependencies?.length ?? 0}</code>,
                    value: it.dependencies?.length ?? 0,
                    className: 'td-32 text-center'
                  },
                  dependencies: {
                    representation: <ul className="content-scroll">
                      {it.dependencies?.map(it => {
                        return (<li key={it}>{it}</li>);
                      })}
                    </ul>,
                    value: it.dependencies?.join(', '),
                    className: 'td-1024'
                  },
                };
              }),
              rowBehavior: {
                expandableContent: (row) => <BeanDefinitionDetails row={row} />
              }
            }}
          />
        </Row>
      </div>
    </Card>
  );
};


export default BeanDefinitions;
