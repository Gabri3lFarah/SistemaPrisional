import { provideHttpClient } from '@angular/common/http';
import { ApplicationConfig } from '@angular/core';
import { ApiService } from './services/api.service'; // 🔥 Caminho correto para `ApiService`
import { importProvidersFrom } from '@angular/core';
import { FormsModule } from '@angular/forms';

export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(), // 🔥 Habilita requisições HTTP no Angular
    ApiService,          // 🔥 Garante que `ApiService` possa ser injetado corretamente
    importProvidersFrom(FormsModule) // 🔥 Adiciona suporte ao `ngModel`
  ]
};
