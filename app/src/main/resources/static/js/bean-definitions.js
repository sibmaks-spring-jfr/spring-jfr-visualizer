class BeanDefinitions extends React.Component {
    render() {
        const beanDefinitions = this.props.beanDefinitions;

        return <div className="card">
            <div className="card-header"
                 data-bs-toggle="collapse"
                 data-bs-target="#beanDefinitionsCollapse"
                 aria-expanded="false"
                 aria-controls="beanDefinitionsCollapse"
                 role={'button'}>
                <h4 className="card-title">
                    Bean Definitions
                </h4>
            </div>
            <div id="beanDefinitionsCollapse" className="table-responsive collapse">
                <CustomTable
                    className={'card-body overflow-scroll table table-striped table-hover'}
                    thead={{className: 'table-dark'}}
                    columns={[
                        {
                            key: 'scope',
                            label: 'Scope'
                        },
                        {
                            key: 'beanClassName',
                            label: 'Class Name'
                        },
                        {
                            key: 'beanName',
                            label: 'Bean Name'
                        },
                        {
                            key: 'primary',
                            label: 'Primary'
                        },
                        {
                            key: 'dependencies',
                            label: 'Dependencies'
                        },
                    ]}
                    data={beanDefinitions.map(it => {
                        return {
                            scope: {
                                representation: <div className="content-scroll">{it.scope}</div>,
                                value: it.scope,
                                className: 'td-96'
                            },
                            beanClassName: {
                                representation: <div className="content-scroll">{it.beanClassName}</div>,
                                value: it.beanClassName,
                                className: 'td-512'
                            },
                            beanName: {
                                representation: <div className="content-scroll">{it.beanName}</div>,
                                value: it.beanName,
                                className: 'td-512'
                            },
                            primary: it.primary ? 'Yes' : 'No',
                            dependencies: {
                                representation: <ul className="content-scroll">
                                    {it.dependencies.map(it => {
                                        return (<li key={it}>{it}</li>)
                                    })}
                                </ul>,
                                value: it.dependencies.join(", "),
                                className: 'td-1024'
                            }
                        }
                    })}
                    filteringColumns={['scope', 'beanClassName', 'beanName', 'primary', 'dependencies']}
                    sortingColumns={['scope', 'beanClassName', 'beanName', 'dependencies']}
                />
            </div>
        </div>
    }
}