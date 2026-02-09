import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CardLateralComponent } from './card-lateral.component';

describe('CardLateralComponent', () => {
  let component: CardLateralComponent;
  let fixture: ComponentFixture<CardLateralComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CardLateralComponent]
    });
    fixture = TestBed.createComponent(CardLateralComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
