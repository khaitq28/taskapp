import {Box, Button, DialogActions, TextField, Typography} from "@mui/material";
import {useAuth} from "../hooks/useAuth.tsx";
import { useNavigate} from "react-router-dom";
import {useEffect} from "react";
import * as yup from "yup";
import {useForm} from "react-hook-form";
import {yupResolver} from "@hookform/resolvers/yup";


type FormData = {
    username: string;
    password: string;
}

const schema = yup.object({
    username: yup.string().required("Email is required"),
    password: yup.string().required("Password is required"),
}).required();


export const Login = () => {
    const { login, isAuthenticated } = useAuth();
    const navigate = useNavigate();
    const { register, handleSubmit, formState: { errors } } = useForm<FormData>({
        resolver: yupResolver(schema)
    });
    useEffect(() => {
        if (isAuthenticated) {
            navigate('/');
        }
    }, [isAuthenticated, navigate]);

    const handLogin = (id : string, role: string) => {
        login("FAKE_TOKEN",
            {
                id: id,
                name: "Tony " + role,
                email: "user2@example.com",
                role: role
            });
        setTimeout(() => {
            navigate('/');
        }, 0);
    };

    const doLogin = (data: FormData) => {
        console.log(data);
    };


    return (
        <Box>
            {!isAuthenticated && (
                <DialogActions>
                    <Button variant="contained" onClick={() => handLogin('b5cf61a4-9e1f-43c3-92c9-cfbc1af7b1d4', "ADMIN")}>Fake Login as Admin</Button>
                    <Button variant="contained" onClick={() => handLogin('915d92b9-ee02-4131-98c7-5ce67030739a', "MEMBER")}>Fake Login as Member</Button>
                </DialogActions>
            )}

            {!isAuthenticated && (
                <Box sx={{ maxWidth: 400, mx: "auto", mt: 10 }}>

                    <TextField
                        {...register("username")}
                        label="Username"
                        fullWidth
                        margin="normal"
                        error={!!errors.username}
                        helperText={errors.username ? errors.username.message : ""} />

                    <TextField
                        {...register("password")}
                        label="Password"
                        type="password"
                        fullWidth
                        margin="normal"
                        error={!!errors.password}
                        helperText={errors.password ? errors.password.message : ""} />

                    <Button
                        variant="contained"
                        color="primary"
                        fullWidth
                        onClick={handleSubmit((data) => {
                            console.log(data);
                            doLogin(data);
                        })}>
                        Login </Button>
                </Box>
            )}
        </Box>
    );
};