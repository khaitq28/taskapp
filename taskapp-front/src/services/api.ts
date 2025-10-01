import axios, { AxiosInstance } from "axios";
import { getAccess, refreshAccess } from "../context/authClient";


const createApi = (): AxiosInstance => {
    const instance = axios.create({
        baseURL: "http://localhost:8080/taskapp/api/v1",
        timeout: 20000,
        withCredentials: true,
        headers: { "Content-Type": "application/json" },
    });

    instance.interceptors.request.use(async (config) => {
        const token = getAccess();
        if (token) {
            config.headers?.set?.('Authorization', `Bearer ${token}`);
        }
        return config;
    });

    instance.interceptors.response.use(
        (response) => response,
        async (error) => {
            const originalRequest = error.config;
            if (error.response?.status === 401 && !originalRequest._retry) {
                originalRequest._retry = true;
                const newToken = await refreshAccess();
                originalRequest.headers["Authorization"] = `Bearer ${newToken}`;
                return instance(originalRequest);
            }
            return Promise.reject(error);
        }
    );

    return instance;
};

export const api = createApi();
