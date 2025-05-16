import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Typography,
  Box,
  Button, Snackbar, Tabs, Tab
} from '@mui/material';

import {AddTaskDialog} from "./AddTaskDialog.tsx";
import {TaskTab} from "./TaskTab.tsx";
import {TaskSearchBlock} from "./TaskSearchBlock.tsx";
import {Task} from "../../data/mockData.ts";
import {UserService} from "../../services/UserService.ts";
import {useAuth} from "../../hooks/useAuth.tsx";

export const Tasks = () => {
  const navigate = useNavigate();

  const [tasks, setTasks] = useState<Task[]>([]);
  const [openDialog, setOpenDialog] = useState<boolean>(false);
  const [openNotif, setOpenNotif] = useState<boolean>(false);
  const [notifMessage, setNotifMessage] = useState<string>('');
  const [selectedTable, setSelectedTable] = useState(0);

  const finishedTasks = tasks.filter(task => task.completed);
  const unFinishedTasks = tasks.filter(task => !task.completed);

  const auth = useAuth();

  useEffect(() => {
    fetchAllTasks().catch(err=> console.log(err));
  }, []);

  const fetchAllTasks = async () => {
    const currentUserId = auth.userLogin?.id
    const userData = await UserService.getUserById(currentUserId);
    if (userData.data.tasks) {
      setTasks(userData.data.tasks);
    }
  };

  const handleToggleTask = (taskId: number, event: React.MouseEvent) => {
    if ((event.target as HTMLElement).closest('.MuiCheckbox-root')) {
      setTasks(tasks.map(task =>
        task.id === taskId ? { ...task, completed: !task.completed } : task
      ));
    } else {
      navigate(`/tasks/${taskId}`);
    }
  };

  const deleteTask = (taskId: number) => {
    const updatedTasks = tasks.filter(task => task.id !== taskId);
    setTasks(updatedTasks);
    setNotifMessage('Task ' + taskId  + ' was deleted successfully');
    setOpenNotif(true);
  }

  const showTaskDetail = (taskId: number) => {
    navigate(`/tasks/${taskId}`);
  }

  const handleAddNew = (title: string) => {
    const maxId= getMaxId(tasks);
    const newTask: Task = {
      id: maxId,
      user_id: 1,
      title: title,
      completed: false,
      createdAt: new Date().toISOString(),
      description: '',
      status: ''
    }
    setTasks([...tasks, newTask]);
    setNotifMessage('Task "' + title + '" was added successfully');
    setOpenNotif(true);
  }

  const handleCloseNotif = () => {
    setOpenNotif(false);
    setNotifMessage('');
  };

  const getMaxId = (tasks: Task[]): number => {
    let maxId: number = 0;
    tasks.forEach(task => {
      maxId = Math.max(maxId, task.id + 1);
    })
    return maxId;
  }

  const generateTasks = () => {
    const newTasks: Task[] = [];
    const maxId = getMaxId(tasks);
    for (let i = 0; i< 10; i++) {
      newTasks.push({
        id: maxId + i,
        title: 'Title task ' + (maxId + i),
        user_id: 1,
        completed: false,
        createdAt: new Date().toISOString(),
        description: '',
        status: ''
      });
    }
    console.log(newTasks);
    setTasks([...tasks, ...newTasks]);
  }

  const searchHandle = (keyWord: string) => {
    if (keyWord.trim()) {
      setTasks(tasks.filter(e => e.title.toLowerCase().indexOf(keyWord.toLowerCase()) >= 0));
    } else {
      fetchAllTasks();
    }
  }

  return (
    <Box>
      <AddTaskDialog addTask = {handleAddNew}
                     isOpen={openDialog}
                     onClose={() => setOpenDialog(false)}
      />
      <Typography variant="h4" component="h2" gutterBottom>
        Tasks
      </Typography>
      <Box sx={{ textAlign: 'right'}}>
        <TaskSearchBlock searchHandle={searchHandle}/>
      </Box>

      <Box sx={{ textAlign: 'right'}}>
        <Button variant={"contained"} onClick={()=> setOpenDialog(true)}>Add task</Button>

        <Tabs value={selectedTable} onChange={() => setSelectedTable(1- selectedTable)} aria-label="Task Tabs">
          <Tab label={`Unfinished (${unFinishedTasks.length})`} />
          <Tab label={`Finished (${finishedTasks.length})`} />
        </Tabs>
      </Box>

      <Box sx={{ mt:2}}>
        {selectedTable == 0 && (
            <TaskTab tasks={unFinishedTasks} handleToggleTask={handleToggleTask} deleteTask={deleteTask} showTaskDetail={showTaskDetail}/>
        )}
        {selectedTable == 1 && (
            <TaskTab tasks={finishedTasks} handleToggleTask={handleToggleTask} deleteTask={deleteTask} showTaskDetail={showTaskDetail}/>
        )}
      </Box>

      <Snackbar
          open={openNotif}
          autoHideDuration={3000}
          onClose={handleCloseNotif}
          message={notifMessage}
      />

      <Button onClick={() => generateTasks()}>Generate tasks....</Button>

    </Box>
  );

};


