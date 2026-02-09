import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MovveiculoListarComponent } from './movveiculo-listar.component';

describe('MovveiculoListarComponent', () => {
  let component: MovveiculoListarComponent;
  let fixture: ComponentFixture<MovveiculoListarComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MovveiculoListarComponent]
    });
    fixture = TestBed.createComponent(MovveiculoListarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
