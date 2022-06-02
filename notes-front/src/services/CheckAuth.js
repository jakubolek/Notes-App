import jwt from 'jwt-decode'

export default async function checkAuth() {

    const accessToken = localStorage.getItem('access_token');
    const decodeToken = jwt(accessToken);
    const date = new Date();

    const axios = require('axios');
    const API_URL = "http://localhost:8080/api/auth/token/refresh";
    const refreshToken = localStorage.getItem('refresh_token');


    if (decodeToken.exp * 1000 < date.getTime()) {
        const config = {
            method: 'get',
            url: API_URL,
            headers: {
                "Authorization": "Bearer " + refreshToken
            },
        };

        await axios(config)
            .then(function (response) {
                if (response.status === 200) {
                    localStorage.setItem("access_token", response.data.access_token);
                }
            })
            .catch(function (error) {
                console.log(error);
            });
    }

}




