import React, { createContext, useState, useEffect } from 'react';
import { RootReport } from '../api/types';

type RootReportContextType = {
  rootReport: RootReport;
  isLoading: boolean;
};

const defaultReport: RootReport = {
  common: {
    stringConstants: {}
  },
  calls: {
    roots: []
  },
  connections: {
    contexts: {}
  },
  beans: {
    beans: [],
    beanDefinitions: {}
  },
}

export const RootReportContext = createContext<RootReportContextType>({
  rootReport: defaultReport,
  isLoading: true,
});

export const RootReportProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [rootReport, setRootReport] = useState<RootReport>(defaultReport);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    try {
      // @ts-ignore
      const rootReport = window.rootReport || {};
      setRootReport(rootReport);
    } catch (error) {
      console.error('Failed to load:', error);
    } finally {
      setIsLoading(false);
    }
  }, []);

  return (
    <RootReportContext.Provider value={{ rootReport, isLoading }}>
      {children}
    </RootReportContext.Provider>
  );
};
