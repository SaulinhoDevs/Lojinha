import { Component, ElementRef, inject, signal, ViewChild } from '@angular/core';
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
export class ChatBot {
  @ViewChild('chatHistory')
  private chatHistory!: ElementRef;

  private chatService = inject(ChatService);

  userInput = '';

  isLoading = false;

  local = false;

  messages = signal([{ text: 'Olá! Como posso te ajudar hoje?', isBot: true }]);

  sendMessage() {
    this.trimUserMessage();
    if (this.userInput !== '' && !this.isLoading) {
      this.updateMessages(this.userInput);
      this.isLoading = true;
      if (this.local) {
        this.simulateResponse();
      } else {
        this.sendChatMessage();
      }
    }
  }

  private sendChatMessage() {
    this.chatService
      .sendChatMessage(this.userInput)
      .pipe(
        catchError(() => {
          this.updateMessages('Desculpe, não consigo processar seu pedido no momento.', true);
          this.isLoading = false;
          return throwError(() => new Error('Erro ocorreu ao mandar a mensagem.'));
        }),
      )
      .subscribe((response) => {
        this.updateMessages(response.message, true);
        this.userInput = '';
        this.isLoading = false;
      });
  }

  private updateMessages(text: string, isBot = false) {
    this.messages.update((messages) => [...messages, { text, isBot }]);
    this.scrollToBottom();
  }

  private trimUserMessage() {
    this.userInput = this.userInput.trim();
  }

  private simulateResponse() {
    setTimeout(() => {
      const response = 'Esta é uma resposta simulada do bot.';
      this.updateMessages(response, true);
      this.userInput = '';
      this.isLoading = false;
    }, 2000);
  }

  private scrollToBottom() {
    try {
      this.chatHistory.nativeElement.scrollTop = this.chatHistory.nativeElement.scrollHeight;
    } catch (err) {}
  }
}
