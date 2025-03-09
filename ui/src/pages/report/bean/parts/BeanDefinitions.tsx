import React from 'react';
import { Card, Col, Row } from 'react-bootstrap';
import { BeanDefinition, Common } from '../../../../api/types';
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
  common: Common;
  beanDefinitions: BeanDefinition[];
}

const BeanDefinitions: React.FC<BeanDefinitionsPageProps> = ({
                                                               common,
                                                               beanDefinitions
                                                             }) => {
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
      <div id="beanDefinitionsCollapse" className="collapse">
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
                  className: 'text-center text-break'
                },
                beanName: {
                  label: 'Bean Name',
                  sortable: true,
                  filterable: true,
                  className: 'text-center text-break'
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
                  className: 'text-center text-break'
                },
              },
              defaultSort: {
                column: 'beanName',
                direction: 'asc'
              }
            }}
            tbody={{
              data: beanDefinitions.map(it => {
                return {
                  scope: it.scope ? common.stringConstants[it.scope] : '',
                  primary: it.primary === null ? 'Unknown' : (common.stringConstants[it.primary] === 'true' ? 'Yes' : 'No'),
                  generated: it.generated ? 'Yes' : 'No',
                  beanClassName: {
                    representation: <div className="text-break">{it.beanClassName ? common.stringConstants[it.beanClassName] : ''}</div>,
                    value: it.beanClassName ? common.stringConstants[it.beanClassName] : '',
                    className: 'td-512'
                  },
                  beanName: {
                    representation: <div className="text-break">{common.stringConstants[it.beanName]}</div>,
                    value: common.stringConstants[it.beanName],
                    className: 'td-512'
                  },
                  dependenciesCount: {
                    representation: <code>{it.dependencies?.length ?? 0}</code>,
                    value: it.dependencies?.length ?? 0,
                    className: 'td-32 text-center'
                  },
                  dependencies: {
                    representation: <ul className="text-break">
                      {it.dependencies?.map(it => {
                        return (<li key={it}>{common.stringConstants[it]}</li>);
                      })}
                    </ul>,
                    value: it.dependencies?.map(it => common.stringConstants[it])?.join(', '),
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
