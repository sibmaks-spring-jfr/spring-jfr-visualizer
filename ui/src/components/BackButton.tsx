import { Outlet, useNavigate } from 'react-router-dom';
import React from 'react';
import { Button } from 'react-bootstrap';

const BackButton = () => {
  const navigate = useNavigate();

  return (
    <Button variant={'outline-secondary'} onClick={() => navigate(-1)}>Back</Button>
  );
};

export default BackButton;
