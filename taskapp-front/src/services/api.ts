import axios, { AxiosInstance } from "axios";
import { getAccess, refreshAccess } from "../context/authClient";
import {loadConfig} from "../config.ts";


let apiInstance: AxiosInstance | null = null;

const createApi = async (): Promise<AxiosInstance> => {

    if (apiInstance) return apiInstance;

    const cfg = await loadConfig();
    const baseURL = cfg.apiBaseUrl;

    const instance = axios.create({
        baseURL,
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
    apiInstance = instance;
    return instance;
};

export const api = createApi();
