import {createContext, useEffect, useState} from "react";
import {UserLogin} from "../data/UserLogin.ts";
import {claimsToUser, parseJwt, refreshAccess, setAccess} from "./authClient.ts";
import {loadConfig} from "../config.ts";


// type AuthContextType = {
//     userLogin: UserLogin | null;
//     token: string | null;
//     isAuthenticated: boolean;
//     setIsAuthenticated: (isAuthenticated: boolean) => void;
//     login: (token: string, user: UserLogin) => void;
//     logout: () => void,
//     loading: boolean;
// };


type AuthContextType = {
    userLogin: UserLogin | null;
    token: string | null;                 // access (in-memory)
    isAuthenticated: boolean;
    loading: boolean;
    loginWithPassword: (email: string, password: string) => Promise<void>;
    loginFromGooglePopup: () => Promise<void>;    // gọi sau khi nhận postMessage
    logout: () => Promise<void>;
};


export const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
    const [userLogin, setUserLogin] = useState<UserLogin | null>(null);
    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
    const [loading, setLoading] = useState<boolean>(true);
    const [token, setToken] = useState<string | null>(null);

    const setFromAccess = (access: string) => {
        setAccess(access);
        setToken(access);
        const claims = parseJwt(access);
        setUserLogin(claimsToUser(claims));
        setIsAuthenticated(true); // Set authenticated when we have a valid token
    };

    useEffect(() => {
        (async () => {
            try {
                const access = await refreshAccess();
                setFromAccess(access);
            } catch {
                setAccess(null);
                setUserLogin(null);
            } finally {
                setLoading(false);
            }
        })();
    }, []);

    const loginWithPassword = async (email: string, password: string) => {

        const cfg = await loadConfig();
        const backendUrl = cfg.backendBaseUrl;

        const resp = await fetch(`${backendUrl}/auth/login`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            credentials: "include",
            body: JSON.stringify({ email, password }),
        });
        if (!resp.ok) throw new Error("Invalid credentials");
        const { access } = await resp.json();
        setIsAuthenticated(true);
        setFromAccess(access);
    };

    const loginFromGooglePopup = async () => {
        try {
            const access = await refreshAccess();
            setFromAccess(access);
            console.log("Google login successful, user authenticated");
        } catch (error) {
            console.error("Google login failed:", error);
            setIsAuthenticated(false);
            setUserLogin(null);
            setToken(null);
        }
    };


    const logout = async () => {

        const config = await loadConfig();
        const backendUrl = config.backendBaseUrl;

        await fetch(`${backendUrl}/auth/logout`, {
            method: "POST",
            credentials: "include",
        });
        setAccess(null);
        setUserLogin(null);
        setToken(null);
        setIsAuthenticated(false);
    };

    const value: AuthContextType = {
        userLogin,
        token,
        isAuthenticated, // use the local state
        loading,
        loginWithPassword,
        loginFromGooglePopup,
        logout
    };

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;


};