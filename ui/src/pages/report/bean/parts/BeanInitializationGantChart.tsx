import React from 'react';
import GanttChartPage, { Span } from '../../../GanttChartPage';
import { CommonDto } from '../../../../api/protobuf/common';
import { BeanInitialized } from '../../../../api/protobuf/beans';

interface BeanInitializationGantChartProps {
  common: CommonDto;
  beans: BeanInitialized[];
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
