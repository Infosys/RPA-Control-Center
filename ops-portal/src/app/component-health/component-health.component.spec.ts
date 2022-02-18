import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ComponentHealthComponent } from './component-health.component';

describe('ComponentHealthComponent', () => {
  let component: ComponentHealthComponent;
  let fixture: ComponentFixture<ComponentHealthComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ComponentHealthComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ComponentHealthComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
