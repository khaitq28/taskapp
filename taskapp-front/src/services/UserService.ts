import {api} from "./api.ts";
import {User} from "../data/User.ts";

const USER_BASE_URL = '/users';

export const UserService = {

    getUserById: (id: string | undefined) => {
        return api.get<User>(USER_BASE_URL + `/${id}`);
    },

    getAllUsers: () => {
        return api.get<Object[]>(USER_BASE_URL);
    },

    deleteUser(id: string) {
        return api.delete(USER_BASE_URL + `/${id}`);
    },

    saveUser(user: User) {
        return api.post(USER_BASE_URL, user);
    }
}