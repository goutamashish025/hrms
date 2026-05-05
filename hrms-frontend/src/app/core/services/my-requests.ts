import { Component, OnInit } from '@angular/core';
import { RequestService } from '../services/request.service';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, ɵInternalFormsSharedModule } from '@angular/forms';


@Component({
  selector: 'app-my-requests',
  templateUrl: '../../features/requests/my-requests/my-requests.html',
  styleUrls: ['../../features/requests/my-requests/my-requests.scss'],
  imports: [CommonModule, ɵInternalFormsSharedModule]
})
export class MyRequests implements OnInit {

  requests: any[] = [];
  filteredRequests: any[] = [];
  filterType: string = 'ALL';

  constructor(private requestService: RequestService) {}

  ngOnInit(): void {
    this.loadRequests();
  }

  loadRequests() {
    this.requestService.getMyRequests().subscribe(res => {
      this.requests = res.data;
      this.filteredRequests = this.requests;
    });
  }

  filterRequests(type: string) {
    this.filterType = type;

    if (type === 'ALL') {
      this.filteredRequests = this.requests;
    } else {
      this.filteredRequests = this.requests.filter(r => r.type === type);
    }
  }
}