export default function authHeader() {
    const token = localStorage.getItem('access_token');

    if (token != null) {
        return {
            "Authorization": "Bearer " + token,
            "Content-Type": "application/json"
        };
    }
}