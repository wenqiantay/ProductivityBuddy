import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { UserStore } from './user.store'; // Adjust the import if needed

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router, private userStore: UserStore) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean> | Promise<boolean> | boolean {
    let isLoggedIn = false;
    this.userStore.userId$.subscribe(userId => {
      isLoggedIn = !!userId;
    });
    if (!isLoggedIn) {
      alert('You need to log in first!');
      this.router.navigate(['/login']);
      return false;
    }
    return true;
  }
}