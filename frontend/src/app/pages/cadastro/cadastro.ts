import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-cadastro',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './cadastro.html',
  styleUrl: './cadastro.css'
})
export class Cadastro {
  nome = '';
  email = '';
  senha = '';
  tipo = '';
  documento = '';
  erro = '';
  sucesso = '';
  saindo = false;

  constructor(private router: Router) {}
  onTipoChange() {
    this.documento = '';
  }

  aplicarMascara(event: Event) {
    const input = event.target as HTMLInputElement;
    let valor = input.value.replace(/\D/g, ''); // remove tudo que não é número

    if (this.tipo === 'CPF') {
      // Máscara: 000.000.000-00
      valor = valor.substring(0, 11);
      valor = valor
        .replace(/(\d{3})(\d)/, '$1.$2')
        .replace(/(\d{3})(\d)/, '$1.$2')
        .replace(/(\d{3})(\d{1,2})$/, '$1-$2');

    } else if (this.tipo === 'CNPJ') {
      // Máscara: 00.000.000/0000-00
      valor = valor.substring(0, 14);
      valor = valor
        .replace(/(\d{2})(\d)/, '$1.$2')
        .replace(/(\d{3})(\d)/, '$1.$2')
        .replace(/(\d{3})(\d)/, '$1/$2')
        .replace(/(\d{4})(\d{1,2})$/, '$1-$2');
    }

    this.documento = valor;
  }

    navegarCom(rota: string) {
    this.saindo = true;
    setTimeout(() => this.router.navigate([rota]), 500);
  }
  
  cadastrar() {
    this.erro = '';
    this.sucesso = '';

    if (!this.nome || !this.email || !this.senha || !this.tipo) {
      this.erro = 'Preencha todos os campos.';
      return;
    }

    if (!this.documento) {
      this.erro = `Preencha o ${this.tipo}.`;
      return;
    }

    this.router.navigate(['/login']);
  }
}