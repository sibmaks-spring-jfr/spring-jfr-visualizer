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

