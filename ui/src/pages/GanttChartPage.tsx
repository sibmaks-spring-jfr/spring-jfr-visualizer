import React, { useRef, useEffect } from 'react';
import * as d3 from 'd3';
import { Bean } from '../api/types';

interface GanttChartProps {
  beans: Bean[];
  width?: number;
  height?: number;
}

export interface ProcessedBean extends Bean {
  start: Date;
  end: Date;
  lane: number;
}

const GanttChartPage: React.FC<GanttChartProps> = ({ beans, width = 800, height = 400 }) => {
  const svgRef = useRef<SVGSVGElement | null>(null);

  useEffect(() => {
    if (!beans || beans.length === 0) return;

    // Parse dates and ensure 'start' is not null
    const parseDate = d3.isoParse; // assuming ISO date strings

    const processedData: ProcessedBean[] = beans.map(bean => {
      const startDate = bean.preInitializedAt ? parseDate(bean.preInitializedAt) : parseDate(bean.postInitializedAt);
      if (!startDate) {
        throw new Error(`Invalid date format for bean: ${bean.beanName}`);
      }
      const endDate = parseDate(bean.postInitializedAt);
      if (!endDate) {
        throw new Error(`Invalid postInitializedAt date for bean: ${bean.beanName}`);
      }
      return {
        ...bean,
        start: startDate,
        end: endDate,
        lane: 0, // will be assigned later
      };
    });

    // Sort the data by start time
    const sortedData = processedData.sort((a, b) => a.start.getTime() - b.start.getTime());

    // Assign lanes to handle overlaps
    const lanes: Date[] = []; // Track the end time of the last bean in each lane

    sortedData.forEach(bean => {
      let placed = false;
      for (let i = 0; i < lanes.length; i++) {
        if (lanes[i].getTime() <= bean.start.getTime()) {
          lanes[i] = bean.end;
          bean.lane = i;
          placed = true;
          break;
        }
      }
      if (!placed) {
        lanes.push(bean.end);
        bean.lane = lanes.length - 1;
      }
    });

    const numLanes = lanes.length;
    const margin = { top: 20, right: 30, bottom: 50, left: 150 };
    const innerWidth = width - margin.left - margin.right;
    const innerHeight = height - margin.top - margin.bottom;

    // Clear previous SVG content
    d3.select(svgRef.current).selectAll('*').remove();

    const svg = d3
      .select(svgRef.current)
      .attr('width', width)
      .attr('height', height);

    // Define scales
    const xExtent: [Date, Date] = d3.extent(sortedData.flatMap(d => [d.start, d.end])) as [Date, Date];
    const xScale = d3.scaleTime().domain(xExtent).range([0, innerWidth]);

    const yScale = d3.scaleBand().domain(d3.range(numLanes).map(String)).range([0, innerHeight]).padding(0.1);

    const g = svg
      .append('g')
      .attr('transform', `translate(${margin.left},${margin.top})`);

    // X Axis
    const xAxis = d3.axisBottom<Date>(xScale)
      .ticks(d3.timeMillisecond.every(100))
      .tickFormat((date: Date) => d3.timeFormat('%H:%M:%S')(date));

    g.append('g')
      .attr('transform', `translate(0, ${innerHeight})`)
      .call(xAxis)
      .selectAll('text')
      .attr('transform', 'rotate(-45)')
      .style('text-anchor', 'end');

    // Y Axis (lanes)
    g.append('g')
      .call(d3.axisLeft(d3.scaleBand().domain(d3.range(numLanes).map(String)).range([0, innerHeight])));

    // Bars
    g.selectAll('.bar')
      .data(sortedData)
      .enter()
      .append('rect')
      .attr('class', 'bar')
      .attr('x', d => xScale(d.start))
      .attr('y', d => yScale(String(d.lane))!)
      .attr('width', d => xScale(d.end) - xScale(d.start))
      .attr('height', yScale.bandwidth())
      .attr('fill', 'steelblue')
      .append('title') // Tooltip
      .text(d => `${d.beanName}\nStart: ${d.preInitializedAt ?? 'N/A'}\nEnd: ${d.postInitializedAt}`);

    // Labels (optional)
    g.selectAll('.label')
      .data(sortedData)
      .enter()
      .append('text')
      .attr('x', d => xScale(d.start) + 5)
      .attr('y', d => yScale(String(d.lane))! + yScale.bandwidth() / 2)
      .attr('dy', '.35em')
      .text(d => d.beanName)
      .attr('fill', 'black')
      .attr('font-size', '10px');

  }, [beans, width, height]);

  return <svg ref={svgRef}></svg>;
};

export default GanttChartPage;
