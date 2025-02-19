import { Link, Outlet } from 'react-router-dom';
import React from 'react';
import { Container, Nav } from 'react-bootstrap';

const ApplicationLayout = () => {
  return (
    <Container fluid className="p-0">
      <Nav variant="tabs" defaultActiveKey="/beans" className="mb-3" fill>
        <Nav.Item>
          <Nav.Link as={Link} to={'/beans'} eventKey="beans-report">
            Beans Report
          </Nav.Link>
        </Nav.Item>
        <Nav.Item>
          <Nav.Link as={Link} to={'/calls'} eventKey="calls-report">
            Calls Report
          </Nav.Link>
        </Nav.Item>
      </Nav>
      <main className="flex-nowrap">
        <Outlet />
      </main>
    </Container>
  );
};

export default ApplicationLayout;
