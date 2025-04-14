import { NavLink, Outlet } from 'react-router-dom';
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
            Beans
          </NavLink>
        </Nav.Item>
        <Nav.Item>
          <NavLink
            to="/calls"
            className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}
          >
            Calls
          </NavLink>
        </Nav.Item>
        <Nav.Item>
          <NavLink
            to="/connection-pools"
            className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}
          >
            Connection Pools
          </NavLink>
        </Nav.Item>
        <Nav.Item>
          <NavLink
            to="/kafka-consumers"
            className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}
          >
            Kafka Consumers
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
