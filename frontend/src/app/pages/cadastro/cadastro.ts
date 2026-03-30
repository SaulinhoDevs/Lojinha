import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-cadastro',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './cadastro.html',
  styleUrl: './cadastro.css'
})
export class Cadastro {
  nome = '';
  email = '';
  senha = '';
  tipo = '';
  erro = '';
  sucesso = '';

constructor(private router: Router) {}

  cadastrar() {
    this.erro = '';
    this.sucesso = '';

    if (!this.nome || !this.email || !this.senha || !this.tipo) {
      this.erro = 'Preencha todos os campos.';
      return;
    }

    // Aqui você chama seu serviço de cadastro
    // this.authService.cadastrar(...)

    this.router.navigate(['/login']);
  }
}