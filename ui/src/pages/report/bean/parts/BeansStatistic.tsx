import React from 'react';
import { Card, Row } from 'react-bootstrap';
import { toISOString } from '../../../../utils/datetime';
import { CustomTable } from '@sibdevtools/frontend-common';
import { CommonDto } from '../../../../api/protobuf/common';
import { BeanInitialized } from '../../../../api/protobuf/beans';

export interface BeansPageProps {
  common: CommonDto;
  beans: BeanInitialized[];
}

const BeansStatistic: React.FC<BeansPageProps> = ({
                                                    common,
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
                  className: 'text-center text-break'
                },
                preInitializedAt: {
                  label: 'Pre Initialized At',
                  sortable: true,
                  filterable: true,
                  className: 'text-center text-break'
                },
                postInitializedAt: {
                  label: 'Post Initialized At',
                  sortable: true,
                  filterable: true,
                  className: 'text-center text-break'
                },
                duration: {
                  label: 'Duration',
                  sortable: true,
                  filterable: true,
                  className: 'text-center text-break'
                },
              },
              defaultSort: {
                column: 'preInitializedAt',
                direction: 'asc'
              }
            }}
            tbody={{
              data: beans.map(it => {
                return {
                  beanName: {
                    representation: common.stringConstants[it.beanName],
                    value: common.stringConstants[it.beanName],
                    className: 'td-128 text-break'
                  },
                  preInitializedAt: {
                    representation: <code>{toISOString(it.preInitializedAt)}</code>,
                    value: toISOString(it.preInitializedAt),
                    className: 'td-64 text-center text-break'
                  },
                  postInitializedAt: {
                    representation: <code>{toISOString(it.postInitializedAt)}</code>,
                    value: toISOString(it.postInitializedAt),
                    className: 'td-64 text-center text-break'
                  },
                  duration: {
                    representation: <code>{it.duration}</code>,
                    value: it.duration,
                    className: 'td-64 text-center text-break'
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
