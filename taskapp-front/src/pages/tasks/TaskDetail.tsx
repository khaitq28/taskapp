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
import { Task } from '../../data/mockData.ts';
import {TaskService} from "../../services/TaskService.ts";

const STORAGE_KEY = 'todo-tasks';

export const TaskDetail = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [task, setTask] = useState<Task>();
  const [editedTitle, setEditedTitle] = useState('');
  const [isEditing, setIsEditing] = useState(false);

  useEffect(() => {
    if (!id) return;
    const fetchTask = async () => {
      TaskService.getTaskById(id).then(res => {
        const task: Task = res.data;
        setTask(task);
        setEditedTitle(task.title);
      })
    };
    fetchTask().catch(err => {
      console.error("error:", err);
    });
    },[id]);

  const handleSave = () => {
    if (!task || !editedTitle.trim()) return;
    const savedTasks = localStorage.getItem(STORAGE_KEY);
    if (savedTasks) {
      const tasks = JSON.parse(savedTasks);
      const updatedTasks = tasks.map((t: Task) =>
         t.id === task.id ? { ...t, title: editedTitle.trim() } : t
      );
      task.title = editedTitle;
      localStorage.setItem(STORAGE_KEY, JSON.stringify(updatedTasks));
    }
    setIsEditing(false);
  };

  const handleToggleComplete = () => {
    if (!task) return;

    const savedTasks = localStorage.getItem(STORAGE_KEY);
    if (savedTasks) {
      const tasks = JSON.parse(savedTasks);
      const updatedTasks = tasks.map((t: Task) =>
        t.id === task.id ? { ...t, completed: !t.completed } : t
      );
      localStorage.setItem(STORAGE_KEY, JSON.stringify(updatedTasks));
      setTask({ ...task, completed: !task.completed });
    }
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

      <Paper sx={{ p: 3 , alignItems: 'flex-start', display: 'flex', flexDirection: 'column'}}>
        <Box sx={{ mb: 2 }}>
          <FormControlLabel
            control={
              <Checkbox
                checked={task.completed}
                onChange={handleToggleComplete}
                color="primary"
              />
            }
            label="Mark as completed"
          />
        </Box>

        <Box sx={{ mb: 2 }}>
          {isEditing ? (
            <Box sx={{ display: 'flex', gap: 1 }}>
              <TextField
                fullWidth
                value={editedTitle}
                onChange={(e) => setEditedTitle(e.target.value)}
                variant="outlined"
              />
              <Button variant="contained" onClick={handleSave}>
                Save
              </Button>
              <Button variant="outlined" onClick={() => setIsEditing(false)}>
                Cancel
              </Button>
            </Box>
          ) : (
            <Box sx={{ display: 'flex', gap: 1, alignItems: 'center' }}>
              <Typography variant="h6" sx={{ flex: 1 }}>
                {task.title}
              </Typography>
              <Button variant="outlined" onClick={() => setIsEditing(true)}>
                Edit
              </Button>
            </Box>
          )}
        </Box>

        <Typography variant="body2" color="text.secondary">
          Created at: {new Date(task.createdAt).toLocaleString()}
        </Typography>

        <Button onClick={() => navigate('/tasks')}
          sx={{ mt: 3 }}>
          Back to Tasks
        </Button>
      </Paper>
    </Box>
  );
}; 