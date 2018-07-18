var zooapp;

(function () {
    
    'use strict';
    
    function internalCompareFilters(f, g) {
        if (f.name < g.name) return -1;
        if (f.name > g.name) return 1;
        return 0;
    }
    
    
    function internalSelectFilter(filter) {
        $.each(zooapp.filters[zooapp.objects].available, function(i, f) {
            if (f.name == filter) {
                zooapp.filters[zooapp.objects].selected.push({ name: f.name, type: f.type, value: null, edit: true });
            }
        })
        zooapp.filters[zooapp.objects].selected.sort(internalCompareFilters);
        zooapp.filters[zooapp.objects].available = (
            $.grep(zooapp.filters[zooapp.objects].available, function(f) { return f.name != filter; })
        ).sort(internalCompareFilters);
    }
    
    function internalRemoveFilter(filter) {
        $.each(zooapp.filters[zooapp.objects].selected, function(i, f) {
            if (f.name == filter) {
                zooapp.filters[zooapp.objects].available.push(f);
            }
        })
        zooapp.filters[zooapp.objects].available.sort(internalCompareFilters);
        zooapp.filters[zooapp.objects].selected = (
            $.grep(zooapp.filters[zooapp.objects].selected, function(f) { return f.name != filter; })
        ).sort(internalCompareFilters);
    }
    
    function internalEditFilter(filter) {
        var f = $.grep(zooapp.filters[zooapp.objects].selected, function(f) { return f.name == filter; })[0];
        f.edit = true;
    }
    
    function internalObjectCount() {
        var queryParameters = [];
        queryParameters.push({ name: "collections", value: JSON.stringify(zooapp.collections[zooapp.objects].selected) });
        var parameters = zooapp.filters[zooapp.objects].selected.concat(queryParameters);
        $.get("/search/object-count", parameters, function(data) {
            zooapp.objectCount = data;
        });
    }
    
    function internalSubmitFilterValue(filter) {
        var f = $.grep(zooapp.filters[zooapp.objects].selected, function(f) { return f.name == filter; })[0];
        var validNumeric = (f.type == "numeric" && /^(=|<=|>=|<|>|<>|!=)(\d+\.?\d*)$/.test(f.value.replace(/ /g, '')));
        var validBool = (f.type == "bool" && (f.value == "true" || f.value == "false"));
        if (validNumeric || validBool) {
            f.edit = false;
            internalObjectCount();
        }
    }
    
    function internalDisplayResults() {
        var queryParameters = [];
        var orderBy = [];
        if (zooapp.resultsOrderBy1 && zooapp.resultsOrderBy1.column != "none") {
            orderBy.push(zooapp.resultsOrderBy1);
            if (zooapp.resultsOrderBy2 && zooapp.resultsOrderBy2.column != "none")
                orderBy.push(zooapp.resultsOrderBy2);
        }
        queryParameters.push({ name: "collections", value: JSON.stringify(zooapp.collections[zooapp.objects].selected) });
        queryParameters.push({ name: "display_page", value: zooapp.page });
        queryParameters.push({ name: "order_by", value: JSON.stringify(orderBy) });
        var parameters = zooapp.filters[zooapp.objects].selected.concat(queryParameters);
        $.get("/results", parameters, function(data) {
            zooapp.results = data;
            internalPaginationArray(data.pages)
        });
        zooapp.showResults = true;
    }
    
    function internalToggleOrder(orderByObjectName) {
        var oldValue = zooapp[orderByObjectName].order
        if (oldValue == "ASC") zooapp[orderByObjectName].order = "DESC";
        if (oldValue == "DESC") zooapp[orderByObjectName].order = "ASC";
    }
    
    function internalPaginationArray(pages) {
        var start = [];
        var from = 1;
        var to = pages;
        var end = [];
        if (zooapp.page > 10) {
            start = ["..."];
            from = zooapp.page - 7;
        }
        if (pages - zooapp.page > 9) {
            to = zooapp.page + 7;
            end = ["..."];
        }
        zooapp.pagination = start.concat([...Array(to - from + 1).keys()].map(i => i + from)).concat(end);
    }
    
    function internalJumpPage(page) {
        var p = (Number.isInteger(page) ? page : Number(page))
        if (Number.isInteger(p) && (p > 0) && (p <= zooapp.results.pages)) {
            zooapp.page = p;
            internalDisplayResults();
        }
    }
    
    zooapp = new Vue({
        el: '#zooapp',
        methods: {
            isNull: function(value) { return value === null; },
            addFilterToSelected: function(filter) { internalSelectFilter(filter); },
            removeFilterFromSelected: function(filter) { internalRemoveFilter(filter); },
            submitFilterValue: function(filter) { internalSubmitFilterValue(filter); },
            editFilter: function(filter) { internalEditFilter(filter); },
            displayResults: function() { internalDisplayResults(); },
            propertyName: function(property) { return property.name.replace(/_/g, " "); },
            toggleOrder: function(orderByObjectName) { internalToggleOrder(orderByObjectName); },
            countObjects: function() { internalObjectCount(); }
        },
        data: {
            objects: "graphs",
            objectCount: null,
            objectsSelected: false,
            results: {"pages":10613,"data":[{"zooid":1,"bool":{"has_multiple_edges":false,"is_arc_transitive":true,"is_bipartite":false,"is_cayley":true,"is_distance_regular":true,"is_distance_transitive":true,"is_edge_transitive":true,"is_eulerian":false,"is_forest":false,"is_hamiltonian":true,"is_overfull":false,"is_partial_cube":false,"is_split":true,"is_strongly_regular":false,"is_tree":false,"is_moebius_ladder":true,"is_prism":false,"is_spx":false},"numeric":{"order":4,"clique_number":4,"connected_components_number":1,"diameter":1,"girth":3,"number_of_loops":0,"odd_girth":3,"size":6,"triangles_count":4}},{"zooid":2,"bool":{"has_multiple_edges":false,"is_arc_transitive":false,"is_bipartite":false,"is_cayley":true,"is_distance_regular":false,"is_distance_transitive":false,"is_edge_transitive":false,"is_eulerian":false,"is_forest":false,"is_hamiltonian":true,"is_overfull":false,"is_partial_cube":false,"is_split":false,"is_strongly_regular":false,"is_tree":false,"is_moebius_ladder":false,"is_prism":true,"is_spx":false},"numeric":{"order":6,"clique_number":3,"connected_components_number":1,"diameter":2,"girth":3,"number_of_loops":0,"odd_girth":3,"size":9,"triangles_count":2}},{"zooid":3,"bool":{"has_multiple_edges":false,"is_arc_transitive":true,"is_bipartite":true,"is_cayley":true,"is_distance_regular":true,"is_distance_transitive":true,"is_edge_transitive":true,"is_eulerian":false,"is_forest":false,"is_hamiltonian":true,"is_overfull":false,"is_partial_cube":false,"is_split":false,"is_strongly_regular":true,"is_tree":false,"is_moebius_ladder":true,"is_prism":false,"is_spx":false},"numeric":{"order":6,"clique_number":2,"connected_components_number":1,"diameter":2,"girth":4,"number_of_loops":0,"size":9,"triangles_count":0}},{"zooid":4,"bool":{"has_multiple_edges":false,"is_arc_transitive":false,"is_bipartite":false,"is_cayley":true,"is_distance_regular":false,"is_distance_transitive":false,"is_edge_transitive":false,"is_eulerian":false,"is_forest":false,"is_hamiltonian":true,"is_overfull":false,"is_partial_cube":false,"is_split":false,"is_strongly_regular":false,"is_tree":false,"is_moebius_ladder":true,"is_prism":false,"is_spx":false},"numeric":{"order":8,"clique_number":2,"connected_components_number":1,"diameter":2,"girth":4,"number_of_loops":0,"odd_girth":5,"size":12,"triangles_count":0}},{"zooid":5,"bool":{"has_multiple_edges":false,"is_arc_transitive":true,"is_bipartite":true,"is_cayley":true,"is_distance_regular":true,"is_distance_transitive":true,"is_edge_transitive":true,"is_eulerian":false,"is_forest":false,"is_hamiltonian":true,"is_overfull":false,"is_partial_cube":true,"is_split":false,"is_strongly_regular":false,"is_tree":false,"is_moebius_ladder":false,"is_prism":true,"is_spx":true},"numeric":{"order":8,"clique_number":2,"connected_components_number":1,"diameter":3,"girth":4,"number_of_loops":0,"size":12,"triangles_count":0}},{"zooid":6,"bool":{"has_multiple_edges":false,"is_arc_transitive":false,"is_bipartite":false,"is_cayley":true,"is_distance_regular":false,"is_distance_transitive":false,"is_edge_transitive":false,"is_eulerian":false,"is_forest":false,"is_hamiltonian":true,"is_overfull":false,"is_partial_cube":false,"is_split":false,"is_strongly_regular":false,"is_tree":false,"is_moebius_ladder":false,"is_prism":true,"is_spx":false},"numeric":{"order":10,"clique_number":2,"connected_components_number":1,"diameter":3,"girth":4,"number_of_loops":0,"odd_girth":5,"size":15,"triangles_count":0}},{"zooid":7,"bool":{"has_multiple_edges":false,"is_arc_transitive":false,"is_bipartite":true,"is_cayley":true,"is_distance_regular":false,"is_distance_transitive":false,"is_edge_transitive":false,"is_eulerian":false,"is_forest":false,"is_hamiltonian":true,"is_overfull":false,"is_partial_cube":false,"is_split":false,"is_strongly_regular":false,"is_tree":false,"is_moebius_ladder":true,"is_prism":false,"is_spx":false},"numeric":{"order":10,"clique_number":2,"connected_components_number":1,"diameter":3,"girth":4,"number_of_loops":0,"size":15,"triangles_count":0}},{"zooid":8,"bool":{"has_multiple_edges":false,"is_arc_transitive":true,"is_bipartite":false,"is_cayley":false,"is_distance_regular":true,"is_distance_transitive":true,"is_edge_transitive":true,"is_eulerian":false,"is_forest":false,"is_hamiltonian":false,"is_overfull":false,"is_partial_cube":false,"is_split":false,"is_strongly_regular":true,"is_tree":false,"is_moebius_ladder":false,"is_prism":false,"is_spx":false},"numeric":{"order":10,"clique_number":2,"connected_components_number":1,"diameter":2,"girth":5,"number_of_loops":0,"odd_girth":5,"size":15,"triangles_count":0}},{"zooid":9,"bool":{"has_multiple_edges":false,"is_arc_transitive":false,"is_bipartite":false,"is_cayley":true,"is_distance_regular":false,"is_distance_transitive":false,"is_edge_transitive":false,"is_eulerian":false,"is_forest":false,"is_hamiltonian":true,"is_overfull":false,"is_partial_cube":false,"is_split":false,"is_strongly_regular":false,"is_tree":false,"is_moebius_ladder":true,"is_prism":false,"is_spx":false},"numeric":{"order":12,"clique_number":2,"connected_components_number":1,"diameter":3,"girth":4,"number_of_loops":0,"odd_girth":7,"size":18,"triangles_count":0}},{"zooid":10,"bool":{"has_multiple_edges":false,"is_arc_transitive":false,"is_bipartite":false,"is_cayley":true,"is_distance_regular":false,"is_distance_transitive":false,"is_edge_transitive":false,"is_eulerian":false,"is_forest":false,"is_hamiltonian":true,"is_overfull":false,"is_partial_cube":false,"is_split":false,"is_strongly_regular":false,"is_tree":false,"is_moebius_ladder":false,"is_prism":false,"is_spx":false},"numeric":{"order":12,"clique_number":3,"connected_components_number":1,"diameter":3,"girth":3,"number_of_loops":0,"odd_girth":3,"size":18,"triangles_count":4}},{"zooid":11,"bool":{"has_multiple_edges":false,"is_arc_transitive":false,"is_bipartite":true,"is_cayley":true,"is_distance_regular":false,"is_distance_transitive":false,"is_edge_transitive":false,"is_eulerian":false,"is_forest":false,"is_hamiltonian":true,"is_overfull":false,"is_partial_cube":true,"is_split":false,"is_strongly_regular":false,"is_tree":false,"is_moebius_ladder":false,"is_prism":true,"is_spx":false},"numeric":{"order":12,"clique_number":2,"connected_components_number":1,"diameter":4,"girth":4,"number_of_loops":0,"size":18,"triangles_count":0}},{"zooid":12,"bool":{"has_multiple_edges":false,"is_arc_transitive":false,"is_bipartite":true,"is_cayley":true,"is_distance_regular":false,"is_distance_transitive":false,"is_edge_transitive":false,"is_eulerian":false,"is_forest":false,"is_hamiltonian":true,"is_overfull":false,"is_partial_cube":false,"is_split":false,"is_strongly_regular":false,"is_tree":false,"is_moebius_ladder":false,"is_prism":false,"is_spx":true},"numeric":{"order":12,"clique_number":2,"connected_components_number":1,"diameter":3,"girth":4,"number_of_loops":0,"size":18,"triangles_count":0}},{"zooid":13,"bool":{"has_multiple_edges":false,"is_arc_transitive":true,"is_bipartite":true,"is_cayley":true,"is_distance_regular":true,"is_distance_transitive":true,"is_edge_transitive":true,"is_eulerian":false,"is_forest":false,"is_hamiltonian":true,"is_overfull":false,"is_partial_cube":false,"is_split":false,"is_strongly_regular":false,"is_tree":false,"is_moebius_ladder":false,"is_prism":false,"is_spx":false},"numeric":{"order":14,"clique_number":2,"connected_components_number":1,"diameter":3,"girth":6,"number_of_loops":0,"size":21,"triangles_count":0}},{"zooid":14,"bool":{"has_multiple_edges":false,"is_arc_transitive":false,"is_bipartite":true,"is_cayley":true,"is_distance_regular":false,"is_distance_transitive":false,"is_edge_transitive":false,"is_eulerian":false,"is_forest":false,"is_hamiltonian":true,"is_overfull":false,"is_partial_cube":false,"is_split":false,"is_strongly_regular":false,"is_tree":false,"is_moebius_ladder":true,"is_prism":false,"is_spx":false},"numeric":{"order":14,"clique_number":2,"connected_components_number":1,"diameter":4,"girth":4,"number_of_loops":0,"size":21,"triangles_count":0}}]},
            page: 1,
            resultsOrderBy1: { column: "none", order: "ASC"},
            resultsOrderBy2: { column: "none", order: "ASC"},
            searchMessage: "Choose the type of objects to start.",
            showResults: true,
            pagination: [],
            pageJump: null,
            collections: {
                graphs: {
                    selected: ["VT", "CVT", "CAT"],
                    available: [
                        {name: "vertex transitive graphs",
                         id: "VT",
                         info: "up to 31 vertices", 
                         by: "Gordon Royle, Brendan McKay and Alexander Hulpke", 
                         url: "http://staffhome.ecm.uwa.edu.au/~00013890/remote/trans/index.html"},
                        {name: "cubic vertex transitive graphs", 
                         id: "CVT",
                         info: "up to 1280 vertices", 
                         by: "Primož Potočnik, Pablo Spiga and Gabriel Verret", 
                         url: "http://www.matapp.unimib.it/~spiga/census.html"},
                        {name: "cubic arc transitive graphs", 
                         id: "CAT",
                         info: "up to 2048 vertices", 
                         by: "Marston Conder", 
                         url: "https://www.math.auckland.ac.nz/~conder/symmcubic2048list.txt"}
                    ]
                },
                maniplexes: [
                    
                ]
            },
            filters: {
                graphs: {
                    selected: [],
                    available: [
                        {name: "chromatic_index", type: "numeric"},
                        {name: "clique_number", type: "numeric"},
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
                        {name: "is_forest", type: "bool"},
                        {name: "is_hamiltonian", type: "bool"},
                        {name: "is_moebius_ladder", type: "bool"}, // CVT
                        {name: "is_overfull", type: "bool"},
                        {name: "is_partial_cube", type: "bool"},
                        {name: "is_prism", type: "bool"}, // CVT
                        {name: "is_split", type: "bool"},
                        {name: "is_spx", type: "bool"}, // CVT
                        {name: "is_strongly_regular", type: "bool"},
                        {name: "is_tree", type: "bool"},
                        {name: "number_of_loops", type: "numeric"},
                        {name: "odd_girth", type: "numeric"},
                        {name: "order", type: "numeric"},
                        {name: "size", type: "numeric"},
                        {name: "triangles_count", type: "numeric"}
                    ]
                },
                maniplexes: {
                    selected: [],
                    available: []
                }
            },
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
        },
        watch: {
            objects: function (newObjects, oldObjects) {
                this.objectsSelected = !!this.objects && (typeof this.filters[this.objects] !== 'undefined');
                if (!this.objectsSelected) {
                    this.searchMessage = "Choose the type of objects to start.";
                }
                else {
                    internalObjectCount();
                    this.searchMessage = "";
                }
            },
            resultsOrderBy1: function(newOrder1, oldOrder1) {
                if (!newOrder1 || newOrder1 == "none") zooapp.resultsOrderBy2 = "none";
            }
        }
    });

}());
