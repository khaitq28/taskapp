import {User} from "../../data/User.ts";
import {Box, IconButton, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@mui/material";
import {Delete} from "@mui/icons-material";

export const UsersTable = ({users, deleteUser}: {
    users: User[],
    deleteUser: (id: string) => void
}) => {
    return (
        <Box>
            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell align="left">Name</TableCell>
                            <TableCell align="left">Email</TableCell>
                            <TableCell align="left">Role</TableCell>
                            <TableCell align="left"></TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {users.map((userItem) => (
                            <TableRow key={userItem.id}>
                                <TableCell align="left">{userItem.name}</TableCell>
                                <TableCell align="left">{userItem.email}</TableCell>
                                <TableCell align="left">{userItem.role}</TableCell>
                                <TableCell align="left">
                                    <IconButton
                                        onClick={(e) => {
                                            e.stopPropagation();
                                            deleteUser(userItem.id);
                                        }}
                                        edge="end"
                                        aria-label="delete"
                                    >
                                        <Delete />
                                    </IconButton>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        </Box>
    );
}
