import React, {Component} from "react";
import LoginAlert from "./LoginAlert";
import AuthService from "../services/AuthService";
import {Link} from "react-router-dom";
import "./Login.css"

export default class Login extends Component {

    constructor(props) {
        super(props);

        this.state = {username: "", password: "", message: ""};

        this.handleChange = this.handleChange.bind(this);
        this.handleLogin = this.handleLogin.bind(this);
        this.loginAlert = React.createRef();
    }

    handleChange(e) {
        this.setState({
            [e.target.name]: e.target.value
        });
    }

    handleLogin(e) {
        const {username, password} = this.state;
        e.preventDefault()

        AuthService.login(username, password).then(function (response) {
            if (response === 200) {
                this.props.history.push("/home");
                window.location.reload();
            } else if (response === 403) {
                this.showLoginAlert("danger", "Incorrect username or password", "Please try again.");

            } else {
                this.showLoginAlert("danger", "Something went wrong", "Please try again.");
            }

        }.bind(this))


    }

    showLoginAlert(variant, heading, message) {
        this.loginAlert.current.setVariant(variant);
        this.loginAlert.current.setHeading(heading);
        this.loginAlert.current.setMessage(message);
        this.loginAlert.current.setVisible(true);
    }

    render() {
        return (
            <>
                <div className="login">
                    <form onSubmit={this.handleLogin}>
                        <h1>SIGN IN</h1>

                        <div>
                            <label>Username:</label>
                            <input type="text" autoFocus name="username" placeholder="Username"
                                   value={this.state.username}
                                   onChange={this.handleChange}/>
                        </div>

                        <div>
                            <label>Password:</label>
                            <input type="password" name="password" placeholder="********"
                                   value={this.state.password}
                                   onChange={this.handleChange}/>
                            <button className="submitLogin" type="submit"> Login</button>
                        </div>

                        <p>Don't have an account?</p>
                        <Link to={"/register"}>
                            <button className="signUp">Sign up</button>
                        </Link>
                    </form>
                </div>
                <LoginAlert ref={this.loginAlert}/>
            </>
        );
    }
}

