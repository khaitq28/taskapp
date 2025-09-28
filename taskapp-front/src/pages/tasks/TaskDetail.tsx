import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
    Box,
    Typography,
    TextField,
    Button,
    Paper,
    Checkbox,
    FormControlLabel
} from '@mui/material';
import { Task } from '../../data/Task.ts';
import { TaskService } from '../../services/TaskService.ts';

export const TaskDetail = () => {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();
    const [task, setTask] = useState<Task>();
    const [editedTitle, setEditedTitle] = useState('');
    const [des, setDes] = useState('');
    const [isEditing, setIsEditing] = useState(false);

    useEffect(() => {
        if (!id) return;
        const fetchTask = async () => {
            TaskService.getTaskById(id).then(res => {
                const task: Task = res.data;
                setTask(task);
                setEditedTitle(task.title);
                setDes(task.des);
            });
        };
        fetchTask().catch(err => {
            console.error('error:', err);
        });
    }, [id]);

    const handleSave = () => {
        if (!task || !editedTitle.trim()) return;
        const updatedTask = { ...task, title: editedTitle, des };
        TaskService.updateTask(task.taskId, updatedTask).then(res => {
            setTask(res.data);
        }).catch(err => {
            console.error('Error updating task:', err);
        });
        setIsEditing(false);
    };

    const handleToggleComplete = () => {
        if (!task) return;
        const updatedTask = {
            ...task,
            status: task.status.toUpperCase() === 'DONE' ? 'TODO' : 'DONE',
            finishedAt: task.status.toUpperCase() === 'DONE' ? '' : new Date().toISOString()
        };
        TaskService.updateTask(task.taskId, updatedTask).then(res => {
            setTask(res.data);
        }).catch(err => {
            console.error('Error updating task:', err);
        });
    };

    if (!task) {
        return (
            <Box>
                <Typography variant="h5">Task not found</Typography>
                <Button onClick={() => navigate('/tasks')} sx={{ mt: 2 }}>
                    Back to Tasks
                </Button>
            </Box>
        );
    }

    return (
        <Box>
            <Typography variant="h4" component="h2" gutterBottom>
                Task Detail
            </Typography>

            <Paper sx={{ p: 3, alignItems: 'flex-start', display: 'flex', flexDirection: 'column' }}>
                <Box sx={{ mb: 2 }}>
                    <FormControlLabel
                        control={
                            <Checkbox
                                checked={task.status.toUpperCase() === 'DONE'}
                                onChange={handleToggleComplete}
                                color="primary"
                            />
                        }
                        label="Mark as completed"
                    />
                </Box>

                <Box sx={{ mb: 2 }}>
                    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
                        <Box sx={{ display: 'flex', alignItems: 'center' }}>
                            <Typography variant="subtitle2" sx={{ mr: 1 }}>Title:</Typography>
                            {isEditing ? (
                                <TextField
                                    value={editedTitle}
                                    onChange={e => setEditedTitle(e.target.value)}
                                    variant="outlined"
                                    size="small"
                                    sx={{ flex: '0 1 auto', minWidth: 0 }}
                                />
                            ) : (
                                <Typography
                                    variant="h6"
                                    sx={{
                                        flex: '0 1 auto',
                                        minWidth: 0,
                                        whiteSpace: 'nowrap',
                                        overflow: 'hidden',
                                        textOverflow: 'ellipsis'
                                    }}
                                >
                                    {task.title}
                                </Typography>
                            )}
                        </Box>
                        <Box sx={{ display: 'flex', alignItems: 'center' }}>
                            <Typography variant="subtitle2" sx={{ mr: 1 }}>Description:</Typography>
                            {isEditing ? (
                                <TextField
                                    value={des}
                                    onChange={e => setDes(e.target.value)}
                                    variant="outlined"
                                    size="small"
                                    sx={{ flex: '0 1 auto', minWidth: 0 }}
                                />
                            ) : (
                                <Typography
                                    variant="h6"
                                    sx={{
                                        flex: '0 1 auto',
                                        minWidth: 0,
                                        whiteSpace: 'nowrap',
                                        overflow: 'hidden',
                                        textOverflow: 'ellipsis'
                                    }}
                                >
                                    {task.des}
                                </Typography>
                            )}
                        </Box>
                        {isEditing ? (
                            <Box sx={{ display: 'flex', gap: 1, mt: 1 }}>
                                <Button variant="contained" onClick={handleSave}>
                                    Save
                                </Button>
                                <Button
                                    variant="outlined"
                                    onClick={() => {
                                        setIsEditing(false);
                                        setEditedTitle(task.title);
                                        setDes(task.des);
                                    }}
                                >
                                    Cancel
                                </Button>
                            </Box>
                        ) : (
                            <Button variant="outlined" sx={{ mt: 1 }} onClick={() => setIsEditing(true)}>
                                Edit
                            </Button>
                        )}
                    </Box>
                </Box>

                <Typography variant="body2" color="text.secondary">
                    Created at: {new Date(task.createdAt).toLocaleString()}
                </Typography>
                {task.finishedAt && (
                    <Typography variant="body2" color="text.secondary">
                        Finished at: {new Date(task.finishedAt).toLocaleString()}
                    </Typography>
                )}

                <Button onClick={() => navigate('/tasks')} sx={{ mt: 3 }}>
                    Back to Tasks
                </Button>
            </Paper>
        </Box>
    );
};
