const API_URL = "http://localhost:8080/api/auth/";

class AuthService {

    login(username, password) {

        const axios = require('axios');
        const qs = require('qs');
        const data = qs.stringify({
            'username': username,
            'password': password
        });
        const config = {
            method: 'post',
            url: 'http://localhost:8080/api/auth/login',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            data: data
        };

        return axios(config)
            .then(function (response) {
                if (response.status === 200) {
                    localStorage.setItem("user", username);
                    localStorage.setItem("access_token", response.data.access_token);
                    localStorage.setItem("refresh_token", response.data.refresh_token);
                }
                return response.status
            })
            .catch(function (error) {
                return error.response.status
            });

    }

    logout() {
        localStorage.removeItem("user");
    }

    register(username, email, password) {

        const axios = require('axios');
        const data = {
            "username": username,
            "email": email,
            "password": password
        };

        const config = {
            method: 'post',
            url: API_URL + "users",
            headers: {
                'Content-Type': 'application/json'
            },
            data: data
        };

        return axios(config)
            .then(function (response) {
                return response.status;
            })
            .catch(function (error) {
                return error.response.status;
            });
    }

    getCurrentUser() {
        return localStorage.getItem('user');
    }
}

export default new AuthService();