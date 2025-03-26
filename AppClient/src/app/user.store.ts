import { Injectable } from "@angular/core";
import { ComponentStore } from '@ngrx/component-store';


export interface UserState {
    userId: string | null
    spotifyToken: string | null
    userName: string | null
    email: string | null
    todoList: string[];
}

@Injectable()
export class UserStore extends ComponentStore<UserState> {

    constructor() {
        super({
            userId: null,
            spotifyToken: null,
            todoList: [],
            userName: null,
            email: null
        })
    }

    //Updaters
    readonly setUserId = this.updater((state, userId: string) => ({
        ...state,
        userId
    }))

    readonly setSpotifyToken = this.updater((state, token: string) => ({
        ...state,
        spotifyToken: token
    }))

    readonly setTodoList = this.updater((state, todos: string[]) => ({
        ...state,
        todoList: todos
    }))

    readonly setUserName = this.updater((state, userName: string) => ({
        ...state,
        userName
    }))

    readonly setEmail = this.updater((state, email: string) => ({
        ...state,
        email
    }));

    readonly addTodo = this.updater((state, todo: string) => ({
        ...state,
        todoList: [...state.todoList, todo]
    }))

    readonly deleteTodo = this.updater((state, index: number) => ({
        ...state,
        todoList: state.todoList.filter((_, i) => i !== index)
    }))

    

    //Selectors
    readonly userId$ = this.select((state) => state.userId)
    readonly spotifyToken$ = this.select((state) => state.spotifyToken)
    readonly todoList$ = this.select((state) => state.todoList)
    readonly userName$ = this.select((state) => state.userName)
    readonly email$ = this.select((state) => state.email)
}