import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Categorias } from '../pages/categorias/categorias';
import { Categoria } from './interfaces/categoria.model';

@Injectable({ providedIn: 'root' })
export class CategoriasService {
  static readonly BASE_PATH = 'http://localhost:8080';

  private readonly http = inject(HttpClient);

  getCategorias(): Observable<Categorias[]> {
    return this.http.get<Categorias[]>(`${CategoriasService.BASE_PATH}/categorias`);
  }

  saveCategoria(dadosBrutos: any): Observable<Categoria> {
    const categoriaParaSalvar: Categoria = {
      nome: dadosBrutos.nome,
      descricao: dadosBrutos.descricao,
    };

    return this.http.post<Categoria>(
      `${CategoriasService.BASE_PATH}/categorias`,
      categoriaParaSalvar,
    );
  }
}
