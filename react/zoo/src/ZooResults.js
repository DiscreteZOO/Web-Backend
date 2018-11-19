import React, { Component } from 'react';
import { Container, Row, Col, Button, ButtonGroup, FormGroup, Label, Input } from 'reactstrap';

const columnsData = {
    graphs: {
        columnSet: "default",
        default: [
            {name: "order", display: "order", type: "numeric"},
            {name: "connected_components_number", display: "connected components number", type: "numeric"},
            {name: "diameter", display: "diameter", type: "numeric"},
            {name: "girth", display: "girth", type: "numeric"},
            {name: "has_multiple_edges", display: "has multiple edges", type: "bool"},
            {name: "is_arc_transitive", display: "is arc transitive", type: "bool"},
            {name: "is_bipartite", display: "is bipartite", type: "bool"},
            {name: "is_cayley", display: "is cayley", type: "bool"},
            {name: "is_distance_regular", display: "is distance regular", type: "bool"},
            {name: "is_distance_transitive", display: "is distance transitive", type: "bool"},
            {name: "is_edge_transitive", display: "is edge transitive", type: "bool"},
            {name: "is_eulerian", display: "is eulerian", type: "bool"},
            {name: "is_hamiltonian", display: "is hamiltonian", type: "bool"},
            {name: "is_strongly_regular", display: "is strongly regular", type: "bool"}
        ],
        custom: []
    },
    maniplexes: {
        columnSet: "default",
        default: [
            {name: "ORBITS", display: "number of orbits", type: "numeric"},
            {name: "IS_POLYTOPE", display: "is polytope", type: "bool"},
            {name: "IS_REGULAR", display: "is regular", type: "bool"},
            {name: "SMALL_GROUP_ORDER", display: "group order", type: "numeric"},
            {name: "SYMMETRY_TYPE", display: "symmetry type", type: "string"}
        ],
        custom: []
    }
}

function camelCase(s) { return s.replace(/_([a-z])/g, function (g) { return g[1].toUpperCase(); }); }

class ZooResults extends Component {
    
    render() {
        return (
            <section id="results">
				<Container>
					<Row>
                        <Col lg="12">
                            <div className="table-responsive">
                                <table className="table table-striped">
                                    <thead>
                                        <tr>
                                            {columnsData.graphs.default.map((c) => {
                                                return <th key={c.name}>{c.display}</th>
                                            })}
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {this.props.results.map((r) => {
                                            return(
                                                <tr key={r.zooid}>
                                                    {columnsData.graphs.default.map((c) => {
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