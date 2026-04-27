import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient, withFetch,withInterceptors } from '@angular/common/http';
import { App } from './app/app';     // ✅ comes from app.ts
import { appConfig } from './app/app.config';
// import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './app/auth-interceptor';

providers: [
  provideHttpClient(withInterceptors([authInterceptor]))
]

bootstrapApplication(App, {
  providers: [
    provideHttpClient(withFetch()),
    ...appConfig.providers
  ]
});
