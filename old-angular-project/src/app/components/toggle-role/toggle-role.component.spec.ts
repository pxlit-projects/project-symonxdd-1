import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ToggleRoleComponent } from './toggle-role.component';

describe('ToggleRoleComponent', () => {
  let component: ToggleRoleComponent;
  let fixture: ComponentFixture<ToggleRoleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ToggleRoleComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ToggleRoleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
