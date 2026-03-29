import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Cliente } from './interfaces/cliente.model';

@Injectable({ providedIn: 'root' })
export class ClientsService {
  static readonly BASE_PATH = 'http://localhost:8080';

  private http = inject(HttpClient);

  getClients(): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(`${ClientsService.BASE_PATH}/clientes`);
  }

  saveClient(dadosBrutos: any): Observable<Cliente> {
    let telefoneTratado = dadosBrutos.telefone;
    if (telefoneTratado) {
      telefoneTratado = telefoneTratado.replace(/\D/g, '');
    }

    const clienteParaSalvar: Cliente = {
      nome: dadosBrutos.nome,
      divida: dadosBrutos.divida ?? 0.0,
      telefone: telefoneTratado,
      rua: dadosBrutos.rua,
      bairro: dadosBrutos.bairro,
      numero: dadosBrutos.numero,
    };

    return this.http.post<Cliente>(`${ClientsService.BASE_PATH}/clientes`, clienteParaSalvar);
  }

  buscarPorId(id: number): Observable<Cliente> {
    return this.http.get<Cliente>(`${ClientsService.BASE_PATH}/clientes/${id}`);
  }

}
