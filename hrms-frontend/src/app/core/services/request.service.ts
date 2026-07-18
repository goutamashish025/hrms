import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RequestService {

  private apiUrl = `${environment.apiUrl}/requests`;

  constructor(private http: HttpClient) {}

  getMyRequests() {
    return this.http.get<any>(`${this.apiUrl}/my`);
  }
}