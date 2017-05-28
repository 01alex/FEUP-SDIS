import { User } from '../models/users';

export interface Repos {
    name: string,
    full_name: string,
    owner: User, 
    private: boolean,
    html_url: string,
    description: string,
    fork: boolean,
    url: string
}