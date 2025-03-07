import React from 'react';
import { Card, Row } from 'react-bootstrap';
import { Bean } from '../../../../api/types';
import { toISOString } from '../../../../utils/datetime';
import { CustomTable } from '@sibdevtools/frontend-common';

export interface BeansPageProps {
  beans: Bean[];
}

const BeansStatistic: React.FC<BeansPageProps> = ({
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
                beanName: {
                  label: 'Bean Name',
                  sortable: true,
                  filterable: true,
                  className: 'text-break'
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
              data: beans.map(it => {
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
