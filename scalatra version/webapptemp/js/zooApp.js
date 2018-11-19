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
        $.get("/search/object-count", zooapp.filters[zooapp.objects].selected, function(data) {
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
        var searchParameters = [];
        var orderBy = [];
        if (zooapp.resultsOrderBy1 && zooapp.resultsOrderBy1.column != "none") {
            orderBy.push(zooapp.resultsOrderBy1);
            if (zooapp.resultsOrderBy2 && zooapp.resultsOrderBy2.column != "none")
                orderBy.push(zooapp.resultsOrderBy2);
        }
        searchParameters.push({ name: "display_page", value: zooapp.page });
        searchParameters.push({ name: "order_by", value: JSON.stringify(orderBy) });
        $.get("/results", zooapp.filters[zooapp.objects].selected.concat(searchParameters), function(data) {
            zooapp.results = data;
        });
        zooapp.showResults = true;
    }
    
    function internalToggleOrder(orderByObjectName) {
        var oldValue = zooapp[orderByObjectName].order
        if (oldValue == "ASC") zooapp[orderByObjectName].order = "DESC";
        if (oldValue == "DESC") zooapp[orderByObjectName].order = "ASC";
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
            toggleOrder: function(orderByObjectName) { internalToggleOrder(orderByObjectName); }
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
