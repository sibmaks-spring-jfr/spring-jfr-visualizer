export interface BeanDefinition {
  scope: string | null;
  beanClassName: string | null;
  beanName: string;
  primary: 'true' | 'false' | null;
  dependencies: string[] | null;
  generated: boolean;
}

export interface Bean {
  beanName: string;
  duration: number;
}

