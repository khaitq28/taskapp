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

        <Box>
            <TableContainer component={Paper}>
                <List>
                    <>
                        {users.map((task) => (
                            <ListItem
                                key={task.id}
                                component="div"
                                sx={{
                                    cursor: 'pointer'
                                }}
                            >
                                <ListItemText
                                    primary={task.name}
                                />
                                <ListItemText
                                    primary={task.email}
                                />
                                <ListItemText
                                    primary={task.role}
                                />
                            </ListItem>
                        ))}
                    </>
                </List>
            </TableContainer>
        </Box>
    )
}