import React from 'react';
import { Bean } from '../../../../api/types';
import GanttChartPage from '../../../GanttChartPage';

interface BeanInitializationGantChartProps {
  beans: Bean[];
}


const BeanInitializationGantChart: React.FC<BeanInitializationGantChartProps> = ({ beans }) => {
  const spans = beans
    .filter(it => it.preInitializedAt)
    .map(bean => {
      return {
        id: bean.beanName,
        start: bean.preInitializedAt || 0,
        end: bean.postInitializedAt,
        label: bean.beanName,
      };
    });
  return <GanttChartPage spans={spans} />;
};

export default BeanInitializationGantChart;
