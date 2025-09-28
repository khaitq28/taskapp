import {useEffect, useState} from "react";
import {User} from "../../data/User.ts";
import {UserService} from "../../services/UserService.ts";
import {UsersTable} from "./UsersTable.tsx";
import {Box, Button, Dialog, DialogActions, DialogContent, DialogTitle, MenuItem, TextField} from "@mui/material";
import * as yup from "yup";

const schema = yup.object().shape({
    name: yup.string().required("Name is required"),
    email: yup.string().email("Invalid email").required("Email is required"),
    role: yup.string().oneOf(["USER", "ADMIN"], "Role must be USER or ADMIN"),
});

export const AdminPage = () => {

    const [users, setUsers] = useState<User[]>([]);
    const [open, setOpen] = useState(false);

    const [formUser, setFormUser] = useState({
        name: "",
        email: "",
        role: "USER",
    });

    const handleChange = (e) => {
        setFormUser((prev) => ({
            ...prev,
            [e.target.name]: e.target.value,
        }));
    };

    const fetchAllUsers = async () => {
        UserService.getAllUsers().then(data => {
            setUsers(data.data);
        })
    };

    useEffect(() => {
        fetchAllUsers().catch(err=> console.log(err));
    }, []);

    const deleteUser = (id: string) => {
        let ok = window.confirm(`Are you sure you want to delete user with ID ${id}?`);
        if (!ok) return;
        UserService.deleteUser(id).then(() => {
            fetchAllUsers().catch(err=> console.log(err));
        }).catch(err => {
            console.error("Error deleting user:", err);
        });
    };

    const handleSubmit = () => {
        console.log(formUser);
        const newUser: User = {
            id: '',
            name: formUser.name,
            email: formUser.email,
            role: formUser.role
        };
        UserService.saveUser(newUser).then(() => {
            fetchAllUsers();
        });
        setOpen(false);
        setFormUser({ name: "", email: "", role: "USER" }); // 🔄 Reset form
    };

    return (
        <div className="admin-page">
            <h2>Admin Page</h2>
            <p>Welcome to the admin page!</p>

            <Box>
                <Button
                    variant="contained"
                    color="primary"
                    onClick={(e) => {
                        e.stopPropagation();setOpen(true);
                    }}
                    sx={{float: 'right', marginBottom: 2}}>
                    Add User
                </Button>
            </Box>
            {users.length === 0 && <p>No users found.</p>}
            {users.length > 0 && <UsersTable users={users} deleteUser={deleteUser}/>}


            <Dialog open={open} onClose={() => setOpen(false)}>
                <DialogTitle>Add User</DialogTitle>
                <DialogContent>
                    <TextField
                        autoFocus
                        margin="dense"
                        label="Name"
                        name="name"
                        fullWidth
                        value={formUser.name}
                        onChange={handleChange}
                    />
                    <TextField
                        margin="dense"
                        label="Email"
                        name="email"
                        fullWidth
                        value={formUser.email}
                        onChange={handleChange}
                    />
                    <TextField
                        margin="dense"
                        label="Role"
                        name="role"
                        select
                        fullWidth
                        value={formUser.role}
                        onChange={handleChange}>
                        <MenuItem value="USER">USER</MenuItem>
                        <MenuItem value="ADMIN">ADMIN</MenuItem>
                    </TextField>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => setOpen(false)}>Cancel</Button>
                    <Button onClick={() => {
                        handleSubmit();
                    }}>Save</Button>
                </DialogActions>
            </Dialog>

        </div>


    );
};