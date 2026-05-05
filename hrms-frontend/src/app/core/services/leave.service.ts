import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LeaveService {

  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  private getHeaders() {
    const token = localStorage.getItem('token');

    if (!token) {
      throw new Error('No authentication token found');
    }

    return {
      headers: new HttpHeaders({
        'Authorization': 'Bearer ' + token,
        'Content-Type': 'application/json'
      })
    };
  }

  getLeaveTypes(): Observable<any> {
    return this.http.get(`${this.baseUrl}/leaves/types`, this.getHeaders());
  }

  applyLeave(data: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/leaves/apply`, data, this.getHeaders());
  }

  getMyLeaves(): Observable<any> {
    return this.http.get(`${this.baseUrl}/leaves/my`, this.getHeaders());
  }

  cancelLeave(id: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/leaves/${id}/cancel`, {}, this.getHeaders());
  }
}