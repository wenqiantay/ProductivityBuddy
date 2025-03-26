import { Component, inject, OnInit } from '@angular/core';
import { UserData } from '../model/model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { RegistrationService } from '../services/RegistrationService';

@Component({
  selector: 'app-sign-up',
  standalone: false,
  templateUrl: './sign-up.component.html',
  styleUrl: './sign-up.component.css'
})
export class SignUpComponent implements OnInit {

  private fb = inject(FormBuilder)
  protected form !: FormGroup
  private route = inject(Router)
  private registrationSvc = inject(RegistrationService)

  protected userData !: UserData
  private router = inject(Router)
  
ngOnInit(): void {
  this.createForm()
}

createForm() {
  this.form = this.fb.group({
    username: this.fb.control<string>('', [Validators.required]),
    name: this.fb.control<string>('', [Validators.required]),
    gender: this.fb.control<string>('', [Validators.required]),
    email: this.fb.control<string>('', [Validators.email]),
    birthdate: this.fb.control<string>('',[Validators.required]),
    password: this.fb.control<string>('', [Validators.required]),
  })
}

processForm() {
  const username = this.form.get('username')?.value
  const name = this.form.get('name')?.value
  const gender = this.form.get('gender')?.value
  const email = this.form.get('email')?.value
  const birthdate = this.form.get('birthdate')?.value
  const password = this.form.get('password')?.value

  this.userData = {
    name: name,
    username: username,
    gender: gender,
    email: email,
    birthdate: birthdate,
    password: password
  }

  //Post to back end to save into SQL
  this.registrationSvc.postRegistration(this.userData).then(
    response => {
      console.log(response)
    
      this.router.navigate(['/verifyemail'])
    }
  ).catch(err => {
    console.log(err)
  })
}

goBack() {
  this.router.navigate(['/'])
}

}
