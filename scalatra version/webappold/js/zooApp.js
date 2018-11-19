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
                zooapp.filters[zooapp.objects].selected.push({ name: f.name, display: f.display, type: f.type, value: null, edit: true });
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
        queryParameters.push({ name: "objects", value: zooapp.objects });
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
        queryParameters.push({ name: "objects", value: zooapp.objects });
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
    
    function internalDisplaySymmetryType(st) {
        return "{" + st.split("-").join(", ") + "}";
    }
    
    function internalDisplayValue(r, type, column) {
        var value = r[type][column];
        return ((type == "string" && column == "SYMMETRY_TYPE") ? internalDisplaySymmetryType(value) : value);
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
            toggleOrder: function(orderByObjectName) { internalToggleOrder(orderByObjectName); },
            countObjects: function() { internalObjectCount(); },
            goToPage: function(page) { internalJumpPage(page); },
            displayValue: function(r, type, column) { return internalDisplayValue(r, type, column); }
        },
        data: {
            objects: null,
            objectCount: null,
            objectsSelected: false,
            results: {
                pages: null,
                data: null
            },
            page: 1,
            resultsOrderBy1: { column: "none", order: "ASC"},
            resultsOrderBy2: { column: "none", order: "ASC"},
            searchMessage: "Choose the type of objects to start.",
            showResults: false,
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
                maniplexes: {
                    selected: ["M2"],
                    available: [
                        {name: "2-orbit maniplexes",
                         id: "M2",
                         info: "of rank 3, up to 300 flags", 
                         by: "Katja Berčič, Daniel Pellicer", 
                         url: null}
                    ]
                }
            },
            filters: {
                graphs: {
                    selected: [],
                    available: [
                        {name: "chromatic_index", display: "chromatic index", type: "numeric"},
                        {name: "clique_number", display: "clique number", type: "numeric"},
                        {name: "connected_components_number", display: "connected components number", type: "numeric"},
                        {name: "diameter", display: "diameter", type: "numeric"},
                        {name: "girth", display: "girth", type: "numeric"},
                        {name: "has_multiple_edges", display: "has multiple edges", type: "bool"},
                        {name: "is_arc_transitive", display: "is arc transitive", type: "bool"},
                        {name: "is_bipartite", display: "is bipartite", type: "bool"},
                        {name: "is_cayley", display: "is cayley", type: "bool"},
                        {name: "is_distance_regular", display: "is distance_regular", type: "bool"},
                        {name: "is_distance_transitive", display: "is distance transitive", type: "bool"},
                        {name: "is_edge_transitive", display: "is edge transitive", type: "bool"},
                        {name: "is_eulerian", display: "is eulerian", type: "bool"},
                        {name: "is_forest", display: "is forest", type: "bool"},
                        {name: "is_hamiltonian", display: "is hamiltonian", type: "bool"},
                        {name: "is_moebius_ladder", display: "is moebius ladder", type: "bool"}, // CVT
                        {name: "is_overfull", display: "is overfull", type: "bool"},
                        {name: "is_partial_cube", display: "is partial cube", type: "bool"},
                        {name: "is_prism", display: "is prism", type: "bool"}, // CVT
                        {name: "is_split", display: "is split", type: "bool"},
                        {name: "is_spx", display: "is SPX", type: "bool"}, // CVT
                        {name: "is_strongly_regular", display: "is strongly regular", type: "bool"},
                        {name: "is_tree", display: "is tree", type: "bool"},
                        {name: "number_of_loops", display: "number of loops", type: "numeric"},
                        {name: "odd_girth", display: "odd girth", type: "numeric"},
                        {name: "order", display: "order", type: "numeric"},
                        {name: "size", display: "size", type: "numeric"},
                        {name: "triangles_count", display: "triangles count", type: "numeric"}
                    ]
                },
                maniplexes: {
                    selected: [],
                    available: [
                        {name: "ORBITS", display: "number of orbits", type: "numeric"},
                        {name: "IS_POLYTOPE", display: "is polytope", type: "bool"},
                        {name: "IS_REGULAR", display: "is regular", type: "bool"},
                        {name: "SMALL_GROUP_ORDER", display: "group order", type: "numeric"},
                        {name: "SYMMETRY_TYPE", display: "symmetry type", type: "string"}
                    ]
                }
            },
            columns: {
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
        },
        watch: {
            objects: function (newObjects, oldObjects) {
                this.objectsSelected = !!this.objects && (typeof this.filters[this.objects] !== 'undefined');
                if (!this.objectsSelected) {
                    this.searchMessage = "Choose the type of objects to start.";
                }
                else {
                    zooapp.showResults = false;
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
