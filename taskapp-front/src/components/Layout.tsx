import { AppBar, Typography, Container, Box, ThemeProvider, createTheme } from '@mui/material';
import {NavBar} from "./NavBar.tsx";

const theme = createTheme({
  palette: {
    background: {
      default: '#f5f5f5',
      paper: '#ffffff',
    },
  },
});

interface LayoutProps {
  children: React.ReactNode;
}

export const Layout = ({ children }: LayoutProps) => {

  return (
    <ThemeProvider theme={theme}>
      <Box sx={{ 
        display: 'flex', 
        flexDirection: 'column', 
        minHeight: '100vh',
        width: '100%',
        maxWidth: '1200px',
        bgcolor: '#fffeff',
        boxShadow: '0 0 10px rgba(0,0,0,0.1)'
      }}>
        <AppBar position="static" color="primary">
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            Todo App
          </Typography>

          <NavBar/>
        </AppBar>
        <Container component="main" sx={{ mt: 4, mb: 4, flex: 1 }}>
          {children}
        </Container>
      </Box>
    </ThemeProvider>
  );
}; 