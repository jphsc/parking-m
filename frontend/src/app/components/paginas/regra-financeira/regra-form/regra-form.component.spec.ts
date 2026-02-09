import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegraFormComponent } from './regra-form.component';

describe('RegraFormComponent', () => {
  let component: RegraFormComponent;
  let fixture: ComponentFixture<RegraFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RegraFormComponent]
    });
    fixture = TestBed.createComponent(RegraFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
