import { Component, OnInit } from '@angular/core';
import { LeaveService } from '../../core/services/leave.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-apply-leave',
  standalone: true,
  templateUrl: '../../features/leave/apply-leave/apply-leave.html',
  imports: [FormsModule, CommonModule]
})
export class ApplyLeaveComponent implements OnInit {

  leaveTypes: any[] = [];

  leave: any = {
    leaveTypeId: '',
    startDate: '',
    endDate: '',
    reason: ''
  };

  constructor(private leaveService: LeaveService) {}

  ngOnInit(): void {
    this.loadLeaveTypes();
  }

  // ✅ ONLY ONE METHOD (clean)
  loadLeaveTypes(): void {
    this.leaveService.getLeaveTypes().subscribe({
      next: (res: any) => {

        console.log("API RESPONSE 👉", res);

        // handle both formats
        this.leaveTypes = res.data || res;

        console.log("ASSIGNED 👉", this.leaveTypes);
      },
      error: (err) => {
        console.error("Error loading leave types", err);
      }
    });
  }

  submit(): void {

    // ✅ validation
    if (!this.leave.leaveTypeId) {
      alert("Please select leave type");
      return;
    }

    this.leaveService.applyLeave(this.leave).subscribe({
      next: () => {
        alert('Leave Applied Successfully ✅');

        // reset form
        this.leave = {
          leaveTypeId: '',
          startDate: '',
          endDate: '',
          reason: ''
        };
      },
      error: (err) => {
        console.error(err);
        alert(err?.error?.message || 'Error occurred');
      }
    });
  }
}