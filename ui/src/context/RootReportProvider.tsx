import React, { createContext, useEffect, useState } from 'react';
import { Base64 } from '@sibdevtools/frontend-common';
import { RootReport } from '../api/protobuf/common';
import pako from 'pako';

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
      const raw = (window.rootReport ?? '') as string;
      const decoded = Base64.Decoder.text2buffer(raw);
      const decompressed = pako.ungzip(decoded);
      const rootReport = RootReport.decode(decompressed);
      setRootReport(rootReport);
    } catch (error) {
      console.error('Failed to load', error);
    } finally {
      setIsLoading(false);
      // @ts-ignore
      window.rootReport = null;
    }
  }, []);

  return (
    <RootReportContext.Provider value={{ rootReport, isLoading }}>
      {children}
    </RootReportContext.Provider>
  );
};
