import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

type RequestType = 'leave' | 'wfh';

@Injectable({ providedIn: 'root' })
export class ManagerRequestService {

  private baseUrl = `${environment.apiUrl}/manager`;

  constructor(private http: HttpClient) {}

  private segment(type: RequestType): string {
    return type === 'leave' ? 'leaves' : 'work-request';
  }

  getPending(type: RequestType): Observable<any> {
    return this.http.get(`${this.baseUrl}/${this.segment(type)}/pending`);
  }

  approve(type: RequestType, id: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/${this.segment(type)}/${id}/approve`, {});
  }

  reject(type: RequestType, id: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/${this.segment(type)}/${id}/reject`, {});
  }
}
