import React, { Component } from 'react';
import { Container, Row, Col, Button, ButtonGroup, FormGroup, Label, Input } from 'reactstrap';

const dataFilters = {
    graphs: {
        has_multiple_edges: "bool",
        is_arc_transitive: "bool",
        is_bipartite: "bool",
        is_cayley: "bool",
        is_distance_regular: "bool",
        is_distance_transitive: "bool",
        is_edge_transitive: "bool",
        is_eulerian: "bool",
        is_forest: "bool",
        is_hamiltonian: "bool",
        is_moebius_ladder: "bool", // CVT
        is_overfull: "bool",
        is_partial_cube: "bool",
        is_prism: "bool", // CVT
        is_split: "bool",
        is_spx: "bool", // CVT
        is_strongly_regular: "bool",
        is_tree: "bool",
        chromatic_index: "numeric",
        clique_number: "numeric",
        connected_components_number: "numeric",
        diameter: "numeric",
        girth: "numeric",
        number_of_loops: "numeric",
        odd_girth: "numeric",
        order: "numeric",
        size: "numeric",
        triangles_count: "numeric"
    }
}

const dataCollections = {
    graphs: {
        VT: {id: "VT",
             name: "vertex transitive graphs",
             info: "up to 31 vertices", 
             by: "Gordon Royle, Brendan McKay and Alexander Hulpke", 
             url: "http://staffhome.ecm.uwa.edu.au/~00013890/remote/trans/index.html"},
        CVT: {id: "CVT",
              name: "cubic vertex transitive graphs", 
              info: "up to 1280 vertices", 
              by: "Primož Potočnik, Pablo Spiga and Gabriel Verret", 
              url: "http://www.matapp.unimib.it/~spiga/census.html"},
        CAT: {id: "CAT",
              name: "cubic arc transitive graphs", 
              info: "up to 2048 vertices", 
              by: "Marston Conder", 
              url: "https://www.math.auckland.ac.nz/~conder/symmcubic2048list.txt"}
    },
    maniplexes: {}
}

const getCollectionsKeys = (objects) => {
    if (objects === null) return [];
    else return Object.keys(dataCollections[objects]);
}

class ZooSearch extends Component {
    
    constructor(props) {
        super(props);
        this.state = {
            objects: null,
            collections: [],
            selectedFilters: "{}",
            counter: 0
        };
    }
    
    componentDidUpdate(pp, ps) {
        function postData(url = ``, data = {}) {
          // Default options are marked with *
            return fetch(url, {
                method: "POST", // *GET, POST, PUT, DELETE, etc.
//                mode: "cors", // no-cors, cors, *same-origin
//                cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
//                credentials: "same-origin", // include, *same-origin, omit
                headers: { "Content-Type": "application/json; charset=utf-8" },
//                redirect: "follow", // manual, *follow, error
//                referrer: "no-referrer", // no-referrer, *client
                body: JSON.stringify(data), // body data type must match "Content-Type" header
            })
            .then(response => response.json()); // parses response to JSON
        }
        
        function sortedKeys(o) {
            const keys = Object.keys(o);
            const sorted = keys.sort();
            const filtered = sorted.filter((f) => !(o[f] === null))
            return filtered;
        }
        const s = this.state
        const objectsCollectionsUpdate = (s.objects != ps.objects) || (s.collections != ps.collections);
        const filters = JSON.parse(s.selectedFilters);
        const keys = sortedKeys(filters);
        var selectedFiltersUpdate = false;
        
        if (s.selectedFilters != ps.selectedFilters) {
            const prevFilters = JSON.parse(ps.selectedFilters);
            const prevKeys = sortedKeys(prevFilters)
            if (keys.length != prevKeys.length) {
                selectedFiltersUpdate = true;
            }
            else {
                var i = 0;
                while (i < keys.length && !selectedFiltersUpdate) {
                    if (keys[i] != prevKeys[i]) selectedFiltersUpdate = true;
                    else if (filters[keys[i]] != prevFilters[keys[i]]) selectedFiltersUpdate = true;
                    i++;
                }
            }
        }
        
        if (objectsCollectionsUpdate || selectedFiltersUpdate) {
            var queryFilters = keys.map((k) => ({name: k, value: "" + filters[k]}))
            var queryJSON = {
                collections: s.collections,
                filters: queryFilters
            }
//            fetch('http://localhost:8080/')
//              .then(response => response.json())
//              .then(data => console.log({ data }));
//            console.log(queryJSON);
            postData('http://localhost:8080/count/' + s.objects, queryJSON).then(data => {
                this.setState({counter: data.value})
            }).catch(error => console.error(error));
        }
    }
    
    chooseObjects(newChosenObjects) {
        const previousChosenObjects = this.state.objects;
        if (newChosenObjects == previousChosenObjects) { return; }
        
        var newState = {
            objects: newChosenObjects,
            collections: getCollectionsKeys(newChosenObjects),
            selectedFilters: "{}",
            counter: 0
        }
        
        this.setState(newState);
    }
    
    toggleCollection(c) {
        var result = {};
        var newList = this.state.collections.slice(0); // clone
        var i = newList.indexOf(c);
            
        if (i > -1) newList.splice(i, 1);
        else newList.push(c);

        this.setState({collections: newList});
    }
    
    updateFilters(jsonString) {
        this.setState({selectedFilters: jsonString});
    }
    
    objectCount() {
//        var queryParameters = [];
//        console.log(this.props.)
//        queryParameters.push({ name: "collections", value: JSON.stringify(zooapp.collections[zooapp.objects].selected) });
//        var parameters = zooapp.filters[zooapp.objects].selected.concat(queryParameters);
//        $.get("/search/object-count", parameters, function(data) {
//            zooapp.objectCount = data;
//        });
        console.log("counting...")
    }
    
    render() {
        return (
            <section className="bg-primary" id="search">
                <Container id="zoo-search-box">
                    <Row>
                        <Col lg="3" md="3" sm="12" className="mx-auto my-4" id="select-type">
                            <h2 className="section-heading text-white" id="step2">Search</h2>
                            <ZooChooseObjects
                                objects={this.state.objects}
                                collections={this.state.collections}
                                chooseObjects={(o) => this.chooseObjects(o)}
                                toggleCollection={(c) => this.toggleCollection(c)} />
                        </Col>
                        <ZooFilters
                            objects={this.state.objects}
                            filters={this.state.selectedFilters}
                            callback={(s) => this.updateFilters(s)} />
                    </Row>
                    { !(this.state.objects === null) &&
                        <React.Fragment>
                            <hr className="my-2" />
                            <Row>
                                <Col lg="8" className="text-white">
                                    <p><Button className="mr-3" onClick={this.objectCount}>Display results</Button> Objects found: <i>{this.state.counter}</i>.</p>
                                </Col>
                            </Row>
                        </React.Fragment>
                    }
                </Container>
            </section>
        );
    }
}


/* * * * * * * * * * * * * * * * * * * * * * * * *
    Handling the choice of objects
 * * * * * * * * * * * * * * * * * * * * * * * * */
class ZooChooseObjects extends Component {
    
    renderButton(value, label) {
        return(
            // default color="secondary"
            <Button
                className={"zoo-radio-objects" + (this.props.objects == value ? " focus" : "")}
                onClick={() => this.props.chooseObjects(value)}>
                {label} <ZooInfoButton value="type" />
            </Button>
        );
    }
    
    render() {
        return(
            <React.Fragment>
                <ButtonGroup id="zoo-choose-objects">
                    {this.renderButton("graphs", "Graphs")}
                    {this.renderButton("maniplexes", "Maniplexes")}
                </ButtonGroup>
                <div className="mx-auto my-4">
                    {!(this.props.objects === null) &&
                        <ZooChooseCollections
                            objects={this.props.objects}
                            collections={this.props.collections}
                            toggle={(c) => this.props.toggleCollection(c)} />
                    }
                </div>
            </React.Fragment>
        );
    }
}

/* * * * * * * * * * * * * * * * * * * * * * * * *
    Handling the choice of collections
 * * * * * * * * * * * * * * * * * * * * * * * * */
class ZooChooseCollections extends Component {
    
    renderCollection(c) {
        const isChecked = this.props.collections.indexOf(c.id) > -1;
        return (
            <Label check key={c.id} className="text-white" onClick={() => this.props.toggle(c.id)}>
                <Input type="checkbox" defaultChecked={isChecked} /> {c.name}
            </Label>
        );
    }

    render() {
        var availableCollectionsKeys = getCollectionsKeys(this.props.objects)
        if (this.props.objects === null) { return <p className="text-white">Choose a type of objects to start.</p>; }
        if (availableCollectionsKeys.length > 0) {
            return(
                <FormGroup check>
                    {availableCollectionsKeys.map((key) => {
                        return this.renderCollection(dataCollections[this.props.objects][key]);
                    })}
                </FormGroup>
            );
        }
    }

}


/* * * * * * * * * * * * * * * * * * * * * * * * *
    Handling filters
    - - - - - - - - - - - - - - - -
    null: filter selected, no valid value
    true/false: boolean values (default = true)
    /^(=|<=|>=|<|>|<>|!=)(\d+\.?\d*)$/
 * * * * * * * * * * * * * * * * * * * * * * * * */
class ZooFilters extends Component {

    constructor(props) {
        super(props);
        this.addFilter = this.addFilter.bind(this);
    }
    
    getCurrentFilters() { return JSON.parse(this.props.filters); }
    updateFilters(filtersObject) { this.props.callback(JSON.stringify(filtersObject)); }
    
    addFilter(name) {
        var newState = this.getCurrentFilters();
        newState[name] = null;
        this.updateFilters(newState);
    }
    
    removeFilter(name) {
        var newState = this.getCurrentFilters();
        delete newState[name];
        this.updateFilters(newState);
    }
    
    updateFilterValue(name, value) {
        var newState = this.getCurrentFilters();
        newState[name] = value;
        this.updateFilters(newState);
    }
    
    renderAvailable(filters) {
        return(
            <div className="zoo-search-filter">
                <div className="zoo-filter-box">
                    <ul className="fa-ul">
                        {filters.map((f) => 
                            <li key={f} onClick={this.addFilter.bind(this, f)}>
                                <span className="fa-li"><i className="fas fa-plus"></i></span>
                                {f} <ZooInfoButton value="filter" />
                            </li>
                        )}
                    </ul>
                </div>
            </div>
        );
    }
    
    renderSelected(filters) {
        return(
            <div className="zoo-search-filter">
                <div className="zoo-filter-box">
                    {filters.length == 0 && <p className="text-center my-3">Select filters</p>}
                    <ul className="fa-ul">
                        {filters.map((f) => (
                            <SelectedFilter key={f.name}
                                name={f.name}
                                value={f.value}
                                type={dataFilters[this.props.objects][f.name]}
                                onDoneEditing={(v) => this.updateFilterValue(f.name, v)}
                                onRemoveFilter={() => this.removeFilter(f.name)}/>
                        ))}
                    </ul>
                </div>
            </div>
        );
    }
    
    render() {
        const display = !(this.props.objects === null)
        var selected = [];
        var available = [];
        if (display) {
            const current = this.getCurrentFilters(); 
            const all = Object.keys(dataFilters[this.props.objects]);
            const currentKeys = Object.keys(current);
            selected = currentKeys.map((k) => ({name: k, value: current[k]}));
            available = all.filter((i) => {return currentKeys.indexOf(i) < 0;})
        }
        return(
            <React.Fragment>
                <Col id="zoo-selected-filters" md="5" sm="7" className="mx-auto my-4">
                    {display && this.renderSelected(selected)}
                </Col>
                <Col id="zoo-choose-filters" md="4" sm="5" className="mx-auto my-4">
                    {display && this.renderAvailable(available)}
                </Col>
            </React.Fragment>
        );
    }
}


class SelectedFilter extends Component {
    
    constructor(props) {
        super(props);
        this.state = { edit: true, value: (this.props.type == "bool" ? true : "") };
        this.editFilter = this.editFilter.bind(this);
        this.toggleBooleanValue = this.toggleBooleanValue.bind(this);
        this.handleInputChange = this.handleInputChange.bind(this);
        this.validateAndUpdate = this.validateAndUpdate.bind(this);
    }
    
    editFilter() {
        this.setState({ edit: true });
    }
    
    toggleBooleanValue(newValue) {
        const previousValue = this.state.value;
        if (newValue == previousValue) { return; }
        this.setState({ value: newValue });
    }
    
    handleInputChange(e) {
        var change = {};
        this.setState({ value: e.target.value });
    }
    
    validateAndUpdate() {
        var valueValid = false;
        if (this.props.type == "bool") {
            valueValid = (this.state.value === true || this.state.value === false)
        }
        if (this.props.type == "numeric") {
            valueValid = /^(=|<=|>=|<|>|<>|!=)(\d+\.?\d*)$/.test(this.state.value.replace(/ /g, ''))
        }
        if (valueValid) {
            this.setState({ edit: false });
            this.props.onDoneEditing(this.state.value);
        }
    }
    
    renderEditCondition() {
        if (this.props.type == "bool") {
            return(
                <ButtonGroup id="zoo-choose-objects" className="zoo-bool-filter btn-group-sm">
                    <Button
                        className={(this.state.value ? "focus" : "")}
                        onClick={this.toggleBooleanValue.bind(this, true)}>True</Button>
                    <Button
                        className={(!this.state.value ? "focus" : "")}
                        onClick={this.toggleBooleanValue.bind(this, false)}>False</Button>
                </ButtonGroup>
            );
        }
        else {
            return (
                <input className="zoo-numeric-filter" type="text"
                    onChange={this.handleInputChange.bind(this)}
                    value={this.state.value} />
            );
        }
    }
    
    render() {
        return(
            <li key={this.props.name} className={(this.state.edit ? "edit" : "")}>
                { (this.state.value === false && (!this.state.edit)) && <i>not </i> }
                {this.props.name} <ZooInfoButton value="filter" />
                { !this.state.edit && <i className="zoo-numeric-condition-display">{this.state.value} </i> }
                { this.state.edit && this.renderEditCondition() }
                <span className="text-muted small">
                    <span className="remove-button" onClick={this.props.onRemoveFilter}><i className="fas fa-minus"></i></span>
                    <span className="done-button" onClick={this.validateAndUpdate.bind(this)}><i className="fas fa-check"></i></span>
                    <span className="edit-button" onClick={this.editFilter.bind(this)}><i className="fas fa-pen"></i></span>
                </span>
            </li>
        );
    }
}


function ZooInfoButton(props) {
    return(
        <a href="#!" className={"info-" + props.value}>
            <i className="far fa-question-circle" data-fa-transform="shrink-4 up-3"></i>
        </a>
    );
}

export default ZooSearch;