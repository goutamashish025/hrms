import { Component } from '@angular/core';

@Component({
  selector: 'app-dashboard',
  imports: [],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class Dashboard {
    // role: string | null = null;

    role = localStorage.getItem('role');

  ngOnInit(): void {
    // ✅ get role from localStorage
    this.role = localStorage.getItem('role');
  }
  logout() {
    localStorage.removeItem('token'); 
    window.location.href = '/login'; // Redirect to login page
  }

}
