import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MovveiculoDetalheComponent } from './movveiculo-detalhe.component';

describe('MovveiculoDetalheComponent', () => {
  let component: MovveiculoDetalheComponent;
  let fixture: ComponentFixture<MovveiculoDetalheComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MovveiculoDetalheComponent]
    });
    fixture = TestBed.createComponent(MovveiculoDetalheComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
