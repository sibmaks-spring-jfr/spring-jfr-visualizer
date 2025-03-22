import React, { createContext, useEffect, useState } from 'react';
import { Base64 } from '@sibdevtools/frontend-common';
import { RootReport } from '../api/protobuf/common';

type RootReportContextType = {
  rootReport: RootReport;
  isLoading: boolean;
};

const defaultReport: RootReport = {
  common: {
    stringConstants: []
  },
  calls: {
    contexts: {}
  },
  connections: {
    contexts: {}
  },
  beans: {
    beans: [],
    beanDefinitions: {}
  },
};

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
      const rootReport = window.rootReport ?? '';
      const decodedBase64 = Base64.Decoder.text2array(rootReport);
      const decoded = RootReport.decode(decodedBase64);
      console.log(decoded);
      setRootReport(decoded);
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
