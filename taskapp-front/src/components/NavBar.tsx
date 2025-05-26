import {useContext} from "react";
import {AuthContext} from "../context/AuthContext.tsx";
import {Box, Button, Toolbar} from "@mui/material";
import {Link as RouterLink} from "react-router";
import {useNavigate} from "react-router-dom";


export const NavBar = () => {

    const auth = useContext(AuthContext);
    const navigate = useNavigate();
    const handLogout =() => {
        if (auth) {
            auth.logout();
        }
        setTimeout(() => {
            navigate('/');
        }, 0);
    };

    return (
            <Toolbar>
                <Button color="inherit" component={RouterLink} to="/">Home</Button>
                {auth?.isAuthenticated && (
                    <Button color="inherit" component={RouterLink} to="/tasks">Tasks</Button>
                )}
                {auth?.isAuthenticated && auth.userLogin?.role === 'ADMIN' && (
                    <Button color="inherit" component={RouterLink} to="/admin">Admin</Button>
                )}
                <Box sx={{ flexGrow: 1 }} />

                {auth?.isAuthenticated ? (
                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                        <span>Hello, {auth.userLogin?.name}</span>
                        <Button variant="contained" color="primary" onClick={() => handLogout()}>
                            Logout
                        </Button>
                    </Box>
                ) : (
                    <Button variant="contained" color="primary" component={RouterLink} to="/login">
                        Login
                    </Button>
                )}
            </Toolbar>
    )

};