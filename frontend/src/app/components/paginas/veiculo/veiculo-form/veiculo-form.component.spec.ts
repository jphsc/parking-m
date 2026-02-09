import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VeiculoFormComponent } from './veiculo-form.component';

describe('VeiculoFormComponent', () => {
  let component: VeiculoFormComponent;
  let fixture: ComponentFixture<VeiculoFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VeiculoFormComponent]
    });
    fixture = TestBed.createComponent(VeiculoFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
