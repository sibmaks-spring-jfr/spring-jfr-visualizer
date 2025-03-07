import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.min.js';
import BeansReportPage from './pages/report/bean/BeansReportPage';
import { HashRouter, Route, Routes } from 'react-router-dom';
import ApplicationLayout from './components/ApplicationLayout';
import CallReportPage from './pages/report/call/CallReportPage';
import { RootReportProvider } from './context/RootReportProvider';
import ConnectionsReportPage from './pages/report/connection/ConnectionsReportPage';
import CallsReportPage from './pages/report/call/CallsReportPage';

const App: React.FC = () => {
  return (
    <RootReportProvider>
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
    </RootReportProvider>
  );
};

export default App;
