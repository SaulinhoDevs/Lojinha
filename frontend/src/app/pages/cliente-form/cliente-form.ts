import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { NgxMaskDirective } from 'ngx-mask';
import { ClientsService } from '../../services/clients-service';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-cliente-form',
  standalone: true,
  imports: [ReactiveFormsModule, NgxMaskDirective],
  templateUrl: './cliente-form.html',
  styleUrl: './cliente-form.css',
})
export class ClienteForm implements OnInit {
  private fb = inject(FormBuilder);

  private clientsService = inject(ClientsService);

  private route = inject(ActivatedRoute);
  private router = inject(Router);

  formulario!: FormGroup;

  modoEdicao = false;
  clienteIdParaEdicao?: number;

  ngOnInit(): void {
    this.iniciarFormulario();
    this.configurarRegraDaDivida();

    const idDaUrl = this.route.snapshot.paramMap.get('id');
    if (idDaUrl) {
      this.modoEdicao = true;
      this.clienteIdParaEdicao = Number(idDaUrl);
      this.formulario.get('divida')?.disable();
      this.carregarClienteParaEdicao(this.clienteIdParaEdicao);
    }
  }

  private iniciarFormulario(): void {
    this.formulario = this.fb.group({
      nome: ['', [Validators.required]],

      temDivida: [false],

      divida: [{ value: null, disabled: true }],

      telefone: [''],
      rua: ['', [Validators.required]],
      bairro: ['', [Validators.required]],
      numero: [null, [Validators.required]],
    });
  }

  private configurarRegraDaDivida(): void {
    this.formulario.get('temDivida')?.valueChanges.subscribe((usuarioMarcouQueTemDivida) => {
      const campoDivida = this.formulario.get('divida');

      if (usuarioMarcouQueTemDivida) {
        campoDivida?.enable();
      } else {
        campoDivida?.disable();
        campoDivida?.setValue(null);
      }
    });
  }

  private carregarClienteParaEdicao(id: number): void {
    this.clientsService.buscarPorId(id).subscribe({
      next: (cliente) => {
        this.formulario.patchValue({
          nome: cliente.nome,
          divida: cliente.divida,
          rua: cliente.rua,
          bairro: cliente.bairro,
          numero: cliente.numero,
          telefone: cliente.telefone,
        });
      },
      error: (erro) => {
        console.error('Erro ao carregar cliente', erro);
        alert('Cliente não encontrado!');
        this.router.navigate(['/clientes']);
      },
    });
  }

  salvar(): void {
    if (this.formulario.invalid) {
      alert('Por favor, preencha todos os campos obrigatórios.');
      this.formulario.markAllAsTouched();
      return;
    }

    const dadosBrutos = this.formulario.getRawValue();

    if (this.modoEdicao && this.clienteIdParaEdicao) {
      this.clientsService.updateCliente(this.clienteIdParaEdicao, dadosBrutos).subscribe({
        next: () => {
          alert('Cliente atualizado com sucesso!');
          this.router.navigate(['/clientes']);
        },
        error: (erro: HttpErrorResponse) => {
          console.error('Erro ao atualizar', erro);
          alert('Erro ao atualizar cliente.');
        },
      });
    } else {
      this.clientsService.saveClient(dadosBrutos).subscribe({
        next: (clienteSalvo) => {
          alert(`Show de bola! Cliente ${clienteSalvo.nome} salvo com sucesso!`);
          this.formulario.reset();
        },
        error: (erro) => {
          console.error('Erro ao salvar:', erro);
          alert(
            'Ops! Deu ruim ao tentar salvar no banco de dados. Verifique se o Spring Boot está rodando.',
          );
        },
      });
    }
  }

  isCampoInvalido(nomeDoCampo: string): boolean {
    const campo = this.formulario.get(nomeDoCampo);
    return !!(campo && campo.invalid && (campo.touched || campo.dirty));
  }
}
