import { Component, inject } from '@angular/core';
import { CategoriasService } from '../../services/categorias-service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-categorias',
  imports: [CommonModule],
  templateUrl: './categorias.html',
  styleUrl: './categorias.css',
})
export class Categorias {

  private categoriasService = inject(CategoriasService);

  public categorias: any[] = [];

  ngOnInit() {
    this.categoriasService.getCategorias().subscribe((response: any) => {
      console.log(response);
      this.categorias = response;
    });
  }

}
