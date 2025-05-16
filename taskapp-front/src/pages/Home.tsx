import {Typography, Box} from '@mui/material';

export const Home = () => {


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

            </Box>

      );
};