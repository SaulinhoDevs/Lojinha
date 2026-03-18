import { Produtos } from './../pages/produtos/produtos';
import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";

@Injectable({providedIn: 'root'})
export class ProductsService {

    static readonly BASE_PATH = 'http://localhost:8080';

    private http = inject(HttpClient);

    getProducts() {
        return this.http.get(`${ProductsService.BASE_PATH}/produtos`);
    }
}