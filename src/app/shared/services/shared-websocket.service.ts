import {Injectable} from '@angular/core';
import * as SockJS from 'sockjs-client';
import {Subject} from 'rxjs';

export enum ConnectionState {
  DISCONNECTED,
  CONNECTING,
  CONNECTED,
  DISCONNECTING
}

/**
 * Handles the websocket connectivity to the monitor server.
 */
@Injectable()
export class SharedWebSocketService {
  private state: ConnectionState = ConnectionState.DISCONNECTED;
  private socket: WebSocket = undefined;
  private address: string;
  private messageId: number = 0;
  public onMessageReceived: Subject<string> = undefined;
  public onConnectivityChanged: Subject<ConnectionState> = undefined;
  public onError: Subject<Event> = undefined;

  public constructor() {
    this.onMessageReceived = new Subject<string>();
    this.onConnectivityChanged = new Subject<ConnectionState>();
    this.onError = new Subject<Event>();
  }

  public generateMessageId(): number {
    return this.messageId++;
  }

  public connect(address: string): void {
    if (this.state !== ConnectionState.DISCONNECTED) {
      return;
    }

    // Begin the connection
    this.address = address;
    this.state = ConnectionState.CONNECTING;
    this.socket = new SockJS(address, null, {
      'transports': [
        'websocket'
      ]
    });
    this.onConnectivityChanged.next(this.state);

    // Setup the callbacks
    this.socket.onopen = () => {
      this.state = ConnectionState.CONNECTED;
      this.onConnectivityChanged.next(this.state);
    };

    this.socket.onerror = (event: Event) => {
      this.onError.next(event);
    };

    this.socket.onmessage = this.dispatchMessage.bind(this);

    this.socket.onclose = (e: CloseEvent) => {
      console.log('onclose...', e);
      if (!e.wasClean) {
        this.onError.next(e);
      }

      this.state = ConnectionState.DISCONNECTED;
      this.socket = undefined;
      this.onConnectivityChanged.next(this.state);
    };
  }

  public disconnect(): void {
    if (this.state !== ConnectionState.CONNECTING && this.state !== ConnectionState.CONNECTED) {
      return;
    }

    console.log('Disconnecting...');
    this.state = ConnectionState.DISCONNECTING;
    this.onConnectivityChanged.next(this.state);
    this.socket.close();
  }

  public sendMessage(message: string): void {
    if (this.state !== ConnectionState.CONNECTED) {
      return;
    }

    // Send the payload
    this.socket.send(message);
  }

  private dispatchMessage(message: MessageEvent): void {
    if (this.state !== ConnectionState.CONNECTED) {
      return;
    }

    this.onMessageReceived.next(message.data);
  }

  public getState(): ConnectionState {
    return this.state;
  }

  public getAddress(): string {
    return this.address;
  }
}
