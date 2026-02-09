import { TestBed } from '@angular/core/testing';

import { RegraFinanceiraService } from './regra-financeira.service';

describe('RegraFinanceiraService', () => {
  let service: RegraFinanceiraService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RegraFinanceiraService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
