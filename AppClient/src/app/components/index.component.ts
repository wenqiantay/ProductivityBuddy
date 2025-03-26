import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-index',
  standalone: false,
  templateUrl: './index.component.html',
  styleUrl: './index.component.css'
})
export class IndexComponent {
  constructor(private router: Router) {}

  // Handle the Login button click
  onLoginClick() {
    this.router.navigate(['/login']);
  }

  // Handle the Sign Up button click
  onSignupClick() {
    this.router.navigate(['/register']);
  }
}
