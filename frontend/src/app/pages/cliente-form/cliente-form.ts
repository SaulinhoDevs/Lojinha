import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { NgxMaskDirective } from 'ngx-mask';
import { ClientsService } from '../../services/clients-service';

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

  formulario!: FormGroup;

  ngOnInit(): void {
    this.iniciarFormulario();
    this.configurarRegraDaDivida();
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

  salvar(): void {
    if (this.formulario.invalid) {
      alert('Por favor, preencha todos os campos obrigatórios.');
      return;
    }

    const dadosBrutos = this.formulario.getRawValue();

    this.clientsService.saveClient(dadosBrutos).subscribe({
      next: (clienteSalvo) => {
        alert(`Show de bola! Cliente ${clienteSalvo.nome} salvo com sucesso!`);
        this.formulario.reset();
      },
      error: (erro) => {
        console.error('Erro ao salvar:', erro);
        alert('Ops! Deu ruim ao tentar salvar no banco de dados. Verifique se o Spring Boot está rodando.');
      }
    });
  }

}
