import { Routes } from '@angular/router';
import { LoginComponent } from './core/services/login.component';
import { RegisterComponent } from './core/services/register.component';
import { Dashboard } from './features/dashboard/dashboard.component';
import { authGuard } from './guards/auth-guard';   // 👈 add this
import { LayoutComponent } from './layouts/layout/layout.component';  // 👈 add this
import { ApplyLeaveComponent } from '../app/core/services/apply-leave';  // 👈 add thi
import { MyLeaves } from './\core/services/my-leaves';  
import { Attendance } from './core/services/attendance';
import { SidebarComponent } from './layouts/sidebar/sidebar.component';
import { ManagerRequests } from './core/services/manager-requests';
import { ApplyWfh } from './core/services/apply-wfh';
import { MyRequests } from '../app/core/services/my-requests';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  // public routes
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },

  // { path: 'my-leaves', component: MyLeavesComponent },

  // protected routes with layout (sidebar + navbar)
  {
    path: '',
    component: LayoutComponent,
    canActivate: [authGuard],
    children: [
      { path: 'dashboard', component: Dashboard },
       { path: 'apply-leave', component: ApplyLeaveComponent },
       { path: 'my-leaves', component: MyLeaves },
       { path: 'attendance', component: Attendance },
       { path : 'leave-requests', component: ManagerRequests, data: { type: 'leave' } },
       { path : 'wfh-requests', component: ManagerRequests, data: { type: 'wfh' } },
       { path: 'apply-wfh', component: ApplyWfh },
       { path: 'my-requests', component: MyRequests },


      // { path: 'my-leaves', component: MyLeavesComponent }
      // later: employees, attendance, leaves, etc.
    ],
  },

  { path: '**', redirectTo: 'login' },
];
