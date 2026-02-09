import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VeiculoListarComponent } from './veiculo-listar.component';

describe('VeiculoListarComponent', () => {
  let component: VeiculoListarComponent;
  let fixture: ComponentFixture<VeiculoListarComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VeiculoListarComponent]
    });
    fixture = TestBed.createComponent(VeiculoListarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
