import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-manager-requests',
  templateUrl: '../../features/manager-requests/manager-requests.html',
  styleUrl: '../../features/manager-requests/manager-requests.scss',
  standalone: true,
  imports: [CommonModule]
})
export class ManagerRequests implements OnInit {

  requests: any[] = [];
  requestType: string = 'leave';

  constructor(private http: HttpClient, private route: ActivatedRoute) {}

  ngOnInit() {
    this.requestType = this.route.snapshot.data['type'] || 'leave';
    this.loadRequests();
  }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    if (!token) {
      alert('Please login again');
      throw new Error('No token');
    }
    return new HttpHeaders({ Authorization: `Bearer ${token}` });
  }

  loadRequests() {
    const headers = this.getHeaders();

    const url = this.requestType === 'leave'
      ? 'http://localhost:8080/api/manager/leaves/pending'
      : 'http://localhost:8080/api/manager/work-request/pending';

    this.http.get(url, { headers }).subscribe({
      next: (res: any) => {
        this.requests = res.data || res;
      },
      error: (err) => console.error('API ERROR ❌', err)
    });
  }

  approve(id: number) {
    const headers = this.getHeaders();

    const url = this.requestType === 'leave'
      ? `http://localhost:8080/api/manager/leaves/${id}/approve`
      : `http://localhost:8080/api/manager/work-request/${id}/approve`;

    this.http.put(url, {}, { headers }).subscribe({
      next: () => {
        alert('Approved ✅');
        this.loadRequests();
      },
      error: (err) => console.error(err)
    });
  }

  reject(id: number) {
    const headers = this.getHeaders();

    const url = this.requestType === 'leave'
      ? `http://localhost:8080/api/manager/leaves/${id}/reject`
      : `http://localhost:8080/api/manager/work-request/${id}/reject`;

    this.http.put(url, {}, { headers }).subscribe({
      next: () => {
        alert('Rejected ❌');
        this.loadRequests();
      },
      error: (err) => console.error(err)
    });
  }
}