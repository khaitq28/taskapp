import {User} from "../../data/User.ts";
import {useEffect, useState} from "react";
import {Box, Button, Checkbox, IconButton, List, ListItem, ListItemText, Paper, TableContainer} from "@mui/material";
import {Delete} from "@mui/icons-material";


export const UsersTable = ({users, deleteUser, addUser}: {
                    users: User[],
                    deleteUser: (id:string) => void}) => {

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
                        {users.map((userItem) => (
                            <ListItem
                                key={userItem.id}
                                component="div"
                                sx={{
                                    cursor: 'pointer'
                                }}
                            >
                                <ListItemText
                                    primary={userItem.name}
                                />
                                <ListItemText
                                    primary={userItem.email}
                                />
                                <ListItemText
                                    primary={userItem.role}
                                />

                                <IconButton
                                    onClick={(e) => {
                                        e.stopPropagation(); // Prevent triggering the ListItem onClick
                                        deleteUser(userItem.id);
                                    }}
                                    edge="end"
                                    aria-label="delete">
                                    <Delete/>
                                </IconButton>
                            </ListItem>
                        ))}
                    </>
                </List>
            </TableContainer>
        </Box>
    )
}