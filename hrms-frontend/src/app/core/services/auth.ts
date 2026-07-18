import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { TokenStorageService } from './token-storage.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient, private tokenStorage: TokenStorageService) {}

  // login(credentials: any): Observable<any> {
  //   return this.http.post(`${this.apiUrl}/auth/login`, credentials);
  // }

  login(data: { email: string; password: string }): Observable<any> {
    return this.http.post(`${this.apiUrl}/auth/login`, data);
  }

  register(user: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/auth/register`, user);
  }

  logout() {
  this.tokenStorage.removeItem('token');
}

}
