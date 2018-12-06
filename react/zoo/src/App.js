import React, { Component } from 'react';
import './App.css';
import ZooSearch from './ZooSearch';
import ZooResults from './ZooResults';
import { Jumbotron, Container, Row, Col, Button } from 'reactstrap';

//import ReactDOM from 'react-dom'

class ZooApp extends Component {
    
    constructor(props) {
        super(props);
        this.state = {
            objects: null,
            results: null
        };
        this.passObjects = (objects) => { this.setState({objects: objects}); }
        this.passResults = (results) => { this.setState({results: results}); }
    }
    
    render() {
        return(
            <React.Fragment>
                <ZooSearch 
                    objects={this.state.objects}
                    api={this.props.api} 
                    passObjects={this.passObjects} 
                    passResults={this.passResults}
                />
                {!(this.state.results === null) && 
                    <ZooResults objects={this.state.objects} results={this.state.results} />
                }
                <ZooFooter />
            </React.Fragment>
        );
    }

}

function ZooHeader() {
    return(
        <header id="introduction" className="intro-toggle show">
            <Jumbotron>
                <Row className="justify-content-center">
                    <Col lg="8" className="text-center">
                        <Button type="button" className="close" aria-label="Close" data-toggle="collapse" data-target=".intro-toggle" role="button" aria-expanded="true" aria-controls="introduction brand">
                            <span aria-hidden="true">&times;</span>
                        </Button>
                        <p><img src="%PUBLIC_URL%/img/logo.svg" width="40%" alt="DiscreteZOO logo" /></p>
                        <p className="small text-muted">A repository of discrete objects</p>
                        <p className="lead mt-4">Various censuses of graphs and other objects exist on the web, and DiscreteZOO aims to give them a common home and to make them easily accessible and searchable.</p>
                        <hr className="my-4" />
                        <p className="small text-muted">
                            <a href=".intro-toggle" data-toggle="collapse" role="button" aria-expanded="true" aria-controls="introduction brand">Try it</a> out or take a short guided tour.
                        </p>
                        <p>
                            <a className="btn btn-highlight btn-lg" href=".intro-toggle" data-toggle="collapse" role="button" aria-expanded="true" aria-controls="introduction brand">Start tour</a>
                        </p>
                    </Col>
                </Row>
            </Jumbotron>
        </header>
    );
}

function ZooFooter() {
    return(
<footer className="bg-dark" id="footer">
            <Container>
                <Row>
                    <Col lg="4" className="my-5 text-white">
                    </Col>
                    <Col lg="4" className="my-5 text-white">
                    </Col>
                    <Col lg="4" className="my-5 text-white">
                        
                    </Col>
                </Row>
            </Container>
        </footer>
    );
    /*                                <div id="licence">
                            <a className="icon-link fa-2x fa-pull-left" href="https://creativecommons.org/licenses/by/4.0/">
                                <i className="fab fa-creative-commons"></i>
                            </a>
                            <p>This work is licensed under a Creative Commons Attribution 4.0 International License</p>
                        </div>*/
}

export default ZooApp;