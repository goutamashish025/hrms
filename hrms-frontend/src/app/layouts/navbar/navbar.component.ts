import { Component, EventEmitter, Output } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth';
import { TokenStorageService } from '../../core/services/token-storage.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
constructor(private authService: AuthService, private router: Router, private tokenStorage: TokenStorageService) {}

  
 @Output() toggle = new EventEmitter<void>();
logout() {
  const maybeObs: any = this.authService.logout();
  if (maybeObs && typeof maybeObs.subscribe === 'function') {
    maybeObs.subscribe({
      next: () => {
        this.clearSession();
      },
      error: () => {
        // Even if backend fails
        this.clearSession();
      }
    });
    return;
  }
  // logout returned no observable; clear session immediately
  this.clearSession();
}

clearSession() {
  this.tokenStorage.removeItem('token');
  this.tokenStorage.removeItem('role');
  this.router.navigate(['/login']);
}
}


