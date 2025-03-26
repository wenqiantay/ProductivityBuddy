import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { lastValueFrom } from "rxjs";
import { Login, UserDetail } from "../model/model";

@Injectable()
export class LoginService {

    private http = inject(HttpClient)

    postLogin(loginData: Login): Promise<UserDetail> {

        const body = {
            username: loginData.username,
            password: loginData.password
        }

        return lastValueFrom(this.http.post<UserDetail>('/api/login', body))

    }
}