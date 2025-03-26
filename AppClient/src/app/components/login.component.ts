import { HttpClient } from '@angular/common/http';
import { Component, inject, OnInit } from '@angular/core';
import { gapi } from 'gapi-script';
import { lastValueFrom } from 'rxjs';
import { GoogleClient, Login } from '../model/model';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LoginService } from '../services/LoginService';
import { UserStore } from '../user.store';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit{

  private fb = inject(FormBuilder)
  protected form !: FormGroup

  protected loginData !: Login

  private loginSvc = inject(LoginService)
  private router = inject(Router)

  private userStore = inject(UserStore)

  ngOnInit(): void {
    
    this.createForm()
    
  }

  createForm() {
    this.form = this.fb.group({
      username: this.fb.control<string>('', [Validators.required]),
      password: this.fb.control<string>('', Validators.required)
    })
  }

  processForm() {
    const username = this.form.get('username')?.value
    const password = this.form.get('password')?.value

    this.loginData = {
      username: username,
      password: password
    }

    this.loginSvc.postLogin(this.loginData).then( response => {
      console.log(response)

      const userId = response.userId
      const username = response.username
      const email = response.email

      this.userStore.setEmail(email)
      this.userStore.setUserId(userId)
      this.userStore.setUserName(username)

      localStorage.setItem('userData', JSON.stringify({
        userId,
        username,
        email
      }))
      
      this.router.navigate(['/dashboard'])
    }).catch(err => {
      console.log(err)
      alert(`Login failed: ${err.message || 'An unknown error occurred.'}`)
    })

  }

  goBack() {
    this.router.navigate(['/'])
  }


}



