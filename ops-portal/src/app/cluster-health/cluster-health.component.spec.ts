import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ClusterHealthComponent } from './cluster-health.component';

describe('ClusterHealthComponent', () => {
  let component: ClusterHealthComponent;
  let fixture: ComponentFixture<ClusterHealthComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ClusterHealthComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClusterHealthComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
