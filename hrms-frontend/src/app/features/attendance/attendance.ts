import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AttendanceService } from '../../core/services/attendance.service';

@Component({
  selector: 'app-attendance',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './attendance.html',
  styleUrls: ['./attendance.scss']
})
export class Attendance implements OnInit {

  attendanceList: any[] = [];
  filteredAttendance: any[] = [];
  todayRecord: any = null;

  availableMonths: string[] = [];
  selectedMonth: string = '';

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

      const currentMonth = today.substring(0, 7);
      const monthsWithData = Array.from(
        new Set(this.attendanceList.map((a: any) => a.date.substring(0, 7)))
      );

      this.availableMonths = Array.from(new Set([currentMonth, ...monthsWithData]))
        .sort()
        .reverse();

      this.selectedMonth = this.availableMonths[0] || currentMonth;
      this.applyMonthFilter();
    });
  }

  onMonthChange() {
    this.applyMonthFilter();
  }

  applyMonthFilter() {
    this.filteredAttendance = this.attendanceList.filter(
      (a: any) => a.date.startsWith(this.selectedMonth)
    );
  }

  monthLabel(month: string): string {
    const [year, monthNum] = month.split('-');
    const date = new Date(Number(year), Number(monthNum) - 1, 1);
    return date.toLocaleString('default', { month: 'long', year: 'numeric' });
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