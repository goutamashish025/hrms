// import { Component, signal } from '@angular/core';
// import { RouterOutlet } from '@angular/router';

// @Component({
//   selector: 'app-root',
//   standalone: true,
//   imports: [RouterOutlet],
//   templateUrl: './app.html',
//   styleUrls: ['./app.scss']   // ✅ corrected
// })
// export class App {
//   protected readonly title = signal('hrms-frontend');
// }
// src/app/app.ts
// src/app/app.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet],   // 👈 NO LayoutComponent here
  templateUrl: './app.html',
})
export class App {}
