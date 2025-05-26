import {Task} from "./Task.ts";

export interface User {
    id: number;
    name: string;
    email: string;
    tasks: Task[];
    role: string;
}