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
import {TaskService} from "../../services/TaskService.ts";

export const TaskDetail = () => {
  const { id } = useParams<{ id: number }>();
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
      })
    };
    fetchTask().catch(err => {
      console.error("error:", err);
    });
    },[id]);

  const handleSave = () => {
    if (!task || !editedTitle.trim()) return;
    task.title = editedTitle;
    task.des = des;
    TaskService.updateTask(task.taskId, task).then(res => {
      const updatedTask: Task = res.data;
      setTask(updatedTask);
    }).catch(err => {
      console.error("Error updating task:", err);
    });
    setIsEditing(false);
  };

  const handleToggleComplete = () => {
    if (!task) return;
    if (task.status.toUpperCase() === 'DONE') {
        task.status = 'TODO';
        task.finishedAt = '';
    } else {
        task.status = 'DONE';
        task.finishedAt = new Date().toISOString();
    }
    console.log(task);
    TaskService.updateTask(task.taskId, task).then(res => {
      const newTask: Task = res.data;
      setTask(newTask);
    }).catch(err => {
      console.error("Error updating task:", err);
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

      <Paper sx={{ p: 3 , alignItems: 'flex-start', display: 'flex', flexDirection: 'column'}}>
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
          <>
          {isEditing ? (
            <Box sx={{ display: 'flex', gap: 1 }}>
              <TextField
                  fullWidth
                  value={editedTitle}
                  onChange={(e) => setEditedTitle(e.target.value)}
                  variant="outlined"
              />
              <TextField
                fullWidth
                value={des}
                onChange={(e) => setDes(e.target.value)}
                variant="outlined"
              />
              <Button variant="contained" onClick={handleSave}>
                Save
              </Button>
              <Button variant="outlined" onClick={() => {
                  setIsEditing(false);
                  setDes(task.des); setEditedTitle(task.title);
                }
              }>
                Cancel
              </Button>
            </Box>
          ) : (
            <Box sx={{ display: 'flex', gap: 1, alignItems: 'center' }}>
              <Typography variant="h6" sx={{ flex: 1 }}>
                {task.title}
              </Typography>
              <Typography variant="h6" sx={{ flex: 1 }}>
                {task.des}
              </Typography>
              <Button variant="outlined" onClick={() => setIsEditing(true)}>
                Edit
              </Button>
            </Box>
          )}
          </>
        </Box>

        <Typography variant="body2" color="text.secondary">
          Created at: {new Date(task.createdAt).toLocaleString()}
        </Typography>
        { task.finishedAt && (
            <Typography variant="body2" color="text.secondary">
                Finished at: {new Date(task.finishedAt).toLocaleString()}
            </Typography>
        )}

        <Button onClick={() => navigate('/tasks')}
          sx={{ mt: 3 }}>
          Back to Tasks
        </Button>
      </Paper>
    </Box>
  );
}; 