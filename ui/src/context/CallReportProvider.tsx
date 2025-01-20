import React, { createContext, useState, useEffect } from 'react';
import { CallReport, CallTrace } from '../api/types';

type CallReportContextType = {
  callReport: CallReport;
  context2id2Trace: Map<string, Map<string, CallTrace>>;
  isLoading: boolean;
};

export const CallReportContext = createContext<CallReportContextType>({
  callReport: { roots: [], },
  context2id2Trace: new Map(),
  isLoading: true,
});

export const CallReportProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [callReport, setCallReport] = useState<CallReport>({
    roots: []
  });
  const [context2id2Trace, setContext2id2Trace] = useState<Map<string, Map<string, CallTrace>>>(new Map());
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const loadFile = async () => {
      try {
        // @ts-ignore
        const callsJson = window.callsJson || '{"roots":[]}';
        const report = JSON.parse(callsJson) as CallReport;
        setCallReport(report);

        const context2id2Trace = new Map<string, Map<string, CallTrace>>();
        for (let root of report.roots) {
          let id2Trace = context2id2Trace.get(root.contextId);
          if (!id2Trace) {
            id2Trace = new Map();
            context2id2Trace.set(root.contextId, id2Trace);
          }
          id2Trace.set(root.invocationId, root);
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
    <CallReportContext.Provider value={{ callReport, context2id2Trace, isLoading }}>
      {children}
    </CallReportContext.Provider>
  );
};
