import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { WfhService } from '../../../core/services/wfh.service';

@Component({
  selector: 'app-apply-wfh',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './apply-wfh.html',
  styleUrl: './apply-wfh.scss'
})
export class ApplyWfh {

  wfh: any = {
    date: '',
    type: 'WFH',
    reason: ''
  };

  constructor(private wfhService: WfhService) {}

  submit() {
    if (!this.wfh.date || !this.wfh.reason) {
      alert("Please fill all fields");
      return;
    }

    this.wfhService.submitWfhRequest(this.wfh).subscribe({
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