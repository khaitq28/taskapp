import {Typography, Box} from '@mui/material';
import {useAuth} from "../hooks/useAuth.tsx";

export const Home = () => {

    const { userLogin, loading, isAuthenticated } = useAuth();

    if (loading) return <div>Loading...</div>;


    return (
            <Box sx={{ textAlign: 'center', mt: 4 }}>
              <Typography variant="h4" component="h1" gutterBottom>
                Welcome to Todo App
              </Typography>
              <Typography variant="body1" color="text.secondary" textAlign="left">
                A simple application to manage your tasks <br/>
                Frontend: React, TypeScript, Vite, Material UI
                <br />
                Backend: Golang, Gin, Gorm, PostgreSQL
                <br />
              </Typography>


                {isAuthenticated ? (
                    <div>
                        Welcome, {userLogin?.name ?? userLogin?.email}
                        <pre>{JSON.stringify(userLogin, null, 2)}</pre>
                    </div>
                ) : (
                    <div>Not signed in</div>
                )}

            </Box>

      );
};