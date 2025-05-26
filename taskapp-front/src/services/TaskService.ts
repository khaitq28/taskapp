import {Task} from "../data/Task.ts";
import {api} from "./api.ts";


const TASK_BASE_URL = '/tasks';

export const TaskService = {

    getTasks: () => {
        return api.get<Task[]>(TASK_BASE_URL, {});
    },

    createTask: (data: Task) => {
        return api.post(TASK_BASE_URL, data)
    },

    getTaskById: (id: number)  => {
        return api.get<Task>(TASK_BASE_URL + `/${id}`);
    },

    updateTask: (id: number, data: Task) => {
        return api.put(TASK_BASE_URL + `/${id}`, data);
    },

    deleteTask(taskId: number) {
        return api.delete(TASK_BASE_URL + `/${taskId}`);
    }
};