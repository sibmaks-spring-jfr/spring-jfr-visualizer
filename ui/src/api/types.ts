export type Stereotype = 'UNKNOWN' | 'COMPONENT' | 'CONTROLLER' | 'REST_CONTROLLER' | 'SERVICE' | 'REPOSITORY'

export interface BeanDefinition {
  scope: string | null;
  beanClassName: string | null;
  beanName: string;
  primary: 'true' | 'false' | null;
  dependencies: string[] | null;
  stereotype: Stereotype | null;
  generated: boolean;
}

export interface Bean {
  contextId: string;
  beanName: string;
  preInitializedAt: number | null;
  postInitializedAt: number;
  duration: number;
}


export enum InvocationType {
  ASYNC,
  JPA,
  CONTROLLER,
  SCHEDULED
}

export interface CallTrace {
  contextId: string;
  correlationId: string | null;
  invocationId: string;
  success: boolean;
  type: InvocationType;
  startTime: number;
  endTime: number;
  threadName: string;
  className: string;
  methodName: string;
  details: Record<string, string>;
  children: CallTrace[];
}

export interface BeanReport {
  beans: Bean[];
  beanDefinitions: Record<string, BeanDefinition[]>;
}

export interface CallReport {
  roots: CallTrace[];
}

export interface RootReport {
  beans: BeanReport
  calls: CallReport
  connections: ConnectionReport
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
