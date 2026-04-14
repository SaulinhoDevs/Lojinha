import { ChatService } from './../services/chat-service';
import { Component, ElementRef, inject, OnInit, signal, ViewChild } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { NgClass } from '@angular/common';
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

  messages = signal([{ text: 'Olá! Como posso ajudar você hoje?', isBot: true }]);

  sendMessage() {
    this.trimUserMessage();
    if (this.userInput != '') {
      this.updateMessages(this.userInput);
      this.isLoading = true;

      this.chatService
        .sendChatMessage(this.userInput)
        .pipe(
          catchError(() => {
            this.updateMessages('Desculpe, ocorreu um erro ao processar sua mensagem.', true);
            this.userInput = '';
            this.isLoading = false;
            return throwError(() => new Error('Erro ao enviar mensagem'));
          }),
        )
        .subscribe((response) => {
          this.updateMessages(response.message, true);
          this.userInput = '';
          this.isLoading = false;
        });
    }
  }

  private updateMessages(text: string, isBot = false) {
    this.messages.update((messages) => [...messages, { text: text, isBot: isBot }]);
    this.scrollToBottom();
  }

  private trimUserMessage() {
    this.userInput = this.userInput.trim();
  }

  private scrollToBottom() {
    try {
      this.chatHistory.nativeElement.scrollTop = this.chatHistory.nativeElement.scrollHeight;
    } catch (err) {}
  }
}
