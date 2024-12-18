/// <reference types="@angular/localize" />

import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';
import { registerLocaleData } from '@angular/common';
import localeNlBe from '@angular/common/locales/nl-BE'; // Dutch (Belgium)

// Register Belgian Dutch locale globally
registerLocaleData(localeNlBe);

bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));
