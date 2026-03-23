import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { ChatResponse } from '../chat-response';

@Injectable({
  providedIn: 'root',
})

export class ChatService {

  private readonly API = 'http://localhost:8080/api/ia/chat-bot';

  private http = inject(HttpClient);

  sendChatMessage(message: string) {
    return this.http.post<ChatResponse>(this.API, { message })
  }

}
