import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";

@Injectable({providedIn: 'root'})
export class VendasService {

    static readonly BASE_PATH = 'http://localhost:8080';

    private http = inject(HttpClient);

    getVendas() {
        return this.http.get(`${VendasService.BASE_PATH}/vendas`);
    }
}