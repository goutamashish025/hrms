import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-apply-wfh',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: '../../features/attendance/apply-wfh/apply-wfh.html',
  styleUrl: '../../features/attendance/apply-wfh/apply-wfh.scss'
})
export class ApplyWfh {

  wfh: any = {
    date: '',        // ✅ must match backend
    type: 'WFH',     // ✅ enum value
    reason: ''
  };

  constructor(private http: HttpClient) {}

  submit() {
    const token = (localStorage.getItem('token') || '').replace(/"/g, '');

    if (!this.wfh.date || !this.wfh.reason) {
      alert("Please fill all fields");
      return;
    }

    this.http.post(
      'http://localhost:8080/api/employee/work-request',
      this.wfh,
      {
        headers: new HttpHeaders({
          Authorization: `Bearer ${token}`
        })
      }
    ).subscribe({
      next: (res) => {
        console.log(res);
        alert("WFH request submitted ✅");

        // reset form
        this.wfh = {
          date: '',
          type: 'WFH',
          reason: ''
        };
      },
      error: (err) => {
        console.error(err);
        alert("Error submitting request ❌");
      }
    });
  }
}