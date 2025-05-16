import {createContext, useEffect, useState} from "react";
import {UserLogin} from "../data/UserLogin.ts";


type AuthContextType = {
    userLogin: UserLogin | null;
    token: string | null;
    isAuthenticated: boolean;
    setIsAuthenticated: (isAuthenticated: boolean) => void;
    login: (token: string, user: UserLogin) => void;
    logout: () => void,
    loading: boolean;
};


export const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
    const [userLogin, setUserLogin] = useState<UserLogin | null>(null);
    const [token, setToken] = useState<string | null>(null);
    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {

        const storedToken = localStorage.getItem('token');
        const storedUserLogin = localStorage.getItem('userLogin');
        const storedIsAuthenticated = localStorage.getItem('isAuthenticated');
        if (storedToken && storedUserLogin && storedIsAuthenticated) {
            setToken(storedToken);
            setUserLogin(JSON.parse(storedUserLogin));
            setIsAuthenticated(JSON.parse(storedIsAuthenticated));
        }
        setLoading(false);
    }, []);

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

    const logout = () => {
        setToken(null);
        setUserLogin(null);
        setIsAuthenticated(false);
        localStorage.removeItem('token');
        localStorage.removeItem('userLogin');
        localStorage.removeItem('isAuthenticated');
        localStorage.removeItem('loading');
    };

    return (

        <AuthContext.Provider value={{ userLogin, token, isAuthenticated, setIsAuthenticated, login, logout, loading }}>
            {children}
        </AuthContext.Provider>
    );
}