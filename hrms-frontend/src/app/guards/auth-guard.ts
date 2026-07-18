// src/app/guards/auth-guard.ts
import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { TokenStorageService } from '../core/services/token-storage.service';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);

  const token = inject(TokenStorageService).getItem('token');
  
  if (token) {
    return true;          // user logged in
  }

  router.navigate(['/login']);  // not logged in
  return false;
};
