import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class WfhService {

  private apiUrl = `${environment.apiUrl}/employee/work-request`;

  constructor(private http: HttpClient) {}

  submitWfhRequest(data: any): Observable<any> {
    return this.http.post(this.apiUrl, data);
  }
}
