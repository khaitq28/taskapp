import {api} from "./api.ts";
import {User} from "../data/User.ts";

const USER_BASE_URL = '/users';

export const UserService = {

    getUserById: (id: number | undefined) => {
        return api.get<User>(USER_BASE_URL + `/${id}`);
    }


}