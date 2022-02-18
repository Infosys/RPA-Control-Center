import { TestBed, inject } from '@angular/core/testing';

import { ClusterDetailsService } from './cluster-details.service';

describe('ClusterDetailsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ClusterDetailsService]
    });
  });

  it('should be created', inject([ClusterDetailsService], (service: ClusterDetailsService) => {
    expect(service).toBeTruthy();
  }));
});
