import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.min.js';
import BeansReportPage from './pages/report/bean/BeansReportPage';
import { HashRouter, Route, Routes } from 'react-router-dom';
import CallsReportPage from './pages/report/call/CallsReportPage';
import ReportsMenuPage from './pages/ReportsMenuPage';
import ApplicationLayout from './components/ApplicationLayout';
import CallReportPage from './pages/report/call/CallReportPage';
import { BeanReportProvider } from './context/BeanReportProvider';
import { CallReportProvider } from './context/CallReportProvider';

const App: React.FC = () => {
  return (
    <CallReportProvider>
      <BeanReportProvider>
        <HashRouter>
          <Routes>
            <Route path="/" element={<ApplicationLayout />}>
              <Route index element={<ReportsMenuPage />} />
              <Route path="calls">
                <Route index element={<CallsReportPage />} />
                <Route path={':contextId/:callId'} element={<CallReportPage />} />
              </Route>
              <Route path="beans" element={<BeansReportPage />} />
            </Route>
          </Routes>
        </HashRouter>
      </BeanReportProvider>
    </CallReportProvider>
  );
};

export default App;
