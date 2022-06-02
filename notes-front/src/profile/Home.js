import React, {Component} from "react";
import "./Home.css"

export default class Home extends Component {

    render() {
        return (
            <div className="container">
                <h2>Welcome to my notes app!</h2>
                <br/>
                <h3> Github: <a href="https://github.com/jakubolek">Jakub Olek</a></h3>
            </div>
        );
    }
}