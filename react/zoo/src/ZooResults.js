import React, { Component } from 'react';
import { Container, Row, Col } from 'reactstrap';
import objectProperties from './objectProperties.json';

function camelCase(s) { return s.replace(/_([a-z])/g, function (g) { return g[1].toUpperCase(); }); }

const defaultColumns = {
    graphs: ["order", "vt", "cvt", "symcubic", "diameter", "girth", 
             "is_arc_transitive", "is_bipartite", "is_cayley", "is_hamiltonian"],
    maniplexes: []
}



class ZooColumnSettings extends Component {
    render() {
        return (
            <th key={this.props.column.name}>
                <i className="fas fa-minus"></i>
                <i className="fas fa-plus"></i>
            </th>
        );
    }
}

class ZooResults extends Component {
    
    constructor(props) {
        super(props);
        this.state = {
            columns: defaultColumns
        };
        this.getColumns = (objects) => {
            const colNames = this.state.columns[this.props.objects]
            const colObjects = colNames.map((columnName) => {
                var obj = objectProperties[objects][columnName];
                obj.name = columnName;
                return obj;  
            })
            return colObjects;
        }
    }
    
    render() {
        const columns = this.getColumns(this.props.objects);
        return (
            <section id="results">
				<Container>
					<Row>
                        <Col lg="12">
                            <div className="table-responsive">
                                <table className="table table-striped">
                                    <thead>
                                        <tr>
                                            {columns.map((c) => <th key={c.name}>{c.display}</th>)}
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {this.props.results.map((r) => {
                                            return(
                                                <tr key={r.zooid}>
                                                    {columns.map((c) => {
                                                        return(
                                                            <td key={r.zooid + "-" + c.name}>
                                                                {String(r[c.type][camelCase(c.name)])}
                                                            </td>
                                                        );
                                                    })}
                                                </tr>
                                            );
                                        })}
                                    </tbody>
                                </table>
                            </div>
					   </Col>
				    </Row>
                </Container>
            </section>
        );
    }
}



export default ZooResults;