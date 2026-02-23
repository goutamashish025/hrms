// import { Routes } from '@angular/router';
// import { LoginComponent } from '../app/auth/login/login.component';
// import { RegisterComponent } from '../app/auth/register/register.component';
// import { Dashboard } from '../app/dashboard/dashboard.component';

// export const routes: Routes = [
//   { path: '', redirectTo: 'login', pathMatch: 'full' },
//   { path: 'login', component: LoginComponent },
//   { path: 'register', component: RegisterComponent },
//   { path: 'dashboard', component: Dashboard }
// ];
import { Routes } from '@angular/router';
import { LoginComponent } from '../app/auth/login/login.component';
import { RegisterComponent } from '../app/auth/register/register.component';
import { Dashboard } from '../app/dashboard/dashboard.component';
import { authGuard } from './guards/auth-guard';   // 👈 add this
import { LayoutComponent } from '../app/layout/layout.component';  // 👈 add this

// import { Routes } from '@angular/router';
// import { LoginComponent } from './app/auth/login/login.component';
// import { RegisterComponent } from './app/auth/register/register.component';
// import { Dashboard } from './app/dashboard/dashboard.component';
// import { LayoutComponent } from './app/layout/layout.component';
// import { authGuard } from './app/guards/auth-guard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  // public routes
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },

  // protected routes with layout (sidebar + navbar)
  {
    path: '',
    component: LayoutComponent,
    canActivate: [authGuard],
    children: [
      { path: 'dashboard', component: Dashboard },
      // later: employees, attendance, leaves, etc.
    ],
  },

  { path: '**', redirectTo: 'login' },
];
