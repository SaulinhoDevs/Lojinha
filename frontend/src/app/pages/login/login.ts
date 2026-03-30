import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  imports: [
    FormsModule, RouterLink,
  ],
  templateUrl: './login.html',
  styleUrl: './login.css',
})

export class Login {

  email = '';
  senha = '';
  erro = '';
  sucesso = '';

  constructor(private router: Router) {}

  logar() {
    this.erro = '';
    this.sucesso = '';

    if (!this.email || !this.senha) {
      this.erro = 'Preencha todos os campos.';
      return;
    }

    // Aqui você chama seu serviço de cadastro
    // this.authService.logar(...)

    this.router.navigate(['/inicio']);
  }
}
