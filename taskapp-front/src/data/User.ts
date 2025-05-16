import {Task} from "./mockData.ts";

export interface User {
    id: number;
    name: string;
    email: string;
    tasks: Task[];
    role: string;
}