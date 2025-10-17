import {Box, Button, DialogActions, TextField} from "@mui/material";
import {useAuth} from "../hooks/useAuth.tsx";
import { useNavigate} from "react-router-dom";
import * as yup from "yup";
import {useForm} from "react-hook-form";
import {yupResolver} from "@hookform/resolvers/yup";
import { useState } from "react";
import {loadConfig} from "../config.ts";

type FormData = {
    username: string;
    password: string;
}

const schema = yup.object({
    username: yup.string().required("Email is required"),
    password: yup.string().required("Password is required"),
}).required();

export const Login = () => {
    const { loginFromGooglePopup, loginWithPassword, isAuthenticated } = useAuth();
    const navigate = useNavigate();
    const { register, handleSubmit, formState: { errors } } = useForm<FormData>({
        resolver: yupResolver(schema)
    });
    const [loginMethod, setLoginMethod] = useState<null | "email" | "google">(null);

    const doLogin = async  (data: FormData) => {
        await loginWithPassword(data.username, data.password);
        navigate("/", { replace: true });
    };

    const loginGoogle = async () => {
        const cfg = await loadConfig();
        const googleOAuthUrl = cfg.googleOAuthUrl;
        const oauthPopupOrigin = cfg.oauthPopupOrigin;

        window.open(
            googleOAuthUrl,
            "loginGoogle",
            "width=500,height=600"
        );

        window.addEventListener("message", async (event) => {
            if (event.origin !== oauthPopupOrigin) {
                console.warn("[FE] ignore message due to origin mismatch");
                return;
            }
            if (event.data?.type === "GOOGLE_LOGIN_SUCCESS") {
                console.log("[FE] about to call refresh, url");
                localStorage.setItem('refreshToken', event.data.refreshToken);
                try {
                    await loginFromGooglePopup();
                    console.log("[FE] refresh ok → navigate home");
                    navigate("/", { replace: true });
                } catch (e) {
                    console.error("[FE] refresh failed:", e);
                }
            }
        }, { once: true });
    };

    return (
        <Box>
            {!isAuthenticated && loginMethod === null && (
                <Box sx={{
                    minHeight: "100vh",
                    display: "flex",
                    flexDirection: "column",
                    alignItems: "center"
                }}>
                    <DialogActions>
                        <Button variant="contained" onClick={() => setLoginMethod("email")}>Login with Email</Button>
                        <Button variant="contained" onClick={() => { setLoginMethod("google"); loginGoogle(); }}>Login with Google</Button>
                    </DialogActions>
                </Box>
            )}

            {!isAuthenticated && loginMethod === "email" && (
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
                            doLogin(data);
                        })}>
                        Login
                    </Button>
                    <Button
                        fullWidth
                        sx={{ mt: 1 }}
                        onClick={() => setLoginMethod(null)}>
                        Back
                    </Button>
                </Box>
            )}
        </Box>
    );
}
