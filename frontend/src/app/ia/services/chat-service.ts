import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ChatResponse } from './chat-response';

@Injectable({
  providedIn: 'root',
})
export class ChatService {
  static readonly API = 'http://localhost:8080';

  private http = inject(HttpClient);

  sendChatMessage(message: string) {
    return this.http.post<ChatResponse>(`${ChatService.API}/api/ia/chat-bot`, { message });
  }
}
