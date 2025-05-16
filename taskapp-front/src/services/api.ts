import axios, {AxiosInstance} from "axios";


const  createApi = (): AxiosInstance => {
    return axios.create({
        baseURL: 'http://localhost:8080/api/v1',
        timeout: 20000,
        headers: {
            'Content-Type': 'Application/json'
        }
    });
};

export const api = createApi();