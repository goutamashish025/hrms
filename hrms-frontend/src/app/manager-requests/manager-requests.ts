import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-manager-requests',
  templateUrl: './manager-requests.html',
  styleUrl: './manager-requests.scss',
  standalone: true,
  imports: [CommonModule]
})
export class ManagerRequests implements OnInit {

  requests: any[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.loadRequests();
  }

  loadRequests() {
    const token = localStorage.getItem('token');

    if (!token) {
      console.error("No authentication token found ❌");
      alert("Please login again");
      return;
    }

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    this.http.get('http://localhost:8080/api/manager/leaves/pending', { headers })
      .subscribe({
        next: (res: any) => {
          console.log("API RESPONSE 👉", res);

          // ✅ handle both formats
          this.requests = res.data || res;
        },
        error: (err: any) => {
          console.error("API ERROR ❌", err);
        }
      });
  }

  approve(id: number) {
  const token = localStorage.getItem('token');

  this.http.put(`http://localhost:8080/api/manager/leaves/${id}/approve`, {}, {
    headers: {
      Authorization: `Bearer ${token}`
    }
  }).subscribe({
    next: () => {
      alert("Approved ✅");
      this.loadRequests(); // refresh
    },
    error: (err) => {
      console.error(err);
    }
  });
}


reject(id: number) {
  const token = localStorage.getItem('token');

  this.http.put(`http://localhost:8080/api/manager/leaves/${id}/reject`, {}, {
    headers: {
      Authorization: `Bearer ${token}`
    }
  }).subscribe({
    next: () => {
      alert("Rejected ❌");
      this.loadRequests(); // refresh
    },
    error: (err) => {
      console.error(err);
    }
  });
}
}