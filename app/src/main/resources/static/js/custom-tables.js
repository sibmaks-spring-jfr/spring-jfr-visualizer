class CustomTable extends React.Component {
    constructor() {
        super();
        this.state = {
            filters: {},
            sortColumn: null,
            sortDirection: 'asc'
        }
    }

    getCellValue(cell) {
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
    }

    getCellRepresentation(cell) {
        if (!cell) {
            return '';
        }
        if (typeof cell === 'string' || typeof cell === 'number' || typeof cell === 'boolean') {
            return cell;
        }
        return cell.representation;
    }

    handleSort(columnKey) {
        const sortingColumns = this.props.sortingColumns || [];
        if (!sortingColumns.includes(columnKey)) return;
        const direction = this.state.sortColumn === columnKey && this.state.sortDirection === 'asc' ? 'desc' : 'asc';
        this.setState(
            {
                ...this.state,
                sortColumn: columnKey,
                sortDirection: direction
            },
            null
        );
    };

    renderHeader() {
        const cells = [];
        const filteringCells = [];
        const filteringColumns = this.props.filteringColumns || [];
        const sortingColumns = this.props.sortingColumns || [];
        for (let cell of this.props.columns) {
            const td = <th
                key={cell.key}
                onClick={() => this.handleSort(cell.key)}
                role={sortingColumns.includes(cell.key) ? 'button' : ''}
            >
                {cell.label}
                {sortingColumns.includes(cell.key) && this.state.sortColumn === cell.key && (
                    this.state.sortDirection === 'asc' ? ' ▲' : ' ▼'
                )}
            </th>;
            cells.push(td);
            if (!filteringColumns) {
                continue;
            }
            if (filteringColumns.includes(cell.key)) {
                const filterInput = <th>
                    <input
                        className={'form-control'}
                        type='text'
                        placeholder={`${cell.label} filter`}
                        value={this.state.filters[cell.key] || ''}
                        onChange={(e) => {
                            this.setState(
                                {
                                    ...this.state,
                                    filters: ({...this.state.filters, [cell.key]: e.target.value})
                                },
                                null
                            )
                        }
                        }
                    />
                </th>
                filteringCells.push(filterInput);
            } else {
                filteringCells.push(<th></th>);
            }
        }
        return (
            <thead
                className={this.props.thead && this.props.thead.className}
            >
            <tr>{cells}</tr>
            <tr>{filteringCells}</tr>
            </thead>
        );
    }

    filterCell(item, key, value) {
        if (!value) return true;
        const cell = item[key];
        let cellValue = this.getCellValue(cell);
        if (typeof cellValue === 'number') {
            if (value.startsWith('>=')) {
                value = +value.substring(2);
                return cellValue >= value;
            }
            if (value.startsWith('>')) {
                value = +value.substring(1);
                return cellValue > value;
            }
            if (value.startsWith('<=')) {
                value = +value.substring(2);
                return cellValue <= value;
            }
            if (value.startsWith('<')) {
                value = +value.substring(1);
                return cellValue < value;
            }
            if (value.startsWith('==')) {
                value = +value.substring(2);
                return cellValue === value;
            }
            cellValue = `${cellValue}`;
        }
        return cellValue.toLowerCase().includes(value.toLowerCase());
    }

    renderBody() {
        const rows = this.props.data
            .filter((item) => {
                return Object.entries(this.state.filters).every(([key, value]) => {
                    return this.filterCell(item, key, value)
                });
            })
            .sort((a, b) => {
                if (!this.state.sortColumn) return 0;
                let aValue = this.getCellValue(a[this.state.sortColumn]);
                let bValue = this.getCellValue(b[this.state.sortColumn]);
                if (typeof aValue === 'number' && typeof bValue === 'number') {
                    if(aValue === bValue) {
                        return 0;
                    }
                    if(this.state.sortDirection === 'asc') {
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

                return this.state.sortDirection === 'asc'
                    ? aValue.localeCompare(bValue)
                    : bValue.localeCompare(aValue);
            })
            .map(row => {
                return <tr>
                    {
                        this.props.columns.map(column => {
                                const cell = row[column.key];
                                return <td className={cell.className || null}>{this.getCellRepresentation(cell)}</td>;
                            }
                        )
                    }
                </tr>
            })

        return <tbody className={this.props.tbody && this.props.tbody.className}>{rows}</tbody>;
    }

    render() {
        return <table
            className={this.props.className}
        >
            {this.renderHeader()}
            {this.renderBody()}
        </table>
    }
}