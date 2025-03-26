import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RegistrationService } from '../services/RegistrationService';

@Component({
  selector: 'app-verified',
  standalone: false,
  templateUrl: './verified.component.html',
  styleUrl: './verified.component.css'
})
export class VerifiedComponent implements OnInit{

  private route = inject(ActivatedRoute)
  private router = inject(Router)
  private registrationSvc = inject(RegistrationService)

  verificationMessage: string = 'Verifying...'
  errorMessage: string | null = null; 

  ngOnInit(): void {
    const token = this.route.snapshot.queryParamMap.get('token');

    if (token) {
      
      this.registrationSvc.verifiedEmail(token).subscribe({
        next: (res) => {
          if (res.status === 'success') {


            this.verificationMessage = '✅ Email successfully verified!';
            setTimeout(() => {
              
              this.router.navigate(['/login']);  
            }, 2000); 
          }
        },
        error: (err) => {
          
          this.errorMessage = '❌ Email verification failed. Please try again later.';
          console.error('Verification failed:', err);
        }
      });
    } else {
      this.errorMessage = '❌ Invalid or missing token.';
    }
  }

}
  