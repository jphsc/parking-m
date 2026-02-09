import { TestBed } from '@angular/core/testing';

import { MovimentoVeiculoService } from './movimento-veiculo.service';

describe('MovimentoVeiculoService', () => {
  let service: MovimentoVeiculoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MovimentoVeiculoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
