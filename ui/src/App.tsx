import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.min.js';
import BeansReportPage from './pages/BeansReportPage';
import { BrowserRouter, HashRouter, Route, Routes } from 'react-router-dom';
import CallsReportPage from './pages/CallsReportPage';
import ReportsMenuPage from './pages/ReportsMenuPage';
import ApplicationLayout from './components/ApplicationLayout';

const App: React.FC = () => {
  return (
    <HashRouter>
      <Routes>
        <Route path="/" element={<ApplicationLayout />}>
          <Route index element={<ReportsMenuPage />} />
          <Route path="calls" element={<CallsReportPage />} />
          <Route path="beans" element={<BeansReportPage />} />
        </Route>
      </Routes>
    </HashRouter>
  );
};

export default App;
