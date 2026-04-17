import { LoginService } from './../../services/login-service';
import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { LoginRequest } from '../../services/interfaces/login-request';

@Component({
  selector: 'app-login',
  imports: [FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  erro = '';
  sucesso = '';

  router = inject(Router);
  loginService = inject(LoginService);

  constructor() {
    this.loginService.removerToken();
  }

  login: LoginRequest = {
    email: '',
    senha: '',
  };

  logar() {
    this.erro = '';
    this.sucesso = '';

    this.loginService.logar(this.login).subscribe({
      next: (resposta) => {
        if (resposta?.token) {
          this.loginService.addToken(resposta.token);
          this.sucesso = 'Login realizado com sucesso!';

          setTimeout(() => {
            this.router.navigate(['/inicio']);
          }, 1000);
        } else {
          this.erro = 'Login ou senha incorretos.';
        }
      },
      error: (err) => {
        if (err.status === 401) {
          this.erro = err.error?.message || 'Email ou senha inválidos.';
        } else {
          this.erro = 'Algum erro aconteceu. Tente novamente mais tarde.';
        }
      },
    });
  }
}
