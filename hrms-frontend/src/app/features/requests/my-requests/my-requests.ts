import { Component, OnInit } from '@angular/core';
import { RequestService } from '../../../core/services/request.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-my-requests',
  templateUrl: './my-requests.html',
  styleUrls: ['./my-requests.scss'],
  imports: [CommonModule, FormsModule]
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