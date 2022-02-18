import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BaselineComparisonComponent } from './baseline-comparison.component';

describe('BaselineComparisonComponent', () => {
  let component: BaselineComparisonComponent;
  let fixture: ComponentFixture<BaselineComparisonComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BaselineComparisonComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BaselineComparisonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
