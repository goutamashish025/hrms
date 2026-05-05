// core/services/api.service.ts
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ApiService {

  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  private getHeaders() {
    const token = (localStorage.getItem('token') || '').replace(/"/g, '');

    return {
      headers: new HttpHeaders({
        Authorization: `Bearer ${token}`
      })
    };
  }

  get(url: string) {
    return this.http.get(`${this.baseUrl}${url}`, this.getHeaders());
  }

  post(url: string, body: any) {
    return this.http.post(`${this.baseUrl}${url}`, body, this.getHeaders());
  }

  put(url: string, body: any) {
    return this.http.put(`${this.baseUrl}${url}`, body, this.getHeaders());
  }
}