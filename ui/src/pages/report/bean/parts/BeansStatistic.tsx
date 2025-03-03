import React, { useState } from 'react';
import { Button, Card, Col, FormLabel, FormSelect, InputGroup, Row } from 'react-bootstrap';
import { Bean } from '../../../../api/types';
import { toISOString } from '../../../../utils/datetime';
import { CustomTable } from '@sibdevtools/frontend-common';

export interface BeansPageProps {
  beans: Bean[];
}

const BeansStatistic: React.FC<BeansPageProps> = ({
                                                    beans
                                                  }) => {


  const [contextId, setContextId] = useState<string>('');
  const [filteredBeans, setFilteredBeans] = useState<Bean[]>([]);
  const contextIds = [...new Set(beans.map(item => item.contextId))];

  const handleBuild = () => {
    setFilteredBeans(
      beans.filter(it => contextId === '' || it.contextId === contextId)
    );
  };

  return (
    <Card>
      <Card.Header
        data-bs-toggle="collapse"
        data-bs-target="#beansCollapse"
        aria-expanded="false"
        aria-controls="beansCollapse"
        role={'button'}>
        <Card.Title className="h4">Beans</Card.Title>
      </Card.Header>
      <div id="beansCollapse" className="table-responsive collapse">
        <Row className="align-items-center m-2">
          <Col md={'auto'}>
            <FormLabel htmlFor={'beanName'}>Context</FormLabel>
          </Col>
          <Col md={'auto'}>
            <InputGroup>
              <FormSelect
                id={'beansStatisticContextId'}
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
                ...(contextId === '') ? {
                  contextId: {
                    label: 'Context Id',
                    sortable: true,
                    filterable: true,
                    className: 'text-center'
                  }
                } : {},
                beanName: {
                  label: 'Bean Name',
                  sortable: true,
                  filterable: true,
                  className: 'text-center'
                },
                preInitializedAt: {
                  label: 'Pre Initialized At',
                  sortable: true,
                  filterable: true,
                  className: 'text-center'
                },
                postInitializedAt: {
                  label: 'Post Initialized At',
                  sortable: true,
                  filterable: true,
                  className: 'text-center'
                },
                duration: {
                  label: 'Duration',
                  sortable: true,
                  filterable: true,
                  className: 'text-center'
                },
              }
            }}
            tbody={{
              data: filteredBeans.map(it => {
                return {
                  contextId: {
                    representation: <div className="content-scroll">{it.contextId}</div>,
                    value: it.contextId,
                    className: 'td-128'
                  },
                  beanName: {
                    representation: <div className="content-scroll">{it.beanName}</div>,
                    value: it.beanName,
                    className: 'td-128'
                  },
                  preInitializedAt: {
                    representation: <code className="content-scroll">{toISOString(it.preInitializedAt)}</code>,
                    value: toISOString(it.preInitializedAt),
                    className: 'td-64 text-center'
                  },
                  postInitializedAt: {
                    representation: <code className="content-scroll">{toISOString(it.postInitializedAt)}</code>,
                    value: toISOString(it.postInitializedAt),
                    className: 'td-64 text-center'
                  },
                  duration: {
                    representation: <code className="content-scroll">{it.duration}</code>,
                    value: it.duration,
                    className: 'td-64 text-center'
                  },
                };
              })
            }}
          />
        </Row>
      </div>
    </Card>
  );
};


export default BeansStatistic;
