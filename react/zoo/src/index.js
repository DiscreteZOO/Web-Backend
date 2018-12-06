import React from 'react';
import ReactDOM from 'react-dom';
// import './index.css';
import ZooApp from './App';
import * as serviceWorker from './serviceWorker';

ReactDOM.render(<ZooApp />, document.getElementById('zooapp'));
const api = window.location.hostname === "localhost" ? "http://localhost:8080" : "https://api.discretezoo.xyz";

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: http://bit.ly/CRA-PWA
serviceWorker.unregister();
