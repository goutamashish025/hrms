import { Component, inject } from '@angular/core';
import { TokenStorageService } from '../../core/services/token-storage.service';

@Component({
  selector: 'app-dashboard',
  imports: [],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class Dashboard {
    private tokenStorage = inject(TokenStorageService);

    role = this.tokenStorage.getItem('role');

  ngOnInit(): void {
    this.role = this.tokenStorage.getItem('role');
  }
  logout() {
    this.tokenStorage.removeItem('token');
    window.location.href = '/login'; // Redirect to login page
  }

}
