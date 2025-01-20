export interface BeanDefinition {
  contextId: string;
  scope: string | null;
  beanClassName: string | null;
  beanName: string;
  primary: 'true' | 'false' | null;
  dependencies: string[] | null;
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
  beanDefinitions: BeanDefinition[];
}

export interface CallReport {
  roots: CallTrace[];
}
