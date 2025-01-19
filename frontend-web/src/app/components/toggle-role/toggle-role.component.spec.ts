// import { ComponentFixture, TestBed } from '@angular/core/testing';
// import { ToggleRoleComponent } from './toggle-role.component';
// import { RoleService } from '../../services/role/role.service';
// import { RouterTestingModule } from '@angular/router/testing';
// import { Router } from '@angular/router';
// import { UserRoles } from '../../enums/user-roles.enum';
// import { MatSelectModule } from '@angular/material/select';
// import { MatFormFieldModule } from '@angular/material/form-field';
// import { MatButtonModule } from '@angular/material/button';
// import { of } from 'rxjs';
// import { AdminGuard } from '../../guards/auth.guard';
// import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
// import { By } from '@angular/platform-browser';

// describe('ToggleRoleComponent', () => {
//   let component: ToggleRoleComponent;
//   let fixture: ComponentFixture<ToggleRoleComponent>;
//   let roleServiceSpy: jasmine.SpyObj<RoleService>;
//   let routerSpy: jasmine.SpyObj<Router>;

//   beforeEach(async () => {
//     // Create spies for Router and RoleService
//     routerSpy = jasmine.createSpyObj('Router', ['navigate', 'config']);
//     roleServiceSpy = jasmine.createSpyObj('RoleService', ['getRole', 'setRole', 'role$']);

//     // Mock router.config to be an array with routes
//     routerSpy.config = [
//       { path: 'admin/dashboard', canActivate: [AdminGuard] },
//       { path: 'user/profile' },
//     ];

//     await TestBed.configureTestingModule({
//       imports: [
//         ToggleRoleComponent,
//         BrowserAnimationsModule,
//         MatButtonModule,
//         MatSelectModule,
//         MatFormFieldModule,
//         RouterTestingModule
//       ],
//       providers: [
//         { provide: RoleService, useValue: roleServiceSpy },
//         { provide: Router, useValue: routerSpy }
//       ]
//     }).compileComponents();
//   });

//   beforeEach(() => {
//     roleServiceSpy.getRole.and.returnValue(UserRoles.EDITOR);
//     roleServiceSpy.role$ = of(UserRoles.EDITOR); // Mocking the observable directly

//     fixture = TestBed.createComponent(ToggleRoleComponent);
//     component = fixture.componentInstance;
//     fixture.detectChanges();
//   });

//   afterEach(() => {
//     // Reset the spy manually to ensure it's cleared before the next test
//     routerSpy.navigate.calls.reset();
//   });

//   it('should initialize with the current role', () => {
//     expect(component.currentRole).toBe(UserRoles.EDITOR);
//   });

//   it('should redirect to / when switching to USER1 or USER2 on an admin route', () => {
//     // Spy on navigate in this test specifically
//     const navigateSpy = spyOn(routerSpy, 'navigate');

//     // Simulate switching to USER1 (non-admin role) on an admin route
//     component.onRoleChange(UserRoles.USER1);

//     // Expect the router to navigate to '/'
//     expect(navigateSpy).toHaveBeenCalledWith(['/']);
//   });

//   it('should not redirect if staying on an admin route while switching to admin role', () => {
//     // Simulate an admin route
//     routerSpy.config = [{ path: 'admin/dashboard', canActivate: [AdminGuard] }];

//     // Spy on navigate in this test specifically
//     const navigateSpy = spyOn(routerSpy, 'navigate');

//     // Simulate switching to an admin role while on an admin route
//     component.onRoleChange(UserRoles.EDITOR);

//     // Expect the router to not navigate
//     expect(navigateSpy).not.toHaveBeenCalled();
//   });

//   it('should change role when a new role is selected', () => {
//     // Spy on roleService.setRole method
//     const setRoleSpy = spyOn(roleServiceSpy, 'setRole');

//     // Simulate switching role
//     component.onRoleChange(UserRoles.USER2);

//     // Check if setRole was called with the correct argument
//     expect(setRoleSpy).toHaveBeenCalledWith(UserRoles.USER2);
//   });

//   it('should not redirect when switching to an admin role from a non-admin route', () => {
//     // Simulate a non-admin route
//     routerSpy.config = [{ path: 'user/profile' }];

//     // Spy on navigate in this test specifically
//     const navigateSpy = spyOn(routerSpy, 'navigate');

//     // Simulate switching to ADMIN (admin role)
//     component.onRoleChange(UserRoles.EDITOR);

//     // Expect the router to not navigate to '/'
//     expect(navigateSpy).not.toHaveBeenCalled();
//   });

//   it('should display the role options in the dropdown', () => {
//     // Initial check that role options are rendered
//     fixture.detectChanges();

//     // Find the mat-select dropdown
//     const select = fixture.debugElement.query(By.css('mat-select'));

//     // Find all mat-option elements
//     const options = fixture.debugElement.queryAll(By.css('mat-option'));

//     // Ensure that there are 3 options (since there are 3 roles in UserRoles)
//     expect(options.length).toBe(3);
//   });
// });
