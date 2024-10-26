import React from 'react';
import CustomTable from '../components/CustomTable';
import { Card } from 'react-bootstrap';
import { Bean } from '../api/types';

export interface BeansPageProps {
  beans: Bean[];
}

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
              key: 'beanName',
              label: 'Bean Name'
            },
            {
              key: 'duration',
              label: 'Duration'
            },
          ]}
          data={beans.map(it => {
            return {
              beanName: it.beanName,
              duration: it.duration,
            };
          })}
          filterableColumns={['beanName', 'duration']}
          sortableColumns={['beanName', 'duration']}
        />
      </div>
    </Card>
  );
};


export default BeansPage;
