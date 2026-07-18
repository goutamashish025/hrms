import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LeaveService {

  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getLeaveTypes(): Observable<any> {
    return this.http.get(`${this.baseUrl}/leaves/types`);
  }

  applyLeave(data: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/leaves/apply`, data);
  }

  getMyLeaves(): Observable<any> {
    return this.http.get(`${this.baseUrl}/leaves/my`);
  }

  cancelLeave(id: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/leaves/${id}/cancel`, {});
  }
}