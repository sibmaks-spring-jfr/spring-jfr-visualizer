ReactDOM.render(
    <Container>
        <Row className={'mt-4 mb-4'}>
            <h3>Spring JavaFlightRecorder - Beans Report</h3>
        </Row>
        <Row className="mb-4">
            <div className="col">
                <BeanDefinitions beanDefinitions={beanDefinitions}/>
            </div>
        </Row>
        <Row className="mb-4">
            <div className="col">
                <Beans beanInitialized={beanInitialized}/>
            </div>
        </Row>
        <Row className="mb-4">
            <div className="col">
                <Graph beans={beans}/>
            </div>
        </Row>
    </Container>
    ,
    document.getElementById('root')
);