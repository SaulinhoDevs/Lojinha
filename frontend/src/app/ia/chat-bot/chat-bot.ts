import { Component, signal } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { NgClass } from '@angular/common';

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
  userInput = '';

  messages = signal([{ text: 'Olá! Como posso te ajudar hoje?', isBot: true }]);

  sendMessage() {
    this.trimUserMessage();
    if (this.userInput !== '') {
      this.updateMessages(this.userInput);
      this.userInput = '';
      this.simulateResponse();
    }
  }

  private updateMessages(text: string, isBot = false){
    this.messages.update((messages) => [...messages, { text: text, isBot: isBot }]);
  }

  private trimUserMessage() {
    this.userInput = this.userInput.trim();
  }

  private simulateResponse() {
    setTimeout(() => {
      const response = 'Esta é uma resposta simulada do bot.';
      this.updateMessages(response, true);
    }, 2000);
  }

}
