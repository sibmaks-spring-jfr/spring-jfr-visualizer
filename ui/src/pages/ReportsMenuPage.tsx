import React from 'react';
import {
  Container,
  ListGroup,
  ListGroupItem,
} from 'react-bootstrap';

const ReportsMenuPage = () => {
  return (
    <Container
      className="d-flex flex-column justify-content-center align-items-center"
      style={{ height: '100vh', maxWidth: '360px' }}
    >
      <h3>Reports</h3>
      <ListGroup>
        <ListGroupItem action={true} href={'#/beans'}>Beans report</ListGroupItem>
        <ListGroupItem action={true} href={'#/calls'}>Calls report</ListGroupItem>
      </ListGroup>
    </Container>
  );
};


export default ReportsMenuPage;
