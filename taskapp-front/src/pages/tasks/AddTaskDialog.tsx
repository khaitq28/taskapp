import {Box, Button, Checkbox, FormControlLabel, Dialog, DialogActions, Paper, TextField} from "@mui/material";
import {useState} from "react";


export const AddTaskDialog =

    ({ addTask, isOpen, onClose }: { addTask: (title: string, description: string, status: string) => void; isOpen: boolean; onClose: () => void}) => {

    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");
    const [status, setStatus] = useState<"DOING" | "DONE">("DOING");

    const saveTask = () => {
        if (title.trim()) {
            addTask(title,description, status);
            setTitle("");
            setDescription("");
            setStatus("DOING");
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
                        placeholder="Title of task"
                    />
                    <TextField
                        fullWidth
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        placeholder="Description (optional)"
                    />
                    <FormControlLabel
                        control={
                            <Checkbox
                                checked={status === "DONE"}
                                onChange={(e) => setStatus(e.target.checked ? "DONE" : "DOING")}
                            />
                        }
                        label="Done"
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