import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./lista-visitantes/lista-visitantes.component').then(m => m.ListaVisitantesComponent)
  },
  {
    path: 'cadastro',
    loadComponent: () =>
      import('./cadastro-visitantes/cadastro-visitantes.component').then(m => m.CadastroVisitantesComponent)
  }
];

