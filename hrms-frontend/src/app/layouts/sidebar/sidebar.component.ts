import { Component, Input, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TokenStorageService } from '../../core/services/token-storage.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterLink, CommonModule],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent {
  private tokenStorage = inject(TokenStorageService);

  @Input() collapsed = false;
  role: string = '';

ngOnInit() {
  this.role = this.tokenStorage.getItem('role') || '';
}

}