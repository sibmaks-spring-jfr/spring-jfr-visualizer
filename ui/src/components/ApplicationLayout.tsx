import { Outlet, NavLink } from 'react-router-dom';
import React from 'react';
import { Container, Nav } from 'react-bootstrap';

const ApplicationLayout = () => {
  return (
    <Container fluid className="p-0">
      <Nav variant="tabs" defaultActiveKey="/beans" className="mb-3" fill>
        <Nav.Item>
          <NavLink
            to="/beans"
            end
            className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}
          >
            Beans Report
          </NavLink>
        </Nav.Item>
        <Nav.Item>
          <NavLink
            to="/calls"
            className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}
          >
            Calls Report
          </NavLink>
        </Nav.Item>
        <Nav.Item>
          <NavLink
            to="/connections"
            className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}
          >
            Connection Report
          </NavLink>
        </Nav.Item>
      </Nav>
      <main className="flex-nowrap">
        <Outlet />
      </main>
    </Container>
  );
};

export default ApplicationLayout;
