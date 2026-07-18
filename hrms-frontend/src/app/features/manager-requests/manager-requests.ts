import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { ManagerRequestService } from '../../core/services/manager-request.service';

@Component({
  selector: 'app-manager-requests',
  templateUrl: './manager-requests.html',
  styleUrl: './manager-requests.scss',
  standalone: true,
  imports: [CommonModule]
})
export class ManagerRequests implements OnInit {

  requests: any[] = [];
  requestType: 'leave' | 'wfh' = 'leave';

  constructor(private managerRequestService: ManagerRequestService, private route: ActivatedRoute) {}

  ngOnInit() {
    this.requestType = this.route.snapshot.data['type'] || 'leave';
    this.loadRequests();
  }

  loadRequests() {
    this.managerRequestService.getPending(this.requestType).subscribe({
      next: (res: any) => {
        this.requests = res.data || res;
      },
      error: (err) => console.error('API ERROR ❌', err)
    });
  }

  approve(id: number) {
    this.managerRequestService.approve(this.requestType, id).subscribe({
      next: () => {
        alert('Approved ✅');
        this.loadRequests();
      },
      error: (err) => console.error(err)
    });
  }

  reject(id: number) {
    this.managerRequestService.reject(this.requestType, id).subscribe({
      next: () => {
        alert('Rejected ❌');
        this.loadRequests();
      },
      error: (err) => console.error(err)
    });
  }
}