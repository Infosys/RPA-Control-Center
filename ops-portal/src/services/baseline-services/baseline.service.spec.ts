import { TestBed } from '@angular/core/testing';

import { BaselineService } from './baseline.service';

describe('BaselineService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: BaselineService = TestBed.get(BaselineService);
    expect(service).toBeTruthy();
  });
});
