import { ApplicationConfig, LOCALE_ID, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideHttpClient } from '@angular/common/http';

import { DateAdapter, NativeDateAdapter, MAT_DATE_FORMATS } from '@angular/material/core'; // Import DateAdapter and NativeDateAdapter
import { MAT_NATIVE_DATE_FORMATS } from '@angular/material/core'; // Import the default date formats

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideAnimationsAsync(),
    provideHttpClient(),
    { provide: LOCALE_ID, useValue: 'nl-BE' }, // Set Belgian locale globally
    { provide: DateAdapter, useClass: NativeDateAdapter },  // Use NativeDateAdapter
    { provide: MAT_DATE_FORMATS, useValue: MAT_NATIVE_DATE_FORMATS },  // Use default native date formats
  ]
};
