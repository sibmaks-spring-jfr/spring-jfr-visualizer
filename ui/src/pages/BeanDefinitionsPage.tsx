import React from 'react';
import CustomTable from '../components/CustomTable';
import { Card } from 'react-bootstrap';
import { BeanDefinition } from '../api/types';

export interface BeanDefinitionsPageProps {
  beanDefinitions: BeanDefinition[];
}

const BeanDefinitionsPage: React.FC<BeanDefinitionsPageProps> = ({
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
      <div id="beanDefinitionsCollapse" className="table-responsive collapse">
        <CustomTable
          className={'card-body overflow-scroll table table-striped table-hover'}
          thead={{ className: 'table-dark' }}
          columns={[
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
              key: 'dependencies_count',
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
          data={beanDefinitions.map(it => {
            return {
              scope: {
                representation: <div className="content-scroll">{it.scope}</div>,
                value: it.scope,
                className: 'td-96'
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
              primary: it.primary === null ? 'Unknown' : (it.primary === 'true' ? 'Yes' : 'No'),
              dependencies_count: {
                representation: <code>{it.dependencies?.length ?? 0}</code>,
                value: it.dependencies?.length ?? 0,
                className: 'td-32'
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
              generated: it.generated ? 'Yes' : 'No',
            };
          })}
          filterableColumns={[
            'scope',
            'beanClassName',
            'beanName',
            'primary',
            'dependencies_count',
            'dependencies',
            'generated'
          ]}
          sortableColumns={[
            'scope',
            'beanClassName',
            'beanName',
            'primary',
            'dependencies_count',
            'dependencies',
            'generated'
          ]}
        />
      </div>
    </Card>
  );
};


export default BeanDefinitionsPage;
