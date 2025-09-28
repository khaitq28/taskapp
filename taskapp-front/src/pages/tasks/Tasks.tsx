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
import {Task} from "../../data/Task.ts";
import {UserService} from "../../services/UserService.ts";
import {useAuth} from "../../hooks/useAuth.tsx";
import {TaskService} from "../../services/TaskService.ts";
import {TaskBlock} from "./TaskBlock.tsx";

export const Tasks = () => {
  const navigate = useNavigate();

  const [tasks, setTasks] = useState<Task[]>([]);
  const [openDialog, setOpenDialog] = useState<boolean>(false);
  const [openNotif, setOpenNotif] = useState<boolean>(false);
  const [notifMessage, setNotifMessage] = useState<string>('');
  const [selectedTable, setSelectedTable] = useState(0);

  const finishedTasks = tasks.filter(task => task.status.toUpperCase() === 'DONE');
  const unFinishedTasks = tasks.filter(task => task.status.toUpperCase() !== 'DONE');

  const auth = useAuth();

  useEffect(() => {
    fetchAllTasks().catch(err=> console.log(err));
  }, []);

  const fetchAllTasks = async () => {
    const currentUserId = auth.userLogin?.email
    const userData = await UserService.getUserById(currentUserId);
    if (userData.data.tasks) {
      console.log(userData.data.tasks);
      setTasks(userData.data.tasks);
    }
  };

  const handleToggleTask = (task: Task, event: React.MouseEvent) => {
    if ((event.target as HTMLElement).closest('.MuiCheckbox-root')) {
      task.status =  task.status === "DOING" ? "DONE" : "DOING";
      TaskService.updateTask(task.taskId, task).then(() => fetchAllTasks().catch(err=> console.log(err)));
    } else {
      navigate(`/tasks/${task.taskId}`);
    }
  };

  const deleteTask = (taskId: string) => {
    TaskService.deleteTask(taskId).then(() => {
      fetchAllTasks().catch(err=> console.log(err));
    });
    setNotifMessage('Task ' + taskId  + ' was deleted successfully');
    setOpenNotif(true);
  }

  const showTaskDetail = (taskId: string) => {
    navigate(`/tasks/${taskId}`);
  }

  const handleAddNew = (title: string, description: string, status: string) => {
    // @ts-ignore
      const newTask: Task = {
          title: title,
          des: description,
          userId: auth.userLogin?.id,
          status: status
    }
    TaskService.createTask(newTask).then(() => {
      fetchAllTasks().catch(err=> console.log(err));
    });
    setNotifMessage('Task ' + title  + ' was added successfully');
    setOpenNotif(true);
  }

  const handleCloseNotif = () => {
    setOpenNotif(false);
    setNotifMessage('');
  };

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
        {/*<Tabs value={selectedTable} onChange={() => setSelectedTable(1- selectedTable)} aria-label="Task Tabs">*/}
        {/*  <Tab label={`Unfinished (${unFinishedTasks.length})`} />*/}
        {/*  <Tab label={`Finished (${finishedTasks.length})`} />*/}
        {/*</Tabs>*/}
      </Box>


        <Box sx={{ display: "flex", justifyContent: "space-between", mt: 2 }}>
            <TaskBlock
                tasks={unFinishedTasks}
                handleToggleTask={handleToggleTask}
                deleteTask={deleteTask}
                showTaskDetail={showTaskDetail}
            />
            <TaskBlock
                tasks={finishedTasks}
                handleToggleTask={handleToggleTask}
                deleteTask={deleteTask}
                showTaskDetail={showTaskDetail}
            />
        </Box>
      <Snackbar
          open={openNotif}
          autoHideDuration={3000}
          onClose={handleCloseNotif}
          message={notifMessage}
      />

    </Box>
  );

};


