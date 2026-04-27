import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LeaveService } from '../leave.service';

@Component({
  selector: 'app-my-leaves',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './my-leaves.html',
  styleUrls: ['./my-leaves.scss']
})
export class MyLeaves implements OnInit {

  // Full data from API
  leaves: any[] = [];

  // Data shown in table (paginated)
  pagedLeaves: any[] = [];

  // Pagination variables
  currentPage = 1;
  pageSize = 5;
  totalPages = 0;

  constructor(private leaveService: LeaveService) {}

  ngOnInit(): void {
    this.loadLeaves();
  }

  // Load data from API
  loadLeaves() {
    this.leaveService.getMyLeaves().subscribe((res: any) => {
      this.leaves = res;

      // Calculate total pages
      this.totalPages = Math.ceil(this.leaves.length / this.pageSize);

      // Load first page
      this.updatePage();
    });
  }

  // Update paginated data
  updatePage() {
    const start = (this.currentPage - 1) * this.pageSize;
    const end = start + this.pageSize;
    this.pagedLeaves = this.leaves.slice(start, end);
  }

  // Next page
  nextPage() {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.updatePage();
    }
  }

  // Previous page
  prevPage() {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.updatePage();
    }
  }

  // Cancel leave
  cancelLeave(id: number) {
    if (confirm('Are you sure you want to cancel this leave?')) {
      this.leaveService.cancelLeave(id).subscribe(() => {
        alert('Leave cancelled successfully');

        // Reload data after cancel
        this.loadLeaves();
      });
    }
  }
}