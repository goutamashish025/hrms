import { Injectable, PLATFORM_ID, inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

@Injectable({ providedIn: 'root' })
export class TokenStorageService {
  private platformId = inject(PLATFORM_ID);
  private get isBrowser() {
    return isPlatformBrowser(this.platformId);
  }

  getItem(key: string): string | null {
    return this.isBrowser ? localStorage.getItem(key) : null;
  }

  setItem(key: string, value: string): void {
    if (this.isBrowser) localStorage.setItem(key, value);
  }

  removeItem(key: string): void {
    if (this.isBrowser) localStorage.removeItem(key);
  }
}
