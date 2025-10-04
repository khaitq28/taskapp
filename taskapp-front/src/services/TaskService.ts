import {Task} from "../data/Task.ts";
import {api} from "./api.ts";


const TASK_BASE_URL = '/tasks';

export const TaskService = {

    getTasks: async () => {
        return (await api).get<Task[]>(TASK_BASE_URL, {});
    },

    createTask: async (data: Task) => {
        return (await api).post(TASK_BASE_URL, data)
    },

    getTaskById: async (id: string)  => {
        return (await api).get<Task>(TASK_BASE_URL + `/${id}`);
    },

    updateTask: async (id: string, data: Task) => {
        return  (await api).put(TASK_BASE_URL + `/${id}`, data);
    },

    async deleteTask(taskId: string) {
        return  (await api).delete(TASK_BASE_URL + `/${taskId}`);
    },


};