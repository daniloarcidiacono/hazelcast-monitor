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

  public connect(address: string, useSockJS: boolean): void {
    if (this.state !== ConnectionState.DISCONNECTED) {
      return;
    }

    // Begin the connection
    if (useSockJS) {
      this.address = address;
    } else {
      if (address.startsWith("https")) {
        this.address = address.replace("https", "wss");
      } else if (address.startsWith("http")) {
        this.address = address.replace("http", "ws");
      }
    }

    this.state = ConnectionState.CONNECTING;
    try {
      this.socket = useSockJS ? new SockJS(this.address) : new WebSocket(this.address);
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
    } catch (e) {
      // This can happen, for example, when trying to establish an insecure SockJS connection from an https page
      // Convert Error to CloseEvent
      this.onError.next(
        new CloseEvent("error", {
          wasClean: false,

          // https://developer.mozilla.org/en-US/docs/Web/API/CloseEvent
          code: 3000,
          reason: e.message
        })
      );

      // Reset the state
      this.state = ConnectionState.DISCONNECTED;
      this.socket = undefined;
      this.onConnectivityChanged.next(this.state);
    }
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
