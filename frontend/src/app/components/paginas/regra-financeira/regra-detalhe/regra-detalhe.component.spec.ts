import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegraDetalheComponent } from './regra-detalhe.component';

describe('RegraDetalheComponent', () => {
  let component: RegraDetalheComponent;
  let fixture: ComponentFixture<RegraDetalheComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RegraDetalheComponent]
    });
    fixture = TestBed.createComponent(RegraDetalheComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
