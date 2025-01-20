import React, { useState } from 'react';
import CustomTable from '../../../../components/CustomTable';
import { Button, Card, Col, FormLabel, FormSelect, InputGroup, Row } from 'react-bootstrap';
import { Bean } from '../../../../api/types';
import { toISOString } from '../../../../utils/datetime';

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
            className={'card-body overflow-scroll table table-striped table-hover'}
            thead={{ className: 'table-dark' }}
            columns={[
              ...(contextId === '' ? [
                {
                  key: 'contextId',
                  label: 'Context Id'
                }] : []),
              {
                key: 'beanName',
                label: 'Bean Name'
              },
              {
                key: 'preInitializedAt',
                label: 'Pre Initialized At'
              },
              {
                key: 'postInitializedAt',
                label: 'Post Initialized At'
              },
              {
                key: 'duration',
                label: 'Duration'
              },
            ]}
            data={filteredBeans.map(it => {
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
            })}
            filterableColumns={[
              'contextId',
              'beanName',
              'preInitializedAt',
              'postInitializedAt',
              'duration'
            ]}
            sortableColumns={[
              'contextId',
              'beanName',
              'preInitializedAt',
              'postInitializedAt',
              'duration'
            ]}
          />
        </Row>
      </div>
    </Card>
  );
};


export default BeansStatistic;
