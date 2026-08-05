import { DatePipe } from '@angular/common';
import { Component, OnDestroy, OnInit, signal } from '@angular/core';
import { OperationsClient, OperationSnapshot, OperationStatus } from './operations-client';

@Component({ selector: 'app-root', imports: [DatePipe], templateUrl: './app.html', styleUrl: './app.css' })
export class App implements OnInit, OnDestroy {
  readonly snapshot = signal<OperationSnapshot | null>(null);
  readonly connected = signal(false);
  readonly error = signal('');
  constructor(private readonly client: OperationsClient) {}
  ngOnInit(): void {
    this.client.connect(
      update => { this.connected.set(true); this.loadSnapshot(update.id); },
      connected => {
        this.connected.set(connected);
        const currentId = this.snapshot()?.id;
        if (connected && currentId) this.loadSnapshot(currentId);
      }
    );
  }
  register(name: string): void { this.client.register(name).subscribe({ next: snapshot => { this.snapshot.set(snapshot); this.error.set(''); }, error: error => this.error.set(error.error?.detail ?? 'Could not register operation') }); }
  transition(status: OperationStatus): void {
    const id = this.snapshot()?.id; if (!id) return;
    this.client.transition(id, status).subscribe({ next: snapshot => { this.snapshot.set(snapshot); this.error.set(''); }, error: error => this.error.set(error.error?.detail ?? 'Could not transition operation') });
  }
  private loadSnapshot(id: string): void { this.client.snapshot(id).subscribe(snapshot => this.snapshot.set(snapshot)); }
  ngOnDestroy(): void { this.client.disconnect(); }
}
