import {User} from "../../data/User.ts";
import {useEffect} from "react";
import {Box, Checkbox, IconButton, List, ListItem, ListItemText, Paper, TableContainer} from "@mui/material";


export const UsersTable = ({users}: { users: User[]}) => {

    useEffect(() => {
        console.log(users);
        users.forEach(e => {
            console.log(`User ID: ${e.id}`);
        });
    }, []);

    return (
        // user MUI for users lists by List and ListItem and Box
        <Box>
                {users.map((user) => (
                    <ListItem key={user.id} component="div">
                        <ListItemText primary={`ID: ${user.id}`} />
                        <ListItemText primary={`Username: ${user.name}`} />
                        <ListItemText primary={`Email: ${user.email}`} />
                        <ListItemText primary={`Role: ${user.role}`} />
                    </ListItem>
                ))}
        </Box>

    )
}