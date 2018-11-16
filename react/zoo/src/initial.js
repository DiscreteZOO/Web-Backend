var initialGlobalState = {
    objects: null,
    graphsSelectedCollections: [],
    maniplexesSelectedCollections: [],
    
    columns: {
        graphs: {
            columnSet: "default",
            default: [
                {name: "order", type: "numeric"},
                {name: "connected_components_number", type: "numeric"},
                {name: "diameter", type: "numeric"},
                {name: "girth", type: "numeric"},
                {name: "has_multiple_edges", type: "bool"},
                {name: "is_arc_transitive", type: "bool"},
                {name: "is_bipartite", type: "bool"},
                {name: "is_cayley", type: "bool"},
                {name: "is_distance_regular", type: "bool"},
                {name: "is_distance_transitive", type: "bool"},
                {name: "is_edge_transitive", type: "bool"},
                {name: "is_eulerian", type: "bool"},
                {name: "is_hamiltonian", type: "bool"},
                {name: "is_strongly_regular", type: "bool"}
            ],
            custom: []
        },
        maniplexes: {
            columnSet: "default",
            default: [

            ],
            custom: []
        }
    }
};

export default initialGlobalState;