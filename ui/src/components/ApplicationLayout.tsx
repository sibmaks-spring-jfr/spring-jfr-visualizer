import { Outlet } from 'react-router-dom';
import React from 'react';

const ApplicationLayout = () => {
  return (
    <main className="d-flex flex-nowrap">
      <Outlet />
    </main>
  );
};

export default ApplicationLayout;
