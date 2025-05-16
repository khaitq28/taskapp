import {Box, Button, Dialog, DialogActions, Paper, TextField} from "@mui/material";
import {useState} from "react";


export const AddTaskDialog =

    ({ addTask, isOpen, onClose }: { addTask: (title: string) => void; isOpen: boolean; onClose: () => void}) => {

    const [title, setTitle] = useState("");
    const saveTask = () => {
        if (title.trim()) {
            addTask(title);
            setTitle("");
            onClose();
        }
    }

    return (
        <Dialog open={isOpen} fullWidth={true} onClose={onClose}>
            <Paper sx={{ p: 3, mb: 3 }}>
                <Box sx={{ display: 'flex', gap: 1 }}>
                    <TextField
                        fullWidth
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        placeholder="Add new task"
                    />
                </Box>
                <DialogActions>
                    <Button onClick={onClose}>Cancel</Button>
                    <Button variant="contained" onClick={saveTask}>Save
                    </Button>
                </DialogActions>
            </Paper>
        </Dialog>
    );
};