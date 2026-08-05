import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { OperationsClient } from './operations-client';

describe('OperationsClient', () => {
  it('loads the persisted snapshot through the operations API', () => {
    TestBed.configureTestingModule({
      providers: [OperationsClient, provideHttpClient(), provideHttpClientTesting()]
    });
    const client = TestBed.inject(OperationsClient);
    const http = TestBed.inject(HttpTestingController);
    const snapshot = {
      id: 'operation-42',
      name: 'Settlement import',
      status: 'PROCESSING' as const,
      version: 2,
      createdAt: '2026-08-05T20:00:00Z',
      lastChangedAt: '2026-08-05T20:01:00Z',
      timeline: []
    };

    let observedVersion = 0;
    client.snapshot(snapshot.id).subscribe(result => observedVersion = result.version);

    const request = http.expectOne('/api/operations/operation-42');
    expect(request.request.method).toBe('GET');
    request.flush(snapshot);
    expect(observedVersion).toBe(2);
    http.verify();
  });
});
