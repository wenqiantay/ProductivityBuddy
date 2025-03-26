import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { catchError, lastValueFrom, Observable, throwError } from "rxjs";
import { UserData } from "../model/model";

Injectable() 
export class RegistrationService {

    private http = inject(HttpClient)

    postRegistration(userData: UserData): Promise<any> {

        const body = {
            username: userData.username,
            name: userData.name,
            email: userData.email,
            gender: userData.gender,
            birthdate: userData.birthdate,
            password: userData.password
        }

        return lastValueFrom(this.http.post<any>('/api/register', body))
        
    }

    verifiedEmail(token: string): Observable<{ status: string; message: string }> {
        return this.http.get<{ status: string, message: string }>(`/api/verify?token=${token}`).pipe(
          catchError((error) => {
            console.error('Error verifying email:', error);
            return throwError(() => new Error('Email verification failed. Please try again later.'));
          })
        );
      }
      
}