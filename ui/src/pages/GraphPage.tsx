import React, { useState, useRef, useEffect } from 'react';
import * as d3 from 'd3';
import { Card, Form, Button, Row, Col, FormLabel, InputGroup } from 'react-bootstrap';
import { SimulationLinkDatum, SimulationNodeDatum } from 'd3-force';

interface GraphProps {
  beanDependencies: Record<string, string[]>;
}

interface Node extends SimulationNodeDatum {
  id: string;
  name: string;
}

interface Edge extends SimulationLinkDatum<Node> {
}

const GraphPage: React.FC<GraphProps> = ({ beanDependencies }) => {
  const graphRef = useRef<SVGSVGElement | null>(null);
  const [beanName, setBeanName] = useState<string>('');
  const [beanNames, setBeanNames] = useState<string[]>([]);

  useEffect(() => {
    const names = Object.keys(beanDependencies).filter(bean => beanDependencies[bean].length > 0);
    setBeanNames(names);
  }, [beanDependencies]);

  const getNodeId = (node: Node | string | number) => {
    if (typeof node === 'object') return node.id;
    if (typeof node === 'number') return String(node);
    return node;
  };

  const highlightNode = (nodeId: string) => {
    const node = d3.selectAll<SVGCircleElement, Node>('.node');
    const link = d3.selectAll<SVGLineElement, Edge>('.link');

    node.classed('selected', false)
      .classed('friend', false);
    link.classed('highlighted', false)
      .attr('marker-end', 'url(#arrowhead)');

    node.filter(n => n && n.id === nodeId).classed('selected', true);

    link.filter(l => l && getNodeId(l.source) === nodeId || l && getNodeId(l.target) === nodeId)
      .classed('highlighted', true)
      .attr('marker-end', 'url(#highlighted-arrowhead)')
      .each(function (edge) {
        if (getNodeId(edge.source) === nodeId) {
          node.filter(n => n && edge && n.id === getNodeId(edge.target))
            .classed('friend', true);
        } else if (getNodeId(edge.target) === nodeId) {
          node.filter(n => n && edge && n.id === getNodeId(edge.source))
            .classed('friend', true);
        }
      });
  };

  // const dragStarted = (simulation: d3.Simulation<Node, any>) => (event) => {
  //   highlightNode(event.subject.id);
  //   if (!event.active) simulation.alphaTarget(0.3).restart();
  //   event.subject.fx = event.subject.x;
  //   event.subject.fy = event.subject.y;
  // };
  //
  // const dragged = (event) => {
  //   event.subject.fx = event.x;
  //   event.subject.fy = event.y;
  // };
  //
  // const dragEnded = (simulation: d3.Simulation<Node, any>) => (event) => {
  //   if (!event.active) simulation.alphaTarget(0);
  //   event.subject.fx = event.x;
  //   event.subject.fy = event.y;
  // };

  const handleBuild = () => {
    if (!beanName || !graphRef.current) return;

    const width = graphRef.current.clientWidth;
    const height = graphRef.current.clientHeight;
    const nodes: Node[] = [{ id: beanName, name: beanName }];
    const edges: Edge[] = [];
    const visited = new Set<string>();

    const traverse = (bean: string) => {
      if (visited.has(bean)) return;
      visited.add(bean);

      const dependencies = beanDependencies[bean] || [];
      dependencies.forEach(dependency => {
        if (visited.has(dependency)) return;
        nodes.push({ id: dependency, name: dependency });
        edges.push({ source: bean, target: dependency });
        traverse(dependency);
      });
    };

    traverse(beanName);

    const svg = d3.select<SVGSVGElement, Node>(graphRef.current);
    svg.selectAll('g').remove();

    const simulation = d3.forceSimulation(nodes)
      .force('link', d3.forceLink<Node, Edge>(edges).id(d => d.id))
      .force('charge', d3.forceManyBody().strength(-200))
      .force('center', d3.forceCenter(width / 2, height / 2));

    const link = svg
      .append('g')
      .attr('class', 'links')
      .selectAll<SVGLineElement, Edge>('line')
      .data(edges)
      .enter()
      .append('line')
      .attr('class', 'link')
      .attr('marker-end', 'url(#arrowhead)');

    const node = svg
      .append('g')
      .attr('class', 'nodes')
      .selectAll<SVGCircleElement, Node>('circle')
      .data(nodes)
      .enter()
      .append('g')
      .attr('class', 'node')
      .on('click', (e, node) => highlightNode(node.id));
    // .call(d3.drag()
    //   .on("start", dragStarted(simulation))
    //   .on("drag", dragged)
    //   .on("end", dragEnded(simulation))
    // );
    node.append('circle')
      .attr('r', 10);

    node.append('text')
      .attr('class', 'node-text')
      .attr('dx', 15)
      .attr('dy', '.35em')
      .text(d => d.name);

    simulation.on('tick', () => {
      link
        .attr(
          'x1',
          d => typeof d.source !== 'string' && typeof d.source !== 'number' && d.source.x !== undefined ? d.source.x : 0
        )
        .attr(
          'y1',
          d => typeof d.source !== 'string' && typeof d.source !== 'number' && d.source.y !== undefined ? d.source.y : 0
        )
        .attr(
          'x2',
          d => typeof d.target !== 'string' && typeof d.target !== 'number' && d.target.x !== undefined ? d.target.x : 0
        )
        .attr(
          'y2',
          d => typeof d.target !== 'string' && typeof d.target !== 'number' && d.target.y !== undefined ? d.target.y : 0
        );

      node
        .attr('cx', d => d.x !== undefined ? d.x : 0)
        .attr('cy', d => d.y !== undefined ? d.y : 0);

      highlightNode(beanName);
    });

  };

  return (
    <Card>
      <Card.Header data-bs-toggle="collapse"
                   data-bs-target="#graphCollapse"
                   aria-expanded="false"
                   aria-controls="graphCollapse"
                   role="button">
        <Card.Title>Dependency Graph</Card.Title>
      </Card.Header>
      <div id="graphCollapse" className="collapse">
        <Row className="align-items-center m-2">
          <Col md={'auto'}>
            <FormLabel htmlFor={'beanName'}>Bean Name</FormLabel>
          </Col>
          <Col md={'auto'}>
            <InputGroup>
              <Form.Control
                id="beanName"
                type="text"
                list="beanNames-suggestions"
                placeholder="Bean Name"
                value={beanName}
                onChange={e => setBeanName(e.target.value)}
              />
              <datalist id="beanNames-suggestions">
                {beanNames.map(it => <option key={it} value={it} />)}
              </datalist>
              <Button variant="outline-secondary" onClick={handleBuild}>
                Build Graph
              </Button>
            </InputGroup>
          </Col>
        </Row>
        <Row className={'m-2'}>
          <svg ref={graphRef}>
            <defs>
              <marker id="arrowhead" className="arrowhead" markerWidth="10" markerHeight="7"
                      refX="10" refY="3.5" orient="auto" markerUnits="strokeWidth">
                <polygon points="0 0, 10 3.5, 0 7" />
              </marker>
              <marker id="highlighted-arrowhead" className="arrowhead highlighted" markerWidth="10"
                      markerHeight="7" refX="10" refY="3.5" orient="auto" markerUnits="strokeWidth">
                <polygon points="0 0, 10 3.5, 0 7" />
              </marker>
            </defs>
          </svg>
        </Row>
      </div>
    </Card>
  );
};

export default GraphPage;
