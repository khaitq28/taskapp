import {createContext, useEffect, useState} from "react";
import {UserLogin} from "../data/UserLogin.ts";
import {claimsToUser, parseJwt, refreshAccess, setAccess} from "./authClient.ts";


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
    const [token, setToken] = useState<string | null>(null);
    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
    const [loading, setLoading] = useState<boolean>(true);

    const setFromAccess = (access: string) => {
        setAccess(access);
        setToken(access);
        const claims = claimsToUser(parseJwt(access));
        console.log(access);
        console.log(claims);

        setIsAuthenticated(true);
        setUserLogin(claims);
    };

    // useEffect(() => {
    //     const storedToken = localStorage.getItem('token');
    //     const storedUserLogin = localStorage.getItem('userLogin');
    //     const storedIsAuthenticated = localStorage.getItem('isAuthenticated');
    //     if (storedToken && storedUserLogin && storedIsAuthenticated) {
    //         setToken(storedToken);
    //         setUserLogin(JSON.parse(storedUserLogin));
    //         setIsAuthenticated(JSON.parse(storedIsAuthenticated));
    //     }
    //     setLoading(false);
    // }, []);

    useEffect(() => {
        (async () => {
            try {
                const access = await refreshAccess();
                setFromAccess(access);
            } catch {
                setAccess(null);
                setToken(null);
                setUserLogin(null);
            } finally {
                setLoading(false);
            }
        })();
    }, []);

    const loginWithPassword = async (email: string, password: string) => {
        const resp = await fetch("http://localhost:8080/taskapp/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            credentials: "include", // để nhận refresh cookie nếu BE set ở /login
            body: JSON.stringify({ email, password })
        });
        if (!resp.ok) throw new Error("login_failed");
        const { access } = await resp.json();
        setFromAccess(access);
    };

    const loginFromGooglePopup = async () => {
        const access = await refreshAccess();
        setFromAccess(access);
    };


    const logout = async () => {
        await fetch("http://localhost:8080/taskapp/auth/logout", {
            method: "POST",
            credentials: "include"
        });
        setAccess(null);
        setToken(null);
        setUserLogin(null);
    };


    const login = (token: string, userLogin: UserLogin) => {
        setToken(token);
        setUserLogin(userLogin);
        setIsAuthenticated(true);
        setLoading(false);
        localStorage.setItem('isAuthenticated', JSON.stringify(true));
        localStorage.setItem('userLogin',  JSON.stringify(userLogin));
        localStorage.setItem('token', token);
        localStorage.setItem('loading', JSON.stringify(true));
    };

    // const logout = () => {
    //     setToken(null);
    //     setUserLogin(null);
    //     setIsAuthenticated(false);
    //     localStorage.removeItem('token');
    //     localStorage.removeItem('userLogin');
    //     localStorage.removeItem('isAuthenticated');
    //     localStorage.removeItem('loading');
    // };

    const value: AuthContextType = {
        userLogin,
        token,
        isAuthenticated: !!token,
        loading,
        loginWithPassword,
        loginFromGooglePopup,
        logout
    };

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;


};