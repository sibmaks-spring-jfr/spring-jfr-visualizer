import React, { useState } from 'react';
import { Table, Form } from 'react-bootstrap';

export interface TableColumn {
  key: string;
  label: string;
}

export interface StyleProps {
  centerHeaders: boolean,
  textCenterValues: boolean,
}

export type CellValue = string | number | boolean | null;

export type Cell = ReactCell | CellValue;

export type Row = { [key: string]: Cell };

export type CellClickHandler = (row: Row, cell: Cell) => void;

export interface ReactCell {
  /**
   * Data UI representation
   */
  representation: React.ReactNode;
  /**
   * Click handler
   * @param row clicked row
   * @param cell clicked cell
   */
  onClick?: CellClickHandler;
  /**
   * Data text representation for filtering and sorting
   */
  value?: CellValue;
  /**
   * Cell TD class name
   */
  className?: string | null;
}

export interface CustomTableProps {
  className?: string;
  thead?: {
    className?: string;
  };
  columns: TableColumn[];
  data: Array<Row>;
  sortableColumns?: string[];
  filterableColumns?: string[];
  styleProps?: StyleProps;
  onRowClick?: (row: Row) => void;
}

const CustomTable: React.FC<CustomTableProps> = ({
                                                   className = null,
                                                   thead = null,
                                                   columns,
                                                   data,
                                                   sortableColumns = [],
                                                   filterableColumns = [],
                                                   styleProps = {
                                                     centerHeaders: true,
                                                     textCenterValues: false,
                                                   },
                                                   onRowClick = null,
                                                 }) => {
  const [filter, setFilter] = useState<{ [key: string]: string }>({});
  const [sortColumn, setSortColumn] = useState<string>('');
  const [sortDirection, setSortDirection] = useState<'asc' | 'desc'>('asc');

  const handleFilterChange = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>, columnKey: string) => {
    setFilter({ ...filter, [columnKey]: event.target.value });
  };

  const handleSort = (columnKey: string) => {
    if (!sortableColumns.includes(columnKey)) return;
    const direction = sortColumn === columnKey && sortDirection === 'asc' ? 'desc' : 'asc';
    setSortColumn(columnKey);
    setSortDirection(direction);
  };

  const getCellValue = (cell: Cell): string | number => {
    if (!cell) {
      return '';
    }
    if (typeof cell == 'string' || typeof cell === 'number') {
      return cell;
    }
    if (typeof cell === 'boolean') {
      return `${cell}`;
    }
    if (typeof cell.value === 'string') {
      return cell.value;
    }
    return cell.value ? `${cell.value}` : '';
  };

  const getCellRepresentation = (cell: Cell): React.ReactNode => {
    if (!cell) {
      return '';
    }
    if (typeof cell === 'string' || typeof cell === 'number' || typeof cell === 'boolean') {
      return cell;
    }
    return cell.representation;
  };

  const getCellClassName = (cell: Cell): React.ReactNode => {
    if (!cell || typeof cell === 'string' || typeof cell === 'number' || typeof cell === 'boolean') {
      return '';
    }
    return cell.className;
  };

  const getCellOnClick = (row: Row, cell: Cell): undefined | (() => void) => {
    if (!cell) {
      return undefined;
    }
    if (typeof cell === 'string' || typeof cell === 'number' || typeof cell === 'boolean') {
      return undefined;
    }
    const onClick = cell.onClick;
    if (!onClick) {
      return undefined;
    }
    return () => onClick(row, cell);
  };

  const filterCell = (item: Row, key: string, value: string): boolean => {
    if (!value) return true;
    const cell = item[key];
    let cellValue = getCellValue(cell);
    if (typeof cellValue === 'number') {
      if (value.startsWith('>=')) {
        const numberValue = +value.substring(2);
        return cellValue >= numberValue;
      }
      if (value.startsWith('>')) {
        const numberValue = +value.substring(1);
        return cellValue > numberValue;
      }
      if (value.startsWith('<=')) {
        const numberValue = +value.substring(2);
        return cellValue <= numberValue;
      }
      if (value.startsWith('<')) {
        const numberValue = +value.substring(1);
        return cellValue < numberValue;
      }
      if (value.startsWith('==')) {
        const numberValue = +value.substring(2);
        return cellValue === numberValue;
      }
      cellValue = `${cellValue}`;
    }
    return cellValue.toLowerCase().includes(value.toLowerCase());
  };

  const rowComparator = (a: Row, b: Row) => {
    if (!sortColumn) return 0;
    let aValue = getCellValue(a[sortColumn]);
    let bValue = getCellValue(b[sortColumn]);
    if (typeof aValue === 'number' && typeof bValue === 'number') {
      if (aValue === bValue) {
        return 0;
      }
      if (sortDirection === 'asc') {
        return aValue > bValue ? 1 : -1;
      }
      return aValue > bValue ? -1 : 1;
    }
    if (typeof aValue !== 'string') {
      aValue = `${aValue}`;
    }
    if (typeof bValue !== 'string') {
      bValue = `${bValue}`;
    }

    return sortDirection === 'asc'
      ? aValue.localeCompare(bValue)
      : bValue.localeCompare(aValue);
  };

  const preparedData = data
    .filter((item) => {
      return Object.entries(filter).every(([key, value]) => {
        if (!value) return true;
        return filterCell(item, key, value);
      });
    })
    .sort(rowComparator);

  return (
    <Table striped={true} bordered={true} hover={true} className={className || ''}>
      <thead className={`table-dark ${thead?.className || ''}`}>
      <tr className={`${styleProps.centerHeaders ? 'text-center' : ''}`}>
        {columns.map((column) => (
          <th
            key={column.key}
            onClick={() => handleSort(column.key)}
            style={{ cursor: sortableColumns.includes(column.key) ? 'pointer' : 'default' }}
          >
            {column.label}
            {sortableColumns.includes(column.key) && sortColumn === column.key && (
              sortDirection === 'asc' ? ' ▲' : ' ▼'
            )}
          </th>
        ))}
      </tr>
      <tr>
        {columns.map((column) => (
          <th key={`filter-${column.key}`}>
            {filterableColumns.includes(column.key) && (
              <Form.Control
                type={'text'}
                placeholder={`Filter ${column.label}`}
                value={filter[column.key] || ''}
                onChange={(e) => handleFilterChange(e, column.key)}
              />
            )}
          </th>
        ))}
      </tr>
      </thead>
      <tbody>
      {preparedData.map((item, index) => (
        <tr
          key={index}
          onClick={onRowClick ? () => onRowClick(item) : undefined}
          role={onRowClick ? 'button' : undefined}>
          {columns.map((column) => {
            const cellOnClick = getCellOnClick(item, item[column.key]);
            return (
              <td
                className={`${styleProps.textCenterValues ? 'text-center' : ''} ${getCellClassName(item[column.key])}`}
                onClick={cellOnClick}
                role={cellOnClick ? 'button' : undefined}
                key={column.key}>{getCellRepresentation(item[column.key])}</td>
            );
          })}
        </tr>
      ))}
      </tbody>
    </Table>
  );
};

export default CustomTable;
