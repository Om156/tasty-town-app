import axios from "axios";

const API_URL = "http://localhost:1200/api/auth";

export const login = async (data) => {
    try {
        const response = await axios.post(API_URL + "/login", data);
        console.log("Logged in user", response.data);

        return response;
    } catch (error) {
        throw error;
    }
}

export const logout = async () => {
    const token = localStorage.getItem('token')
    try {
        const response = await axios.post(`${API_URL}/logout`,
            {}, // Empty body since it's a logout request
            {
                headers: {
                    'Authorization': `Bearer ${token}` // Include token in Authorization header
                }
            }
        );
    
        console.log("User logged out successfully");
        return response;
    } catch (error) {
        console.error('Logout failed:', error);
    }
};

export const registerUser = async (data) => {
    try {
        const response = await axios.post(
            API_URL + "/register",
            data
        );
        console.log('User Registered successfully', response.data);
        return response
    } catch (error) {
        throw error;
    }
}