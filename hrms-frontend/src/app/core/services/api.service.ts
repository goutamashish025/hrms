import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ApiService {

  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  get(url: string) {
    return this.http.get(`${this.baseUrl}${url}`);
  }

  post(url: string, body: any) {
    return this.http.post(`${this.baseUrl}${url}`, body);
  }

  put(url: string, body: any) {
    return this.http.put(`${this.baseUrl}${url}`, body);
  }
}