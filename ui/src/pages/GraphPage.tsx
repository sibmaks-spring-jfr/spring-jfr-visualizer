import React, { useState, useRef, useEffect } from 'react';
import * as d3 from 'd3';
import { Card, Button, Row, Col, FormLabel, InputGroup } from 'react-bootstrap';
import { SimulationLinkDatum, SimulationNodeDatum } from 'd3-force';
import { SuggestiveInput } from '@sibdevtools/frontend-common';
import { SuggestiveItem } from '@sibdevtools/frontend-common/dist/components/suggestive-input/types';
import { BeanDefinition } from '../api/protobuf/beans';
import { Stereotype } from '../api/types';
import { CommonDto } from '../api/protobuf/common';

interface GraphProps {
  common: CommonDto;
  beanDefinitions: BeanDefinition[];
}

interface Node extends SimulationNodeDatum {
  id: number;
  name: string;
  stereotype: Stereotype | null;
}

interface Edge extends SimulationLinkDatum<Node> {
}

const GraphPage: React.FC<GraphProps> = ({
                                           common,
                                           beanDefinitions
                                         }) => {
  const graphRef = useRef<SVGSVGElement | null>(null);
  const [beanId, setBeanId] = useState<number>();
  const [beanNames, setBeanNames] = useState<SuggestiveItem[]>([]);
  const [svg, setSVG] = useState<d3.Selection<SVGGElement, Node, null, undefined> | null>(null);
  const [beanDependencies, setBeanDependencies] = useState<Map<number, number[]> | null>();

  useEffect(() => {
    const beanDependencies = new Map<number, number[]>();
    for (let beanDefinition of beanDefinitions) {
      const dependencies = beanDefinition.dependencies || [];
      const length = dependencies.length || 0;
      if (length <= 0) {
        continue;
      }
      beanDependencies.set(beanDefinition.name, dependencies);
    }
    setBeanDependencies(beanDependencies);

    const newBeanNames: SuggestiveItem[] = [];
    for (let beanDefinition of beanDefinitions) {
      if ((beanDefinition.dependencies?.length ?? 0) <= 0) {
        continue;
      }
      newBeanNames.push({ key: `${beanDefinition.name}`, value: common.stringConstants[beanDefinition.name] });
    }
    setBeanNames(newBeanNames);
  }, [beanDefinitions]);

  useEffect(() => {
    const graph = graphRef.current;
    if (!graph) {
      return;
    }
    if (svg) {
      return;
    }

    const zoom = d3.zoom<SVGSVGElement, Node>()
      .scaleExtent([0.01, 15])
      .on('zoom', (event) => {
        d3.select(graphRef.current).select('g')
          .attr('transform', event.transform);
      });

    const newSVG = d3.select<SVGSVGElement, Node>(graph);
    newSVG
      .selectAll('g')
      .remove();

    setSVG(
      newSVG
        .call(zoom)
        .append('g')
    );
  }, [graphRef, graphRef.current]);

  const getNodeId = (node: Node | string | number) => {
    if (typeof node === 'object') return node.id;
    if (typeof node === 'number') return String(node);
    return node;
  };

  const highlightNode = (nodeId: number) => {
    const node = d3.selectAll<SVGCircleElement, Node>('.node');
    const link = d3.selectAll<SVGLineElement, Edge>('.link');

    node.classed('selected', false)
      .classed('friend', false);
    link.classed('highlighted', false)
      .attr('marker-end', 'url(#arrowhead)');
    link.classed('highlighted-in', false)
      .attr('marker-end', 'url(#arrowhead)');

    node.filter(n => n && n.id === nodeId).classed('selected', true);


    const doSome = (edge: Edge) => {
      if (getNodeId(edge.source) === nodeId) {
        node.filter(n => n && edge && n.id === getNodeId(edge.target))
          .classed('friend', true);
      } else if (getNodeId(edge.target) === nodeId) {
        node.filter(n => n && edge && n.id === getNodeId(edge.source))
          .classed('friend', true);
      }
    };

    link.filter(l => l && getNodeId(l.source) === nodeId)
      .classed('highlighted', true)
      .attr('marker-end', 'url(#highlighted-arrowhead)')
      .each(doSome);

    link.filter(l => l && getNodeId(l.target) === nodeId)
      .classed('highlighted-in', true)
      .attr('marker-end', 'url(#highlighted-in-arrowhead)')
      .each(doSome);
  };

  const dragStarted = (simulation: d3.Simulation<Node, any>) => (event: any) => {
    if (!event.active) {
      highlightNode(event.subject.id);
      simulation.alphaTarget(0.3).restart();
    }
    event.subject.fx = event.subject.x;
    event.subject.fy = event.subject.y;
  };

  const dragged = (event: any) => {
    event.subject.fx = event.x;
    event.subject.fy = event.y;
  };

  const dragEnded = (simulation: d3.Simulation<Node, any>) => (event: any) => {
    if (!event.active) simulation.alphaTarget(0);
    event.subject.fx = event.x;
    event.subject.fy = event.y;
  };

  const handleBuild = () => {
    if (!beanId || !graphRef.current || !svg) return;

    const width = graphRef.current.clientWidth;
    const height = graphRef.current.clientHeight;
    const stereotype = beanDefinitions.find(b => b.name === beanId)?.stereotype ?? -1;
    const nodes: Node[] = [{
      id: beanId,
      name: common.stringConstants[beanId],
      stereotype: stereotype === -1 ? null : common.stringConstants[stereotype] as Stereotype
    }];
    const edges: Edge[] = [];
    const visited = new Set<number>();

    const traverse = (bean: number) => {
      if (visited.has(bean)) return;
      visited.add(bean);

      const dependencies = beanDependencies?.get(bean) || [];
      dependencies.forEach(dependency => {
        if (!visited.has(dependency)) {
          const beanDefinition = beanDefinitions.find(b => b.name === dependency);
          const stereotype = beanDefinition?.stereotype ?? -1;
          nodes.push({
            id: dependency,
            name: common.stringConstants[dependency],
            stereotype: stereotype === -1 ? null : common.stringConstants[stereotype] as Stereotype
          });
          traverse(dependency);
        }
        edges.push({ source: bean, target: dependency });
      });
    };

    traverse(beanId);

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

    const drag = d3.drag<SVGGElement, Node>()
      .on('start', dragStarted(simulation))
      .on('drag', dragged)
      .on('end', dragEnded(simulation));

    const node = svg
      .append('g')
      .attr('class', 'nodes')
      .selectAll<SVGCircleElement, Node>('g')
      .data(nodes)
      .enter()
      .append('g')
      .attr('class', 'node')
      .on('click', (e, node) => {
        highlightNode(node.id);
      })
      .call(drag);

    /*node.append('circle')
      .attr('r', 10);*/

    node.each(function (d) {
      const nodeGroup = d3.select(this);

      if (d.stereotype === 'CONTROLLER' || d.stereotype === 'REST_CONTROLLER') {
        nodeGroup.append('circle')
          .attr('r', 10);
      } else if (d.stereotype === 'SERVICE') {
        nodeGroup
          .append('rect')
          .attr('width', 20)
          .attr('height', 20)
          .attr('rx', 5)
          .attr('ry', 5)
          .attr('x', -10)
          .attr('y', -10);
      } else if (d.stereotype === 'REPOSITORY') {
        nodeGroup
          .append('rect')
          .attr('width', 20)
          .attr('height', 20)
          .attr('x', -10)
          .attr('y', -10);
      } else {
        nodeGroup
          .append('path')
          .attr('d', 'M -10,10 L 10,10 L 0,-10 Z');
      }
    });

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
        .attr('transform', d => `translate(${d.x},${d.y})`);
    });

    highlightNode(beanId);
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
          <Col xl={2}>
            <FormLabel htmlFor={'beanName'}>Bean Name</FormLabel>
          </Col>
          <Col xl={10}>
            <InputGroup>
              <SuggestiveInput
                id="beanName"
                mode={'strict'}
                onChange={it => setBeanId(it.key ? +it.key : undefined)}
                value={beanId ? common.stringConstants[beanId] : ''}
                suggestions={beanNames}
                placeholder={'Bean Name'}
                required={true}
              />
              <Button variant="outline-secondary" onClick={handleBuild}>
                Build
              </Button>
            </InputGroup>
          </Col>
        </Row>
        <Row className={'m-2 h-100'}>
          <svg className={'h-100 graph-svg'} ref={graphRef}>
            <defs>
              <marker id="arrowhead" className="arrowhead" markerWidth="10" markerHeight="7"
                      refX="10" refY="3.5" orient="auto" markerUnits="strokeWidth">
                <polygon points="0 0, 10 3.5, 0 7" />
              </marker>
              <marker id="highlighted-arrowhead" className="arrowhead highlighted" markerWidth="10"
                      markerHeight="7" refX="10" refY="3.5" orient="auto" markerUnits="strokeWidth">
                <polygon points="0 0, 10 3.5, 0 7" />
              </marker>
              <marker id="highlighted-in-arrowhead" className="arrowhead highlighted-in" markerWidth="10"
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
