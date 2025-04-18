import React, { StrictMode } from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';

import './css/graph.css';
import './css/table.css';

const rootElement = document.getElementById('root');
const root = ReactDOM.createRoot(rootElement!);
root.render(
  <StrictMode>
    <App />
  </StrictMode>
);
