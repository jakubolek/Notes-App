import React, {Component} from "react";
import Login from "./login/Login";
import Registration from "./registration/Registration";
import {Link, Route, Switch,} from "react-router-dom";

import AuthService from "./services/AuthService";
import Home from "./profile/Home";
import Note from "./notes/Note";
import "./App.css"

class App extends Component {

    constructor(props) {
        super(props);
        this.logOut = this.logOut.bind(this);
        this.state = {
            currentUser: ""
        }
    }

    componentDidMount() {
        const user = AuthService.getCurrentUser();

        if (user) {
            this.setState({
                currentUser: user
            })
        }
    }

    logOut() {
        AuthService.logout();
        this.setState({
            currentUser: "",
        });
    }

    render() {
        const {currentUser} = this.state;

        return (
            <div>
                <div className="menu">
                    {currentUser ? (
                        <div className="authUser">
                            <li className="tools">
                                <Link to={"/home"} className="link">
                                    <h5>{currentUser}</h5>
                                </Link>
                            </li>

                            <li className="tools">
                                <Link to={"/notes"} className="link">
                                    Note List
                                </Link>
                            </li>

                            <li className="tools">
                                <Link to={"/login"} className="link" onClick={this.logOut}>
                                    Logout
                                </Link>
                            </li>

                        </div>

                    ) : (<div className="notAuthUser">
                        <Link to={"/"} className="brand">
                            <h5>NotesApp</h5>
                        </Link>

                        <li className="tools">
                            <Link to={"/login"} className="link">
                                Login
                            </Link>
                        </li>

                        <li className="tools">
                            <Link to={"/register"} className="link">
                                Register
                            </Link>
                        </li>
                    </div>)

                    }
                </div>
                {currentUser ? (
                    <div className="route">
                        <Switch>
                            <Route exact path={["/", "/home"]} component={Home}/>
                            <Route exact path="/notes" component={Note}/>
                        </Switch>
                    </div>

                ) : (
                    <div className="route">
                        <Switch>
                            <Route exact path={["/", "/home"]} component={Home}/>
                            <Route exact path="/login" component={Login}/>
                            <Route exact path="/register" component={Registration}/>
                            <Route exact path="/notes" component={Login}/>
                        </Switch>
                    </div>

                )}
            </div>
        );
    }
}

export default App;