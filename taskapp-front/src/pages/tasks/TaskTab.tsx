import {Box, Checkbox, IconButton, List, ListItem, ListItemText, Paper, TableContainer} from "@mui/material";
import {Task} from "../../data/mockData.ts";
import { Delete } from "@mui/icons-material";


export const TaskTab = ({tasks, handleToggleTask,deleteTask, showTaskDetail}:
                        {   tasks: Task[],
                             handleToggleTask: (id:number,e:any) => void,
                             deleteTask: (id:number) => void,
                             showTaskDetail: (id:number) =>void}
                        ) => {

    return (
        <Box>
            <TableContainer component={Paper}>
                <List>
                    {tasks.map((task) => (
                        <ListItem
                            key={task.id}
                            component="div"
                            sx={{
                                cursor: 'pointer'
                            }}
                        >

                            <ListItemText
                                primary={task.id}
                            />
                            <Checkbox
                                edge="start"
                                checked={task.completed}
                                // tabIndex={-1}
                                disableRipple
                                color="primary"
                                className="MuiCheckbox-root"
                                onClick={(e) => handleToggleTask(task.id, e)}
                            />
                            <ListItemText
                                primary={task.title}
                                onClick={(e) => {
                                    e.stopPropagation();
                                    showTaskDetail(task.id)}
                                }
                                sx={{
                                    textDecoration: task.completed ? 'line-through' : 'none',
                                    color: task.completed ? 'text.secondary' : 'text.primary'
                                }}
                            />
                            <ListItemText
                                primary={task.status}
                            />
                            <IconButton
                                onClick={(e) => {
                                    e.stopPropagation(); // Prevent triggering the ListItem onClick
                                    deleteTask(task.id);
                                }}
                                edge="end"
                                aria-label="delete">
                                <Delete/>
                            </IconButton>
                        </ListItem>
                    ))}
                </List>
            </TableContainer>
        </Box>
    );

};