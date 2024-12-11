import React from 'react';
import CustomTable from '../components/CustomTable';
import { Card } from 'react-bootstrap';
import { Bean } from '../api/types';

export interface BeansPageProps {
  beans: Bean[];
}

const toLocalDateTime = (millis: number | null): string => {
  if (!millis) {
    return 'Unknown';
  }
  const rawDate = new Date(millis);
  return rawDate.toLocaleDateString() + ' ' + rawDate.toLocaleTimeString();
};

const BeansPage: React.FC<BeansPageProps> = ({
                                               beans
                                             }) => {
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
        <CustomTable
          className={'card-body overflow-scroll table table-striped table-hover'}
          thead={{ className: 'table-dark' }}
          columns={[
            {
              key: 'contextId',
              label: 'Context Id'
            },
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
          data={beans.map(it => {
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
                representation: <code className="content-scroll">{toLocalDateTime(it.preInitializedAt)}</code>,
                value: toLocalDateTime(it.preInitializedAt),
                className: 'td-64 text-center'
              },
              postInitializedAt: {
                representation: <code className="content-scroll">{toLocalDateTime(it.postInitializedAt)}</code>,
                value: toLocalDateTime(it.postInitializedAt),
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
      </div>
    </Card>
  );
};


export default BeansPage;
