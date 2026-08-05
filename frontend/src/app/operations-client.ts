import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';

export type OperationStatus = 'CREATED' | 'PROCESSING' | 'COMPLETED' | 'FAILED';
export interface TimelineEntry { sequence: number; previousStatus: OperationStatus | null; newStatus: OperationStatus; occurredAt: string; }
export interface OperationSnapshot { id: string; name: string; status: OperationStatus; version: number; createdAt: string; lastChangedAt: string; timeline: TimelineEntry[]; }

@Injectable({ providedIn: 'root' })
export class OperationsClient {
  private readonly local = window.location.port === '4200';
  private readonly apiBase = this.local ? 'http://localhost:8080' : '';
  private readonly brokerUrl = this.local ? 'ws://localhost:8080/ws' : `${window.location.protocol === 'https:' ? 'wss' : 'ws'}://${window.location.host}/ws`;
  private stomp?: Client;
  constructor(private readonly http: HttpClient) {}
  register(name: string) { return this.http.post<OperationSnapshot>(`${this.apiBase}/api/operations`, { name }); }
  transition(id: string, status: OperationStatus) { return this.http.post<OperationSnapshot>(`${this.apiBase}/api/operations/${id}/transitions`, { status }); }
  snapshot(id: string) { return this.http.get<OperationSnapshot>(`${this.apiBase}/api/operations/${id}`); }
  connect(onUpdate: (snapshot: OperationSnapshot) => void, onConnection: (connected: boolean) => void): void {
    this.stomp = new Client({ brokerURL: this.brokerUrl, reconnectDelay: 3000, onConnect: () => { onConnection(true); this.stomp?.subscribe('/topic/operations', message => onUpdate(JSON.parse(message.body))); }, onWebSocketClose: () => onConnection(false) });
    this.stomp.activate();
  }
  disconnect(): void { void this.stomp?.deactivate(); }
}
