
import { inject, Injectable } from "@angular/core";
import { RxStompService, StompService } from '@stomp/ng2-stompjs';
import { PlayBackState } from "../model/model";
import { BehaviorSubject, connect } from "rxjs";

@Injectable()
export class WebSocketService {
    private playbackStateSubject = new BehaviorSubject<PlayBackState | null>(null);
    playbackState$ = this.playbackStateSubject.asObservable(); // Observable to emit updates
  
    constructor(private rxStompService: RxStompService) {}
  
    // Connect to WebSocket server using RxStompService
    connect(): void {
      this.rxStompService.configure({
        brokerURL: 'ws://tender-cat-production.up.railway.app/ws',  
        connectHeaders: {},
        debug: (str) => {
          console.log(str); 
        },
        reconnectDelay: 5000, 
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000, 
      });
  
      this.rxStompService.activate(); 
  
      // Subscribe to the topic for real-time updates
      this.rxStompService.watch('/topic/song').subscribe((message) => {
        const playstate: PlayBackState = JSON.parse(message.body);
        this.playbackStateSubject.next(playstate); 
      })
    }

    sendMessage(command: string, data: any): void {
      const message = {
        command: command,
        data: data
      };
      
      this.rxStompService.publish({
        destination: '/app/spotify', 
        body: JSON.stringify(message), 
      });
    
      console.log(`Sent message: ${JSON.stringify(message)}`);
    }
  
    // Disconnect from WebSocket server
    disconnect(): void {
      this.rxStompService.deactivate(); 
    }
  }