import {useEffect, useState} from "react";
import {User} from "../../data/User.ts";
import {UserService} from "../../services/UserService.ts";
import {UsersTable} from "./UsersTable.tsx";


export const AdminPage = () => {

    const [users, setUsers] = useState<User[]>([]);
    const fetchAllUsers = async () => {
        UserService.getAllUsers().then(data => {
            setUsers(data.data.map(user => user.user));
        })
    };

    useEffect(() => {
        fetchAllUsers().catch(err=> console.log(err));
    }, []);

    const deleteUser = (id: number) => {
        let ok = window.confirm(`Are you sure you want to delete user with ID ${id}?`);
        if (!ok) return;
        UserService.deleteUser(id).then(() => {
            fetchAllUsers().catch(err=> console.log(err));
        }).catch(err => {
            console.error("Error deleting user:", err);
        });
    };

    return (
        <div className="admin-page">
            <h2>Admin Page</h2>
            <p>Welcome to the admin page!</p>

            {users.length === 0 && <p>No users found.</p>}
            {users.length > 0 && <UsersTable users={users} deleteUser={deleteUser} />}

        </div>
    );
};