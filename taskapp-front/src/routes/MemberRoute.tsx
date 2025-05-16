import {JSX} from "react";
import {useAuth} from "../hooks/useAuth.tsx";
import {Navigate} from "react-router-dom";


export const MemberRoute = ({children} : {children: JSX.Element}) => {
    const auth = useAuth()
    if (auth.loading){
        return <div>Loading...</div>;
    }
    if (auth.userLogin != null && auth.userLogin.role != null) {
        return children;
    }
    return <Navigate to="/unauthorized" />;
}