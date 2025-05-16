export interface Task {
  id: number;
  user_id: number;
  description: string;
  status: string;
  title: string;
  completed: boolean;
  createdAt: string;
}

export const mockTasks: Task[] = [
  {
    id: 1,
    user_id: 1,
    title: "Learn React",
    completed: false,
    createdAt: "2024-03-20T10:00:00Z",
    description: "",
    status: ""
  },
  {
    id: 2,
    user_id: 1,
    title: "Build Todo App",
    completed: true,
    createdAt: "2024-03-20T11:00:00Z",
    description: "",
    status: ""
  },
  {
    id: 3,
    user_id: 1,
    title: "Study TypeScript",
    completed: false,
    createdAt: "2024-03-20T12:00:00Z",
    description: "",
    status: ""
  }
]; 