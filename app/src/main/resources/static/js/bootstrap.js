class Container extends React.Component {
    render() {
        return (
            <div className={`container ${this.props.className || ''}`}>
                {this.props.children}
            </div>
        )
    }
}

class Row extends React.Component {
    render() {
        return (
            <div className={`row ${this.props.className || ''}`}>
                {this.props.children}
            </div>
        )
    }
}