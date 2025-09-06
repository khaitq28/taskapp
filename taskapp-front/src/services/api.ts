import axios, {AxiosInstance} from "axios";


const  createApi = (): AxiosInstance => {
    return axios.create({
        // baseURL: 'http://localhost:8280/taskapp/v0/api/v1',  //api gateway
        baseURL: 'http://localhost:8080/taskapp/api/v1', //
        timeout: 20000,
        headers: {
            'Content-Type': 'Application/json',
            'apiKey': 'eyJ4NXQjUzI1NiI6Ik1XVXhZbU5rWldJd1lUUTJORGN5TVRVd1l6VTFOVFF5WVRsall6QXlaak01TkRneFpUVmtaREZsTm1WaE5Ea3pZemd5WWpBeU0yTmlaVEF6WWpRMFl3PT0iLCJraWQiOiJnYXRld2F5X2NlcnRpZmljYXRlX2FsaWFzIiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ==.eyJzdWIiOiJhZG1pbkBjYXJib24uc3VwZXIiLCJhcHBsaWNhdGlvbiI6eyJpZCI6MiwidXVpZCI6IjU1ZjA4NjFjLTY4ZjktNDYzNC05ZTg1LWQ1NzJmZGZkNTExMyJ9LCJpc3MiOiJodHRwczpcL1wvbG9jYWxob3N0Ojk0NDNcL29hdXRoMlwvdG9rZW4iLCJrZXl0eXBlIjoiUFJPRFVDVElPTiIsInBlcm1pdHRlZFJlZmVyZXIiOiIiLCJ0b2tlbl90eXBlIjoiYXBpS2V5IiwicGVybWl0dGVkSVAiOiIiLCJpYXQiOjE3NTcxOTM4NzMsImp0aSI6IjAyNWZlNDhjLWVkOWEtNDRjYy05N2I1LTE2NGRhMjljNmE1NCJ9.fv0cwuk1hWd5NU_-MyvcTOIYm-esb3eJMbk_KPdLQPTf4uWkl27LyK-Yj0Bfz-NgU7vGiOYbvbCARhTtYB8gcjrdjgi-hamMA3f5352sXtklNcHjTIWMnlewyOdwP74p0UxZ-km0y1LyCo4pmCfodD1vDp3_9fD-CRnrncUfGLvuAbCnZY7ePMznQ8PUZhZhpNfmsG6Dxo70hynnTT9mYKX-JTmwZhzPX-zjWSIhtpdpCcjEBTzfJL6eV-pUHi4oVX_tN7dMTME9YKh3DREE3qMZJPZba3DhmcJPcSeIsEPCbt3D9O0G3rbSSbRfb3OEnTC60sOgorI2qctDTifTiA=='
        }
    });
};

export const api = createApi();