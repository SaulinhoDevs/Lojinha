import { Component, ElementRef, inject, OnInit, signal, ViewChild } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { NgClass } from '@angular/common';
import { ChatService } from '../services/chat-service';
import { catchError, throwError } from 'rxjs';

@Component({
  selector: 'app-chat-bot',
  imports: [
    MatCardModule,
    MatToolbarModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    FormsModule,
    NgClass,
  ],
  templateUrl: './chat-bot.html',
  styleUrl: './chat-bot.css',
})
export class ChatBot implements OnInit {
  @ViewChild('chatHistory')
  private chatHistory!: ElementRef;

  private chatService = inject(ChatService);

  userInput = '';

  isLoading = false;

  local = false;

  messages = signal<{ text: string; isBot: boolean }[]>([]);

  ngOnInit(): void {
    this.carregarHistorico();
  }

  private carregarHistorico() {
    this.chatService.buscarHistorico().subscribe({
      next: (historicoBanco) => {
        const mensagensMapeadas: { text: string; isBot: boolean }[] = [];

        mensagensMapeadas.push({ text: 'Olá! Como posso te ajudar hoje?', isBot: true });

        historicoBanco.forEach((item: any) => {
          mensagensMapeadas.push({ text: item.pergunta, isBot: false });
          mensagensMapeadas.push({ text: item.resposta, isBot: true });
        });

        this.messages.set(mensagensMapeadas);

        setTimeout(() => this.scrollToBottom(), 100);
      },
      error: (err) => console.error('Erro ao carregar histórico:', err),
    });
  }

  sendMessage() {
    this.trimUserMessage();
    if (this.userInput !== '' && !this.isLoading) {
      const perguntaEnviada = this.userInput;

      this.updateMessages(perguntaEnviada);
      this.isLoading = true;
      this.userInput = '';

      if (this.local) {
        this.simulateResponse();
      } else {
        this.sendChatMessage(perguntaEnviada);
      }
    }
  }

  private sendChatMessage(pergunta: string) {
    this.chatService
      .sendChatMessage(pergunta)
      .pipe(
        catchError(() => {
          this.updateMessages('Desculpe, não consigo processar seu pedido no momento.', true);
          this.isLoading = false;
          return throwError(() => new Error('Erro ocorreu ao mandar a mensagem.'));
        }),
      )
      .subscribe((response: any) => {
        this.updateMessages(response.message, true);
        this.isLoading = false;
      });
  }

  private updateMessages(text: string, isBot = false) {
    this.messages.update((messages) => [...messages, { text, isBot }]);
    setTimeout(() => this.scrollToBottom(), 50);
  }

  private trimUserMessage() {
    this.userInput = this.userInput.trim();
  }

  private simulateResponse() {
    setTimeout(() => {
      const response = 'Esta é uma resposta simulada do bot.';
      this.updateMessages(response, true);
      this.isLoading = false;
    }, 2000);
  }

  private scrollToBottom() {
    try {
      this.chatHistory.nativeElement.scrollTop = this.chatHistory.nativeElement.scrollHeight;
    } catch (err) {}
  }
}
