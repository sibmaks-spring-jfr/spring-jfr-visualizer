export type Stereotype = 'UNKNOWN' | 'COMPONENT' | 'CONTROLLER' | 'REST_CONTROLLER' | 'SERVICE' | 'REPOSITORY'

export interface BeanDefinition {
  scope: number;
  className: number;
  name: number;
  primary: number;
  dependencies: number[] | null;
  stereotype: number;
  generated: boolean;
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
  success: boolean;
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
  type: string;
  message: string;
}

export interface ConnectionEvent {
  index: number;
  action: 'CREATE' | 'COMMIT' | 'ROLLBACK' | 'CLOSE';
  startedAt: number;
  finishedAt: number;
  exception?: Exception;
  threadName?: string;
  transactionIsolation?: number;
}

export interface Connection {
  id: string;
  events: ConnectionEvent[];
  duration: number;
  hasExceptions: boolean;
}

export interface ConnectionReport {
  contexts: Record<string, Record<string, Connection[]>>;
}
