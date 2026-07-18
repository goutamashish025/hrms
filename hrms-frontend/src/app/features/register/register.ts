import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],

  templateUrl: './register.html',
  styleUrl: './register.scss'
})
export class Register {
  user = {
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    role: '' ,
    phone: ''
  };

  constructor(private authService: AuthService) {}

  register() {
    this.authService.register(this.user).subscribe(
      (res) => {
        console.log('Registration successful:', res);
      },
      (err) => {
        console.error('Registration failed:', err);
      }
    );
  }
}

