class Graph extends React.Component {
    constructor() {
        super();
        this.graphRef = React.createRef();
        this.state = {
            beanName: null,
            svg: null,
            beanNames: [],
        };
    }

    componentDidMount() {
        const {beans} = this.props;
        const beanNames = Object.keys(beans).filter(bean => beans[bean].length > 0);
        this.setState({beanNames}, null);
        this.graphRef.current.parentNode.addEventListener('resize', this.handleResize);
    }

    handleResize = () => {
        const width = this.graphRef.current.clientWidth;
        const height = this.graphRef.current.clientHeight;
        d3.select(this.graphRef.current)
            .attr("width", width)
            .attr("height", height)
            .attr("viewBox", `0 0 ${width} ${height}`);
    };

    handleBeanNameChange = (e) => {
        this.setState({beanName: e.target.value}, null);
    };

    handleBuild = () => {
        const {beanName} = this.state;
        if (!beanName) {
            return;
        }
        const {beans} = this.props;
        const nodes = [{id: beanName, name: beanName}];
        const edges = [];
        const visited = new Set();

        const traverse = (bean) => {
            if (visited.has(bean)) return;
            visited.add(bean);

            const dependencies = beans[bean] || [];
            dependencies.forEach(dependency => {
                if (visited.has(dependency)) {
                    return;
                }
                nodes.push({id: dependency, name: dependency});
                edges.push({source: bean, target: dependency});
                traverse(dependency);
            });
        };

        traverse(beanName);

        const width = this.graphRef.current.clientWidth;
        const height = this.graphRef.current.clientHeight;

        let svg = this.state.svg;
        if (!svg) {
            const zoom = d3.zoom()
                .scaleExtent([0.01, 15])
                .on("zoom", (event) => {
                    d3.select(this.graphRef.current).select("g")
                        .attr("transform", event.transform);
                });

            svg = d3.select(this.graphRef.current)
                .attr("width", width)
                .attr("height", height)
                .attr("viewBox", `0 0 ${width} ${height}`)
                .call(zoom)
                .append("g");

            this.setState({svg: svg}, null);
        }

        svg.selectAll("*").remove();

        const simulation = d3.forceSimulation(nodes)
            .force("link", d3.forceLink(edges).id(d => d.id))
            .force("charge", d3.forceManyBody().strength(-150))
            .force('center', d3.forceCenter(width / 2, height / 2))
            .force("edgeCollide", edgeCollide());

        const link = svg.append("g")
            .attr("class", "links")
            .selectAll("line")
            .data(edges)
            .enter().append("line")
            .attr("class", "link")
            .attr("marker-end", "url(#arrowhead)");

        const node = svg.append("g")
            .attr("class", "nodes")
            .selectAll("g")
            .data(nodes)
            .enter().append("g")
            .attr("class", "node")
            .on("click", (e, node) => this.highlightNode(node.id))
            .call(d3.drag()
                .on("start", this.dragStarted(simulation))
                .on("drag", this.dragged)
                .on("end", this.dragEnded(simulation)));

        node.append("circle")
            .attr("r", 10);

        node.append("text")
            .attr("class", "node-text")
            .attr("dx", 15)
            .attr("dy", ".35em")
            .text(d => d.name);

        simulation.on("tick", () => {
            link.attr("x1", d => d.source.x)
                .attr("y1", d => d.source.y)
                .attr("x2", d => d.target.x)
                .attr("y2", d => d.target.y);

            node
                .attr("transform", d => `translate(${d.x},${d.y})`);
        });

        this.highlightNode(beanName);
    };

    highlightNode = (nodeId) => {
        const node = d3.selectAll(".node");
        const link = d3.selectAll(".link");

        node.classed("selected", false).classed("friend", false);
        link.classed("highlighted", false).attr("marker-end", "url(#arrowhead)");

        node.filter(n => n.id === nodeId).classed("selected", true);

        link.filter(l => l.source.id === nodeId || l.target.id === nodeId)
            .classed("highlighted", true)
            .attr("marker-end", "url(#highlighted-arrowhead)")
            .each(function (l) {
                if (l.source.id === nodeId) {
                    node.filter(n => n.id === l.target.id).classed("friend", true);
                } else if (l.target.id === nodeId) {
                    node.filter(n => n.id === l.source.id).classed("friend", true);
                }
            });
    };

    dragStarted = (simulation) => (event) => {
        this.highlightNode(event.subject.id);
        if (!event.active) simulation.alphaTarget(0.3).restart();
        event.subject.fx = event.subject.x;
        event.subject.fy = event.subject.y;
    };

    dragged = (event) => {
        event.subject.fx = event.x;
        event.subject.fy = event.y;
    };

    dragEnded = (simulation) => (event) => {
        if (!event.active) simulation.alphaTarget(0);
        event.subject.fx = event.x;
        event.subject.fy = event.y;
    };

    render() {
        const {beanNames, beanName} = this.state;

        return (
            <div className="card">
                <div className="card-header"
                     data-bs-toggle="collapse"
                     data-bs-target="#graphCollapse"
                     aria-expanded="false"
                     aria-controls="graphCollapse"
                     role="button">
                    <h4 className="card-title">Dependency Graph</h4>
                </div>
                <div id="graphCollapse" className="collapse">
                    <Row className="align-items-center m-2">
                        <div className="col-auto">
                            <label htmlFor="beanName">Bean Name</label>
                        </div>
                        <div className="col-auto">
                            <div className="input-group">
                                <input
                                    id="beanName"
                                    className="form-control"
                                    type="text"
                                    list="beanNames-suggestions"
                                    placeholder="Bean Name"
                                    value={beanName}
                                    onChange={this.handleBeanNameChange}
                                />
                                <datalist id="beanNames-suggestions">
                                    {beanNames.map(it => <option key={it} value={it}/>)}
                                </datalist>
                                <button
                                    className="btn btn-outline-secondary"
                                    type="button"
                                    onClick={this.handleBuild}
                                >
                                    Build
                                </button>
                            </div>
                        </div>
                    </Row>
                    <div id="graph-div">
                        <svg className="w-100 h-100" ref={this.graphRef}>
                            <defs>
                                <marker id="arrowhead" className="arrowhead" markerWidth="10" markerHeight="7"
                                        refX="10" refY="3.5" orient="auto" markerUnits="strokeWidth">
                                    <polygon points="0 0, 10 3.5, 0 7"/>
                                </marker>
                                <marker id="highlighted-arrowhead" className="arrowhead highlighted" markerWidth="10"
                                        markerHeight="7" refX="10" refY="3.5" orient="auto" markerUnits="strokeWidth">
                                    <polygon points="0 0, 10 3.5, 0 7"/>
                                </marker>
                            </defs>
                        </svg>
                    </div>
                </div>
            </div>
        );
    }
}

function edgeCollide() {
    return d3.forceLink()
        .distance(100)
        .strength(1);
}