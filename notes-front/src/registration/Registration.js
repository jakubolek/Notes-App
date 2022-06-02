import React, {Component} from "react";
import AuthService from "../services/AuthService";
import validator from "validator/es";
import RegistrationAlert from "./RegistrationAlert";
import {Link} from "react-router-dom";
import "./Registration.css"

const defaultState = {
    username: "",
    email: "",
    password: "",
    usernameError: "",
    emailError: "",
    passwordError: ""
}

export default class Registration extends Component {

    state = defaultState;

    constructor(props) {
        super(props);

        this.handleChange = this.handleChange.bind(this);
        this.handleRegister = this.handleRegister.bind(this);
        this.registerAlert = React.createRef();

    }

    handleChange(e) {
        this.setState({
            [e.target.name]: e.target.value
        });
    }

    validate() {
        const {username, email, password} = this.state;
        const regex = /^\S*$/;

        let usernameError = "";
        let emailError = "";
        let passwordError = "";

        if (username.length < 4 || username.length > 30 || !regex.test(username)) {
            usernameError = "Username must be between 4 and 30 characters. " +
                "You can use letters, numbers, periods."
        }

        if (!validator.isEmail(email)) {
            emailError = "Invalid email.";
        }

        if (password.length < 4 || password.length > 30 || !regex.test(password)) {
            passwordError = "Password must be between 4 and 30 characters. " +
                "You can use letters, numbers, periods."
        }

        if (usernameError || emailError || passwordError) {
            this.setState({usernameError: usernameError, emailError: emailError, passwordError: passwordError});
            return false;
        }

        return true;
    }


    handleRegister(e) {
        const {username, email, password} = this.state;
        const isValid = this.validate();
        e.preventDefault()

        if (isValid) {

            AuthService.register(username, email, password).then(
                (response) => {
                    if (response === 201) {
                        setTimeout(() => {
                            this.props.history.push('/login');
                            window.location.reload();
                        }, 5000)
                        this.showRegistrationAlert("success", "Successfully Registered", "Now you can sign in");

                    } else if (response === 409) {
                        this.showRegistrationAlert("danger", "This username is already taken", "Please try another one.");
                    }
                }
            );
        }
    }


    showRegistrationAlert(variant, heading, message) {
        this.registerAlert.current.setVariant(variant);
        this.registerAlert.current.setHeading(heading);
        this.registerAlert.current.setMessage(message);
        this.registerAlert.current.setVisible(true);
    }

    render() {
        return (
            <>
                <div className="register">
                    <form onSubmit={this.handleRegister}>
                        <h1>SIGN UP</h1>

                        <div>
                            <label>Username:</label>
                            <input type="text" autoFocus name="username" placeholder="Username"
                                   value={this.state.username}
                                   onChange={this.handleChange}/>
                            <div className="error">{this.state.usernameError}</div>

                        </div>

                        <div>
                            <label>Email:</label>
                            <input type="text" name="email" placeholder="email@domain.com"
                                   value={this.state.email}
                                   onChange={this.handleChange}/>
                            <div className="error">{this.state.emailError}</div>

                        </div>

                        <div>
                            <label>Password:</label>
                            <input type="password" name="password" placeholder="*********"
                                   value={this.state.password}
                                   onChange={this.handleChange}/>
                            <div className="error">{this.state.passwordError}</div>

                        </div>
                        <button className="submitRegister" type="submit"> Register</button>

                        <p>Already have an account?</p>
                        <Link to={"/login"}>
                            <button className="signIn">Sign in</button>
                        </Link>

                    </form>
                </div>
                <RegistrationAlert ref={this.registerAlert}/>
            </>
        );
    }
}

