import React, { useState } from 'react';
import CustomTable from '../../../../components/CustomTable';
import { Button, Card, Col, FormLabel, FormSelect, InputGroup, Row } from 'react-bootstrap';
import { BeanDefinition } from '../../../../api/types';

export interface BeanDefinitionsPageProps {
  beanDefinitions: BeanDefinition[];
}

const BeanDefinitions: React.FC<BeanDefinitionsPageProps> = ({
                                                                   beanDefinitions
                                                                 }) => {
  const [contextId, setContextId] = useState<string>('');
  const [filteredBeanDefinitions, setFilteredBeanDefinitions] = useState<BeanDefinition[]>([]);
  const contextIds = [...new Set(beanDefinitions.map(item => item.contextId))];

  const handleBuild = () => {
    setFilteredBeanDefinitions(
      beanDefinitions.filter(it => contextId === '' || it.contextId === contextId)
    );
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
            className={'card-body overflow-scroll table table-striped table-hover'}
            thead={{ className: 'table-dark' }}
            columns={[
              ...(contextId === '' ? [
                {
                  key: 'contextId',
                  label: 'Context Id'
                }] : []),
              {
                key: 'scope',
                label: 'Scope'
              },
              {
                key: 'beanClassName',
                label: 'Class Name'
              },
              {
                key: 'beanName',
                label: 'Bean Name'
              },
              {
                key: 'primary',
                label: 'Primary'
              },
              {
                key: 'dependenciesCount',
                label: 'Dependencies Count'
              },
              {
                key: 'dependencies',
                label: 'Dependencies'
              },
              {
                key: 'generated',
                label: 'Generated'
              },
            ]}
            data={filteredBeanDefinitions.map(it => {
              return {
                contextId: {
                  representation: <div className="content-scroll">{it.contextId}</div>,
                  value: it.contextId,
                  className: 'td-128 text-center'
                },
                scope: {
                  representation: <div className="content-scroll">{it.scope}</div>,
                  value: it.scope,
                  className: 'td-96 text-center'
                },
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
                primary: {
                  representation: it.primary === null ? 'Unknown' : (it.primary === 'true' ? 'Yes' : 'No'),
                  value: it.primary === null ? 'Unknown' : (it.primary === 'true' ? 'Yes' : 'No'),
                  className: 'text-center'
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
                generated: {
                  representation: it.generated ? 'Yes' : 'No',
                  value: it.generated ? 'Yes' : 'No',
                  className: 'text-center'
                },
              };
            })}
            filterableColumns={[
              'contextId',
              'scope',
              'beanClassName',
              'beanName',
              'primary',
              'dependenciesCount',
              'dependencies',
              'generated'
            ]}
            sortableColumns={[
              'contextId',
              'scope',
              'beanClassName',
              'beanName',
              'primary',
              'dependenciesCount',
              'dependencies',
              'generated'
            ]}
          />
        </Row>
      </div>
    </Card>
  );
};


export default BeanDefinitions;
