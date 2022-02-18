import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { KpiReportComponent } from './kpi-report.component';

describe('KpiReportComponent', () => {
  let component: KpiReportComponent;
  let fixture: ComponentFixture<KpiReportComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ KpiReportComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(KpiReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
