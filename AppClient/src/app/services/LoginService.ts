import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { lastValueFrom } from "rxjs";
import { Login } from "../model/model";

@Injectable()
export class LoginService {

    private http = inject(HttpClient)

    postLogin(loginData: Login): Promise<any> {

        const body = {
            username: loginData.username,
            password: loginData.password
        }

        return lastValueFrom(this.http.post<any>('/api/login', body))

    }
}