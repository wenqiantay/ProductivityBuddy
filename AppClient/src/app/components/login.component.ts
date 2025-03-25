import { HttpClient } from '@angular/common/http';
import { Component, inject, OnInit } from '@angular/core';
import { gapi } from 'gapi-script';
import { lastValueFrom } from 'rxjs';
import { GoogleClient, Login } from '../model/model';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LoginService } from '../services/LoginService';

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
      this.router.navigate(['/dashboard'])
    }).catch(err => {
      console.log(err)
      alert(`Login failed: ${err.message || 'An unknown error occurred.'}`);
    })

  }


}



