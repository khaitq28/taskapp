import {Task} from "../data/mockData.ts";
import {api} from "./api.ts";


const TASK_BASE_URL = '/tasks';

export const TaskService = {

    getTasks: () => {
        return api.get<Task[]>(TASK_BASE_URL, {});
    },

    createTask: (data: Task) => {
        return api.post(TASK_BASE_URL, data)
    },

    getTaskById: (id: string)  => {
        return api.get<Task>(TASK_BASE_URL + `/${id}`);
    },

    updateTask: (id: string, data: Task) => {
        return api.post(TASK_BASE_URL + `/${id}`, data);
    }

};