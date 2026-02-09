import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MovveiculoFormComponent } from './movveiculo-form.component';

describe('MovveiculoFormComponent', () => {
  let component: MovveiculoFormComponent;
  let fixture: ComponentFixture<MovveiculoFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MovveiculoFormComponent]
    });
    fixture = TestBed.createComponent(MovveiculoFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
