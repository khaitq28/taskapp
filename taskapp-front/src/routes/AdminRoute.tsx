import {JSX} from "react";
import {Navigate} from "react-router-dom";
import {useAuth} from "../hooks/useAuth.tsx";


export const AdminRoute = ({children}: {children: JSX.Element}) => {
    const auth = useAuth()
    console.log('admin: ' , auth)
    if (auth.loading) {
        return <div>Loading...</div>;
    }
    if (!auth || !auth.isAuthenticated || auth.userLogin?.role !== 'ADMIN') {
        return <Navigate to="/unauthorized" />;
    }
    return children;
}