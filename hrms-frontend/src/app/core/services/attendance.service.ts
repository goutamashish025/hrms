import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AttendanceService {

  private baseUrl = 'http://localhost:8080/api/attendance';

  constructor(private http: HttpClient) {}

  private getHeaders() {
    const token = localStorage.getItem('token');
    return {
      headers: new HttpHeaders({
        Authorization: `Bearer ${token}`
      })
    };
  }

  checkIn() {
    return this.http.post(`${this.baseUrl}/check-in`, {}, this.getHeaders());
  }

  checkOut() {
    return this.http.post(`${this.baseUrl}/check-out`, {}, this.getHeaders());
  }

  getMyAttendance() {
    return this.http.get(`${this.baseUrl}/my`, this.getHeaders());
  }
}