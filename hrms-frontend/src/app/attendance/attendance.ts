import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AttendanceService } from './attendance.service';

@Component({
  selector: 'app-attendance',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './attendance.html',
  styleUrls: ['./attendance.scss']
})
export class Attendance implements OnInit {

  attendanceList: any[] = [];
  todayRecord: any = null;

  constructor(private attendanceService: AttendanceService) {}

  ngOnInit(): void {
    this.loadAttendance();
  }

  loadAttendance() {
    this.attendanceService.getMyAttendance().subscribe((res: any) => {
      this.attendanceList = res;

      const today = new Date().toISOString().split('T')[0];

      this.todayRecord = this.attendanceList.find(
        (a: any) => a.date === today
      );
    });
  }

  checkIn() {
    this.attendanceService.checkIn().subscribe(() => {
      alert('Checked in successfully');
      this.loadAttendance();
    });
  }

  checkOut() {
    this.attendanceService.checkOut().subscribe(() => {
      alert('Checked out successfully');
      this.loadAttendance();
    });
  }
}