import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  email = '';
  password = '';
  errorMessage = '';

  constructor(
    private authService: AuthService,
    private router: Router
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
  localStorage.setItem('token', response.data.token);

  // Save user role
  localStorage.setItem('role', response.data.role);

  // Optionally save whole user object
  localStorage.setItem('user', JSON.stringify(response.data));

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
