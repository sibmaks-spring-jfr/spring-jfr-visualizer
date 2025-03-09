export type Stereotype = 'UNKNOWN' | 'COMPONENT' | 'CONTROLLER' | 'REST_CONTROLLER' | 'SERVICE' | 'REPOSITORY'

export interface BeanDefinition {
  scope: number;
  className: number;
  name: number;
  primary: number;
  dependencies: number[] | null;
  stereotype: number;
  generated: 0 | 1;
}

export interface Bean {
  contextId: number;
  beanName: number;
  preInitializedAt: number | null;
  postInitializedAt: number;
  duration: number;
}


export type InvocationType = 'ASYNC' | 'JPA' | 'CONTROLLER' | 'SCHEDULED' | 'SERVICE' | 'COMPONENT'

export interface CallTrace {
  contextId: number;
  correlationId: number | null;
  invocationId: number;
  success: 0 | 1;
  type: number;
  startTime: number;
  endTime: number;
  threadName: number;
  className: number;
  methodName: number;
  details: Record<number, number>;
  children: CallTrace[];
}

export interface BeanReport {
  beans: Bean[];
  beanDefinitions: Record<number, BeanDefinition[]>;
}

export interface CallReport {
  roots: CallTrace[];
}

export interface RootReport {
  common: Common;
  beans: BeanReport;
  calls: CallReport;
  connections: ConnectionReport;
}

export interface Common {
  stringConstants: string[];
}

export interface Exception {
  type: number;
  message: number;
}

export type ConnectionAction = 'CREATE' | 'COMMIT' | 'ROLLBACK' | 'CLOSE'

export interface ConnectionEvent {
  index: number;
  action: number;
  startedAt: number;
  finishedAt: number;
  exception?: Exception;
  threadName?: number;
  transactionIsolation?: number;
}

export interface Connection {
  id: string;
  events: ConnectionEvent[];
  duration: number;
  hasExceptions: boolean;
}

export interface ConnectionReport {
  contexts: Record<number, Record<number, Connection[]>>;
}
