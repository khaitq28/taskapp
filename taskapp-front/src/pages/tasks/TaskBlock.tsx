import { Box, Checkbox, IconButton, Paper } from "@mui/material";
import { Task } from "../../data/Task.ts";
import { Delete } from "@mui/icons-material";

export const TaskBlock = ({
                              tasks,
                              handleToggleTask,
                              deleteTask,
                              showTaskDetail
                          }: {
    tasks: Task[],
    handleToggleTask: (task: Task, e: any) => void,
    deleteTask: (id: string) => void,
    showTaskDetail: (id: string) => void
}) => (
    <Box sx={{ width: "45%", margin: "0 auto" }}>

        <div>Number of tasks: {tasks.length}</div>
        {tasks.map((task) => (
            <Paper
                key={task.taskId}
                sx={{
                    display: "flex",
                    alignItems: "center",
                    mb: 2,
                    p: 2,
                    fontSize: "1.2rem",
                    cursor: "pointer"
                }}
            >
                <Checkbox
                    edge="start"
                    checked={task.status.toUpperCase() === "DONE"}
                    disableRipple
                    color="primary"
                    sx={{ mr: 2 }}
                    onClick={(e) => handleToggleTask(task, e)}
                />
                <Box
                    sx={{
                        flex: 2,
                        textDecoration: task.completed ? "line-through" : "none",
                        color: task.completed ? "text.secondary" : "text.primary",
                        textAlign: "left",
                        cursor: "pointer"
                    }}
                    onClick={() => showTaskDetail(task.taskId)}
                >
                    {task.title}
                </Box>
                <Box
                    sx={{ flex: 3, ml: 2, textAlign: "left", cursor: "pointer" }}
                    onClick={() => showTaskDetail(task.taskId)}
                >
                    {task.des}
                </Box>
                <Box sx={{ flex: 1, ml: 2, textAlign: "left" }}>
                    {task.status}
                </Box>
                <IconButton
                    onClick={(e) => {
                        e.stopPropagation();
                        deleteTask(task.taskId);
                    }}
                    edge="end"
                    aria-label="delete"
                    sx={{ ml: 2 }}
                >
                    <Delete />
                </IconButton>
            </Paper>
        ))}
    </Box>
);
