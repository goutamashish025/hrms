import { Routes } from '@angular/router';
import { Login } from './features/login/login';
import { Register } from './features/register/register';
import { Dashboard } from './features/dashboard/dashboard.component';
import { authGuard } from './guards/auth-guard';
import { LayoutComponent } from './layouts/layout/layout.component';
import { ApplyLeaveComponent } from './features/leave/apply-leave/apply-leave';
import { MyLeaves } from './features/leave/my-leaves/my-leaves';
import { Attendance } from './features/attendance/attendance';
import { ManagerRequests } from './features/manager-requests/manager-requests';
import { ApplyWfh } from './features/attendance/apply-wfh/apply-wfh';
import { MyRequests } from './features/requests/my-requests/my-requests';
import { TeamManagementComponent } from './features/team/team-management/team-management';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  // public routes
  { path: 'login', component: Login },
  { path: 'register', component: Register },

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
      { path: 'leave-requests', component: ManagerRequests, data: { type: 'leave' } },
      { path: 'wfh-requests', component: ManagerRequests, data: { type: 'wfh' } },
      { path: 'apply-wfh', component: ApplyWfh },
      { path: 'my-requests', component: MyRequests },
      { path: 'teams', component: TeamManagementComponent },
      // later: employees, attendance, leaves, etc.
    ],
  },

  { path: '**', redirectTo: 'login' },
];
