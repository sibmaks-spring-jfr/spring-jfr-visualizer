import React from 'react';
import { Bean, Common } from '../../../../api/types';
import GanttChartPage, { Span } from '../../../GanttChartPage';

interface BeanInitializationGantChartProps {
  common: Common;
  beans: Bean[];
}


const BeanInitializationGantChart: React.FC<BeanInitializationGantChartProps> = ({
                                                                                   common,
                                                                                   beans
                                                                                 }) => {
  const spans = beans
    .filter(it => it.preInitializedAt)
    .map(bean => {
      return {
        id: common.stringConstants[bean.beanName],
        start: bean.preInitializedAt || 0,
        end: bean.postInitializedAt,
        label: common.stringConstants[bean.beanName],
      } as Span;
    });
  return <GanttChartPage spans={spans} />;
};

export default BeanInitializationGantChart;
