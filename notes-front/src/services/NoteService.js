import authHeader from "./AuthHeader"
import checkAuth from "../services/CheckAuth";

const API_URL = "http://localhost:8080/api/notes/";

class NoteService {

    async saveNote(text) {
        await checkAuth();
        const axios = require('axios');
        const data = JSON.stringify({
            "note": text
        });

        const config = {
            method: "post",
            url: API_URL,
            headers: authHeader(),
            data: data
        };

        return axios(config);
    }

    async getNotes() {
        await checkAuth();

        const axios = require('axios');

        const config = {
            method: "get",
            url: API_URL,
            headers: authHeader()
        };

        return axios(config).then(function (response) {
            return response.data;
        })
    }

    async deleteNote(id) {
        await checkAuth();

        const axios = require('axios');

        const config = {
            method: 'delete',
            url: API_URL + id,
            headers: authHeader()
        };

        return axios(config);
    }

    async updateNote(id, text) {
        await checkAuth();

        const axios = require('axios');
        const data = JSON.stringify({
            "text": text
        });

        const config = {
            method: "put",
            url: API_URL + id,
            headers: authHeader(),
            data: data
        };

        return axios(config);
    }
}

export default new NoteService();