import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.min.js';
import BeansReportPage from './pages/report/bean/BeansReportPage';
import { HashRouter, Route, Routes } from 'react-router-dom';
import ApplicationLayout from './components/ApplicationLayout';
import CallReportPage from './pages/report/call/CallReportPage';
import { BeanReportProvider } from './context/BeanReportProvider';
import { CallReportProvider } from './context/CallReportProvider';
import ConnectionsReportPage from './pages/report/connection/ConnectionsReportPage';
import CallsReportPage from './pages/report/call/CallsReportPage';
import { ConnectionReportProvider } from './context/ConnectionReportProvider';

const App: React.FC = () => {
  return (
    <CallReportProvider>
      <BeanReportProvider>
        <ConnectionReportProvider>
          <HashRouter>
            <Routes>
              <Route path="/" element={<ApplicationLayout />}>
                <Route index element={<BeansReportPage />} />
                <Route path="calls">
                  <Route index element={<CallsReportPage />} />
                  <Route path={':contextId'}>
                    <Route path={':callId'} element={<CallReportPage />} />
                  </Route>
                </Route>
                <Route path="beans" element={<BeansReportPage />} />
                <Route path="connections" element={<ConnectionsReportPage />} />
              </Route>
            </Routes>
          </HashRouter>
        </ConnectionReportProvider>
      </BeanReportProvider>
    </CallReportProvider>
  );
};

export default App;
