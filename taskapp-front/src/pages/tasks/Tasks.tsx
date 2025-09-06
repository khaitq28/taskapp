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
    const currentUserId = auth.userLogin?.id
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

  const handleAddNew = (title: string) => {
    // const maxId= getMaxId(tasks);
    // const newTask: Task = {
    //   id: maxId,
    //   user_id: 'b5cf61a4-9e1f-43c3-92c9-cfbc1af7b1d4',
    //   title: title,
    //   completed: false,
    //   createdAt: new Date().toISOString(),
    //   finishedAt: new Date().toISOString(),
    //   des: '',
    //   status: ''
    // }
    // setTasks([...tasks, newTask]);
    // setNotifMessage('Task "' + title + '" was added successfully');
    // setOpenNotif(true);
  }

  const handleCloseNotif = () => {
    setOpenNotif(false);
    setNotifMessage('');
  };

  const getMaxId = (tasks: Task[]): number => {
    let maxId: number = 0;
    tasks.forEach(task => {
      maxId = Math.max(maxId, task.taskId + 1);
    })
    return maxId;
  }

  const generateTasks = () => {
    // const newTasks: Task[] = [];
    // const maxId = getMaxId(tasks);
    // for (let i = 0; i< 10; i++) {
    //   newTasks.push({
    //     id: maxId + i,
    //     title: 'Title task ' + (maxId + i),
    //     user_id: 1,
    //     completed: false,
    //     finishedAt: new Date().toISOString(),
    //     des: '',
    //     status: ''
    //   });
    // }
    // console.log(newTasks);
    // setTasks([...tasks, ...newTasks]);
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
        <>
        {selectedTable == 0 && (
            <TaskTab tasks={unFinishedTasks} handleToggleTask={handleToggleTask} deleteTask={deleteTask} showTaskDetail={showTaskDetail}/>
        )}
        {selectedTable == 1 && (
            <TaskTab tasks={finishedTasks} handleToggleTask={handleToggleTask} deleteTask={deleteTask} showTaskDetail={showTaskDetail}/>
        )}
        </>
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


