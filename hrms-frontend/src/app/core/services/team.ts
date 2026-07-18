import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TeamService {

  private baseUrl = `${environment.apiUrl}/teams`;

  constructor(private http: HttpClient) {}

  getTeams(): Observable<any> {
    return this.http.get(this.baseUrl);
  }

  createTeam(data: any): Observable<any> {
    return this.http.post(this.baseUrl, data);
  }

  getTeamMembers(teamId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/${teamId}/members`);
  }

  addMembers(teamId: number, data: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/${teamId}/members`, data);
  }

  assignManager(teamId: number, userId: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/${teamId}/manager/${userId}`, {});
  }

  removeMember(teamId: number, userId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${teamId}/members/${userId}`);
  }
}