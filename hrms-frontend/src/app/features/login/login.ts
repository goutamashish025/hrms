import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth';
import { TokenStorageService } from '../../core/services/token-storage.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './login.html',
  styleUrls: ['./login.scss']
})
export class Login {
  email = '';
  password = '';
  errorMessage = '';

  constructor(
    private authService: AuthService,
    private router: Router,
    private tokenStorage: TokenStorageService
  ) {}

  login() {
  this.errorMessage = '';

  this.authService.login({ email: this.email, password: this.password })
    .subscribe({
      next: (response) => {
        console.log(response);

        // if (response.status === 'success') {
        //   localStorage.setItem('token', response.data);
        //   // Redirect to dashboard
        //   this.router.navigate(['/dashboard']);
        // } else {
        //   this.errorMessage = response.message || 'Invalid email or password!';
        // }
        if (response.status === 'success') {
  // Save token
  this.tokenStorage.setItem('token', response.data.token);

  // Save user role
  this.tokenStorage.setItem('role', response.data.role);

  // Optionally save whole user object
  this.tokenStorage.setItem('user', JSON.stringify(response.data));

  // Redirect to dashboard
  this.router.navigate(['/dashboard']);
} else {
  this.errorMessage = response.message || 'Invalid email or password!';
}

      },
      error: (err) => {
        console.error(err);
        this.errorMessage = 'Something went wrong. Please try again!';
      }
    });
}

}
