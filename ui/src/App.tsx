import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.min.js';
import BeansReportPage from './pages/report/bean/BeansReportPage';
import { HashRouter, Route, Routes } from 'react-router-dom';
import ApplicationLayout from './components/ApplicationLayout';
import CallReportPage from './pages/report/call/CallReportPage';
import { BeanReportProvider } from './context/BeanReportProvider';
import { CallReportProvider } from './context/CallReportProvider';
import SandboxPage from './pages/SandboxPage';
import CallsReportPage from './pages/report/call/CallsReportPage';

const App: React.FC = () => {
  return (
    <CallReportProvider>
      <BeanReportProvider>
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
              <Route path="sandbox" element={<SandboxPage />} />
            </Route>
          </Routes>
        </HashRouter>
      </BeanReportProvider>
    </CallReportProvider>
  );
};

export default App;
