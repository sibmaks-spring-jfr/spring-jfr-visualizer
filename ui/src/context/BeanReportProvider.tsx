import React, { createContext, useState, useEffect } from 'react';
import { BeanReport } from '../api/types';

type BeanReportContextType = {
  beanReport: BeanReport;
  isLoading: boolean;
};

export const BeanReportContext = createContext<BeanReportContextType>({
  beanReport: { beans: [], beanDefinitions: [] },
  isLoading: true,
});

export const BeanReportProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [beanReport, setBeanReport] = useState<BeanReport>({
    beans: [],
    beanDefinitions: []
  });
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const loadFile = async () => {
      try {
        // @ts-ignore
        const beansJson = window.beansJson || '{}';
        const beanReport = JSON.parse(beansJson) as BeanReport;
        setBeanReport(beanReport);
      } catch (error) {
        console.error('Failed to load:', error);
      } finally {
        setIsLoading(false);
      }
    };

    loadFile();
  }, []);

  return (
    <BeanReportContext.Provider value={{ beanReport, isLoading }}>
      {children}
    </BeanReportContext.Provider>
  );
};
