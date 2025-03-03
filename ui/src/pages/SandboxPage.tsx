import React from 'react';
import { Container } from 'react-bootstrap';
import Timeline, { Connection } from './Timeline';


const SandboxPage: React.FC = () => {
  const connections = [
    {
      id: '1',
      events: [
        { type: 'create', date: new Date('2023-10-01T10:00:00') },
        { type: 'commit', date: new Date('2023-10-01T10:30:00') },
        { type: 'rollback', date: new Date('2023-10-01T11:00:00') },
        { type: 'close', date: new Date('2023-10-01T12:00:00') },
      ],
    } as Connection,
    {
      id: '2',
      events: [
        { type: 'create', date: new Date('2023-10-02T09:00:00') },
        { type: 'commit', date: new Date('2023-10-02T09:30:00') },
        { type: 'close', date: new Date('2023-10-02T10:00:00') },
      ],
    } as Connection,
  ];

  return (
    <Container>
      <h1>Connection Lifecycle</h1>
      <Timeline connections={connections} />
    </Container>
  );
};

export default SandboxPage;
