import { Inicio } from './pages/inicio/inicio';
import { Routes } from '@angular/router';

export const routes: Routes = [
{path: '', redirectTo: 'inicio', pathMatch: 'full'},
{path: 'inicio', component: Inicio},
];
