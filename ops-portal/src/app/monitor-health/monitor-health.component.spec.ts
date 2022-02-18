import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MonitorHealthComponent } from './monitor-health.component';

describe('MonitorHealthComponent', () => {
  let component: MonitorHealthComponent;
  let fixture: ComponentFixture<MonitorHealthComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MonitorHealthComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MonitorHealthComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
