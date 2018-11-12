import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import ZooSearch from './ZooSearch';

import ReactDOM from 'react-dom'

class ZooApp extends Component {
    render() {
        return(
            <React.Fragment>
                <ZooHeader />
                <ZooSearch />
                <ZooFooter />
            </React.Fragment>
        );
    }
}

class ZooHeader extends Component {
    render() {
        return(
            <header id="introduction" className="intro-toggle show">
                <div className="container jumbotron">
                    <div className="row justify-content-center">
                        <div className="col-lg-8 text-center">
                            <button type="button" className="close" aria-label="Close" data-toggle="collapse" data-target=".intro-toggle" role="button" aria-expanded="true" aria-controls="introduction brand">
                                <span aria-hidden="true">&times;</span>
                            </button>
                            <p><img src="%PUBLIC_URL%/img/logo.svg" width="40%" /></p>
                            <p className="small text-muted">A repository of discrete objects</p>
                            <p className="lead mt-4">Various censuses of graphs and other objects exist on the web, and DiscreteZOO aims to give them a common home and to make them easily accessible and searchable.</p>
                            <hr className="my-4" />
                            <p className="small text-muted">
                                <a href=".intro-toggle" data-toggle="collapse" role="button" aria-expanded="true" aria-controls="introduction brand">Try it</a> out or take a short guided tour.
                            </p>
                            <p>
                                <a className="btn btn-highlight btn-lg" href=".intro-toggle" data-toggle="collapse" role="button" aria-expanded="true" aria-controls="introduction brand" role="button" href="javascript:void(0);" onclick="startIntro();">Start tour</a>
                            </p>
                        </div>
                    </div>
                </div>
            </header>
        );
    }
}

class ZooFooter extends Component {
    render() {
        return(
            <footer className="bg-dark" id="footer">
                <div className="container">
				    <div className="row">
					   <div className="col-lg-4 my-5 text-white">
                            <div id="licence">
                                <a className="icon-link fa-2x fa-pull-left" href="https://creativecommons.org/licenses/by/4.0/">
                                    <i className="fab fa-creative-commons"></i>
                                </a>
                                <p>This work is licensed under a Creative Commons Attribution 4.0 International License</p>
                            </div>
                        </div>
                        <div className="col-lg-4 my-5 text-white">
                        </div>
                        <div className="col-lg-4 my-5 text-white">
                            <h2>Contact</h2>
                        </div>
                    </div>
                </div>
            </footer>
        );
    }
}

export default ZooApp;