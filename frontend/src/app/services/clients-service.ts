import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";

@Injectable({providedIn: 'root'})
export class ClientsService {

    static readonly BASE_PATH = 'http://localhost:8080';

    private http = inject(HttpClient);

    getClients() {
        return this.http.get(`${ClientsService.BASE_PATH}/clientes`);
    }
}