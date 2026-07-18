import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AttendanceService {

  private baseUrl = `${environment.apiUrl}/attendance`;

  constructor(private http: HttpClient) {}

  checkIn() {
    return this.http.post(`${this.baseUrl}/check-in`, {});
  }

  checkOut() {
    return this.http.post(`${this.baseUrl}/check-out`, {});
  }

  getMyAttendance() {
    return this.http.get(`${this.baseUrl}/my`);
  }
}