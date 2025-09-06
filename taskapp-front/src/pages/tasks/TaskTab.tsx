import {Box, Checkbox, IconButton, List, ListItem, ListItemText, Paper, TableContainer} from "@mui/material";
import {Task} from "../../data/Task.ts";
import { Delete } from "@mui/icons-material";


export const TaskTab = ({tasks, handleToggleTask,deleteTask, showTaskDetail}:
                        {   tasks: Task[],
                             handleToggleTask: (id:string,e:any) => void,
                             deleteTask: (id:string) => void,
                             showTaskDetail: (id:string) =>void}
                        ) => {

    return (
        <Box>
            <TableContainer component={Paper}>
                <List>
                    <>
                    {tasks.map((task) => (
                        <ListItem
                            key={task.taskId}
                            component="div"
                            sx={{
                                cursor: 'pointer'
                            }}
                        >
                            <Checkbox
                                edge="start"
                                checked={task.status.toUpperCase() === 'DONE'}
                                disableRipple
                                color="primary"
                                className="MuiCheckbox-root"
                                onClick={(e) => handleToggleTask(task.taskId, e)}
                            />
                            <ListItemText
                                primary={task.title}
                                onClick={(e) => {
                                    e.stopPropagation();
                                    showTaskDetail(task.taskId)}
                                }
                                sx={{
                                    textDecoration: task.completed ? 'line-through' : 'none',
                                    color: task.completed ? 'text.secondary' : 'text.primary'
                                }}
                            />
                            <ListItemText
                                primary={task.des}
                            />
                            <IconButton
                                onClick={(e) => {
                                    e.stopPropagation(); // Prevent triggering the ListItem onClick
                                    deleteTask(task.taskId);
                                }}
                                edge="end"
                                aria-label="delete">
                                <Delete/>
                            </IconButton>
                        </ListItem>
                    ))}
                    </>
                </List>
            </TableContainer>
        </Box>
    );

};