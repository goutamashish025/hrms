import { Routes } from '@angular/router';
import { LoginComponent } from '../app/auth/login/login.component';
import { RegisterComponent } from '../app/auth/register/register.component';
import { Dashboard } from '../app/dashboard/dashboard.component';
import { authGuard } from './guards/auth-guard';   // 👈 add this
import { LayoutComponent } from '../app/layout/layout.component';  // 👈 add this
import { ApplyLeaveComponent } from '../app/leave/apply-leave/apply-leave';  // 👈 add thi
import { MyLeaves } from '../app/leave/my-leaves/my-leaves';  
import { Attendance } from './attendance/attendance';

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
      // { path: 'my-leaves', component: MyLeavesComponent }
      // later: employees, attendance, leaves, etc.
    ],
  },

  { path: '**', redirectTo: 'login' },
];
