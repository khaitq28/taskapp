import axios, {AxiosInstance} from "axios";


const  createApi = (): AxiosInstance => {
    return axios.create({
        // baseURL: 'http://localhost:8280/taskapp/v0/api/v1',  //api gateway
        baseURL: 'http://localhost:8080/taskapp/api/v1', //
        timeout: 20000,
        headers: {
            'Content-Type': 'Application/json',
            'Authorization': 'Bearer ' + 'eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJuSXNiUnZFZUo1b1dQa3RSa3F3SWRLdjh5LVVUVkRlZnJwUmUtM0ppeklBIn0.eyJleHAiOjE3NTgyMjg0NDUsImlhdCI6MTc1ODIyODE0NSwianRpIjoiM2I0ZjczMmItODAzOS00ZGI2LWI1MDUtNzQwNTU0ZDkzMmRlIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgxL3JlYWxtcy90YXNrYXBwIiwic3ViIjoiMGE4MjlmNzMtNmY3MS00ZWRmLTkyNTEtMjgyYzQwOThhMDRiIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoidGFza2FwcC1mcm9udGVuZCIsInNpZCI6ImJmYTg4MzRjLTBjOWUtNDM2Mi04MjZhLTU0ZTZkZGJiYWMzOSIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiKiJdLCJyZXNvdXJjZV9hY2Nlc3MiOnsidGFza2FwcC1mcm9udGVuZCI6eyJyb2xlcyI6WyJhZG1pbiJdfX0sInNjb3BlIjoicHJvZmlsZSBlbWFpbCIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYW1lIjoiUXVhbmcgS2hhaSBUUkFOIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiYWRtaW5fdXNlciIsImdpdmVuX25hbWUiOiJRdWFuZyBLaGFpIiwiZmFtaWx5X25hbWUiOiJUUkFOIiwiZW1haWwiOiJ1c2VyMkBleGFtcGxlLmNvbSJ9.Ju8_-arZZLffOe3L-54uu6A9JZklm55BuJc33ihPHvh9E1yDFgcodcjgwbmFN7_0uOMhnkZxLBpgKzkg4LDuqnt4DZEhwDkzn_U-PvcJOSP5AtTaE7OmIKvYmolSpJxe5Wu4NpDAgsSB3ZJkV6G3jxjJmGXDpKd_u-XLK-MqZYpPry4hg6MIHIDJT8e0KLApc4RTKEG-YIFtXhZGAb9Hu6vanKXyuAK9HqxnTxXbSCx2BKzVi3UiYEk5mWG8U1a14m5Q25ovoIxKKOlfWOr4_NO054t72N1NknknBPJrsKuT2ZWyxv9ruAhJP4VTHS3YsEijwt_fd90p2ZzyF6aOKA'
        }
    });
};

export const api = createApi();