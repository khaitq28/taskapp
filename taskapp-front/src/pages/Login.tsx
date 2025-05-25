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

    const handLogin = (id : number, role: string) => {
        login("FAKE_TOKEN",
            {
                id: id,
                name: "Tony " + role,
                email: "tony@example.com",
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
                    <Button variant="contained" onClick={() => handLogin(1, "ADMIN")}>Fake Login as Admin</Button>
                    <Button variant="contained" onClick={() => handLogin(2, "MEMBER")}>Fake Login as Member</Button>
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