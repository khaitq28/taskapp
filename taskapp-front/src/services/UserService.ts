import {api} from "./api.ts";
import {User} from "../data/User.ts";

const USER_BASE_URL = '/users';

export const UserService = {

    getUserById: async (id: string | undefined) => {
        return (await api).get<User>(USER_BASE_URL + `/${id}`);
    },

    getAllUsers: async () => {
        return (await api).get<Object[]>(USER_BASE_URL);
    },

    async deleteUser(id: string) {
        return (await api).delete(USER_BASE_URL + `/${id}`);
    },

    async saveUser(user: User) {
        return (await api).post(USER_BASE_URL, user);
    }
}