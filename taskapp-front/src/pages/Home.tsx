import {Typography, Box} from '@mui/material';
import {useAuth} from "../hooks/useAuth.tsx";

export const Home = () => {

    const { userLogin, loading, isAuthenticated } = useAuth();

    if (loading) return <div>Loading...</div>;


    return (
            <Box sx={{ textAlign: 'center', mt: 4 }}>
              <Typography variant="h4" component="h1" gutterBottom>
                Todo App (demo Webapp for fullstack Dev/DevOps)
              </Typography>
              <Typography variant="h6" color="text.secondary" textAlign="left" marginBottom="500 px">
                    A simple application to manage your tasks <br/>
                    Frontend: React, TypeScript, Vite, Material UI <br />
                    Backend: Java Spring Boot, Spring Security, JWT, OAuth2, PostgreSQL <br/>
                    Infrastructure: AWS, Docker K8s, EKS, S3, CloudFront, Terraform, ECR, OIDC...  <br />
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