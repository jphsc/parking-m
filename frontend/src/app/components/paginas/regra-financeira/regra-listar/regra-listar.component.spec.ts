import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegraListarComponent } from './regra-listar.component';

describe('RegraListarComponent', () => {
  let component: RegraListarComponent;
  let fixture: ComponentFixture<RegraListarComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RegraListarComponent]
    });
    fixture = TestBed.createComponent(RegraListarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
