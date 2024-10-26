import React from 'react';
import { Container, Spinner } from 'react-bootstrap';

export const Loader = () => {
  return (
    <Container className={'h-100 w-100 d-flex justify-content-center'}>
      <Spinner variant={'dark'} animation={'border'}>
        <span className="visually-hidden">Loading...</span>
      </Spinner>
    </Container>
  );
}
