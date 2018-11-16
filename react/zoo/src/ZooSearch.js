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

class ZooSearch extends Component {
    
    render() {
        const s = this.props.state;
        return (
            <section className="bg-primary" id="search">
                <Container id="zoo-search-box">
                    <Row>
                        <Col lg="3" md="3" sm="12" className="mx-auto my-4" id="select-type">
                            <h2 className="section-heading text-white" id="step2">Search</h2>
                            <ZooChooseObjects 
                                callbacks={this.props.callbacks}
                                objects={s.objects} />
                        </Col>
                        <ZooFilters objects={s.objects} />
                    </Row>
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
                onClick={() => this.props.callbacks.chooseObjects(value)}>
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
                    {!(this.props.objects === null) && <ZooChooseCollections collections={dataCollections[this.props.objects]} />}
                </div>
            </React.Fragment>
        );
    }
}

/* * * * * * * * * * * * * * * * * * * * * * * * *
    Handling the choice of collections
 * * * * * * * * * * * * * * * * * * * * * * * * */
class ZooChooseCollections extends Component {
    
    constructor(props) {
        super(props);
        this.availableCollectionsKeys = Object.keys(this.props.collections);
        this.state = { collections: this.availableCollectionsKeys };
    }
    
    renderCollection(c) {
        const isChecked = this.state.collections.indexOf(c.id) > -1;
        return (
            <Label check key={c.id} className="text-white" onClick={() => this.chooseCollections(c.id)}>
                <Input type="checkbox" defaultChecked={isChecked} /> {c.name}
            </Label>
        );
    }

    chooseCollections(c) {
        var result = {};
        var newList = this.state.collections.slice(0); // clone
        var i = newList.indexOf(c);
            
        if (i > -1) newList.splice(i, 1);
        else newList.push(c);

        this.setState({collections: newList});
    }

    render() {
        if (this.props.isClean) { return <p className="text-white">Choose a type of objects to start.</p>; }
        if (this.availableCollectionsKeys.length > 0) {
            return(
                <FormGroup check>
                    {this.availableCollectionsKeys.map((key) => {
                        return this.renderCollection(this.props.collections[key]);
                    })}
                </FormGroup>
            );
        }
    }

}


/* * * * * * * * * * * * * * * * * * * * * * * * *
    Handling filters
    - - - - - - - - - - - - - - - -
    null: filter is not selected
    true/false: boolean values (default = true)
    /^(=|<=|>=|<|>|<>|!=)(\d+\.?\d*)$/
 * * * * * * * * * * * * * * * * * * * * * * * * */
class ZooFilters extends Component {
    
    constructor(props) {
        super(props);
        this.state = {};
        this.addFilter = this.addFilter.bind(this);
    }
    
    componentWillReceiveProps(nextProps) {
        if (nextProps.objects !== this.props.objects) {
            var df = dataFilters[nextProps.objects]
            var numberOfFilters = df.length;
            var filters = {};
            for (var property in df) {
                if (df.hasOwnProperty(property)) {
                    filters[property] = null;
                }
            }
            this.setState(filters);
        }
    }
    
    addFilter(name) {
        var newState = {};
        newState[name] = (dataFilters[this.props.objects][name] == "bool" ? true : "")
        this.setState(newState);
    }
    
    removeFilter(name) {
        var newState = {};
        newState[name] = null;
        this.setState(newState);
    }
    
    updateFilterValue(name, value) {
        var newState = {};
        newState[name] = value;
        this.setState(newState);
    }
    
    renderFilter(name, selected) {
        const value = this.state[name];
        if (selected) {
            return (
                <SelectedFilter key={name} name={name}
                    type={dataFilters[this.props.objects][name]}
                    onDoneEditing={(value) => this.updateFilterValue(name, value)}
                    onRemoveFilter={() => this.removeFilter(name)}/>
            );
        }
        else {
            return(
                <li key={name} onClick={this.addFilter.bind(this, name)}>
                    <span className="fa-li"><i className="fas fa-plus"></i></span>
                    {name} <ZooInfoButton value="filter" />
                </li>
            );
        }
    }

    renderBox(displayingSelected) {
        if (this.props.objects === null) return;
        const currentAvailableFilters = dataFilters[this.props.objects];
        const showFilter = (f) => !(this.state[f] === null) == displayingSelected
        const filters = Object.keys(currentAvailableFilters).filter(showFilter)
        var displaySelectFiltersMessage = displayingSelected && (filters.length == 0);
        return(
            <div className="zoo-search-filter">
                <div className="zoo-filter-box">
                    {displaySelectFiltersMessage && <p className="text-center my-3">Select filters</p>}
                    <ul className="fa-ul">
                        {filters.map((f) => this.renderFilter(f, displayingSelected))}
                    </ul>
                </div>
            </div>
        );
    }
    
    render() {
        var display = !(this.props.objects === null);
        return(
            <React.Fragment>
                <Col id="zoo-selected-filters" md="5" sm="7" className="mx-auto my-4">
                    {display && this.renderBox(true)}
                </Col>
                <Col id="zoo-choose-filters" md="4" sm="5" className="mx-auto my-4">
                    {display && this.renderBox(false)}
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
            console.log("valid value is: " + this.state.value)
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