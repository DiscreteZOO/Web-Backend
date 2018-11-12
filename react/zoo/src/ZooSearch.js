import React, { Component } from 'react';

class ZooSearch extends Component {
    render() {
        return (
            <section class="bg-primary" id="search">
                <div class="container" id="zoo-search-box">
                    <div className="row">
                        <div className="col-lg-3 col-md-3 col-sm-12 mx-auto my-4" id="select-type">
                            <h2 className="section-heading text-white" id="step2">Search</h2>
                            <ZooChooseObjects />
                        </div>
                    </div>
                </div>
            </section>
        );
    }
}

class ZooChooseObjects extends Component {   
    render() {
        return(
            <div className="btn-group btn-group-toggle" id="zoo-choose-objects">
                <ZooChooseObjectsButton value="graphs" label="Graphs" />
                <ZooChooseObjectsButton value="maniplexes" label="Maniplexes" />
            </div>
        );
    }
}

class ZooChooseObjectsButton extends Component {
    render() {
        return(
            <label className="btn btn-secondary zoo-radio-objects" id="{this.props.value}">
                <input type="radio" name="objects" value="{this.props.value}" autocomplete="off" />
                {this.props.label}
                <a href="#!" className="type-info"><i className="far fa-question-circle" data-fa-transform="shrink-4 up-3"></i></a>
            </label>
        );
    }
}

class ZooSearchMessage extends Component {
    render() {
        return(
            <div class="mx-auto my-4" id="zoo-message">
                <p class="text-white">{this.props.message}</p>
                <div>
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" />
                        <label class="form-check-label text-white">{this.props.name}</label>
                    </div>
                </div>
            </div>
        );
    }
}

class ZooSelectedFilters extends Component {
    render() {
        return(
            <div className="col-lg-5 col-md-5 col-sm-7 mx-auto my-4" id="zoo-selected-filters">
                <div className="zoo-search-filter">
                    <div className="zoo-filter-box">
                        <p className="text-center my-3">Select filters</p>
                        <ul className="fa-ul">
                            
                        </ul>
                    </div>
                </div>
            </div>
        );
    }
}

class ZooSelectedFiltersItem extends Component {
    render() {
        return(
            <li>
                <i>not </i>
                {this.props.name}
                <a href="#!" class="filter-info"><i class="far fa-question-circle" data-fa-transform="shrink-4 up-3"></i></a>
                <i class="zoo-numeric-condition-display">{this.props.value} </i>

                <div class="text-muted small">
                    <span class="remove-button"><i class="fas fa-minus"></i></span>
                    <span class="done-button"><i class="fas fa-check"></i></span>
                    <span class="edit-button"><i class="fas fa-pen"></i></span>
                </div>
                <ZooFiltersBooleanCondition value="true" label="True" />
            </li>
        );
    }
}

class ZooFiltersBooleanCondition extends Component {
    render() {
        return(
            <div className="zoo-bool-filter btn-group btn-group-toggle btn-group-sm" id="zoo-choose-objects">
                <label class="btn btn-secondary">
                    <input type="radio" value="{this.props.value}" autocomplete="off" />
                    {this.props.label}
                </label>
            </div>
        );
    }
}

class ZooFiltersNumericCondition extends Component {
    rendr() {
        return(
            <input class="zoo-numeric-filter" type="text" value="{this.props.value}" />
        );
    }
}

export default ZooSearch;