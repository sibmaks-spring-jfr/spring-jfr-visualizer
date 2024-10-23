class Beans extends React.Component {
    render() {
        const beanInitialized = this.props.beanInitialized;

        return <div className="card">
            <div className="card-header"
                 data-bs-toggle="collapse"
                 data-bs-target="#beansCollapse"
                 aria-expanded="false"
                 aria-controls="beansCollapse"
                 role={'button'}>
                <h4 className="card-title">
                    Beans
                </h4>
            </div>
            <div id="beansCollapse" className="table-responsive collapse">
                <CustomTable
                    className={'card-body overflow-scroll table table-striped table-hover'}
                    thead={{className: 'table-dark'}}
                    columns={[
                        {
                            key: 'beanName',
                            label: 'Bean Name'
                        },
                        {
                            key: 'duration',
                            label: 'Duration'
                        },
                    ]}
                    data={beanInitialized.map(it => {
                        return {
                            beanName: it.beanName,
                            duration: it.duration,
                        }
                    })}
                    filteringColumns={['beanName', 'duration']}
                    sortingColumns={['beanName', 'duration']}
                />
            </div>
        </div>
    }
}