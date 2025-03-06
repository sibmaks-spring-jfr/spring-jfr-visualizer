import React, { createContext, useEffect, useState } from 'react';
import { ConnectionReport } from '../api/types';

type ConnectionReportContextType = {
  report: ConnectionReport;
  isLoading: boolean;
};

export const ConnectionReportContext = createContext<ConnectionReportContextType>({
  report: { connections: [], },
  isLoading: true,
});

export const ConnectionReportProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [report, setReport] = useState<ConnectionReport>({
    connections: []
  });
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const loadFile = async () => {
      try {
        // @ts-ignore
        const connectionsJson = window.connectionsJson || '{"connections":[]}';
        const report = JSON.parse(connectionsJson) as ConnectionReport;
        if(report) {
          setReport(report);
        }
      } catch (error) {
        console.error('Failed to load:', error);
      } finally {
        setIsLoading(false);
      }
    };

    loadFile();
  }, []);

  return (
    <ConnectionReportContext.Provider value={{ report, isLoading }}>
      {children}
    </ConnectionReportContext.Provider>
  );
};
