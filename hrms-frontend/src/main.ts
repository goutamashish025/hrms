import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient, withFetch } from '@angular/common/http';
import { App } from './app/app';     // ✅ comes from app.ts
import { appConfig } from './app/app.config';

bootstrapApplication(App, {
  providers: [
    provideHttpClient(withFetch()),
    ...appConfig.providers
  ]
});
