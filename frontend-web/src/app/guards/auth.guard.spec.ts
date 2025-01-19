import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { AdminGuard } from './auth.guard';
import { RoleService } from '../services/role/role.service';
import { of } from 'rxjs';

// Mock RoleService and Router
class MockRoleService {
  isEditor() {
    return false;  // Default to false for testing else branch
  }
}

class MockRouter {
  navigate(url: string[]) { }
}

describe('AdminGuard', () => {
  let guard: AdminGuard;
  let roleService: RoleService;
  let router: Router;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        AdminGuard,
        { provide: RoleService, useClass: MockRoleService },
        { provide: Router, useClass: MockRouter }
      ]
    });

    guard = TestBed.inject(AdminGuard);
    roleService = TestBed.inject(RoleService);
    router = TestBed.inject(Router);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });

  it('should allow activation if user is editor', () => {
    // Set the role service to return true for isEditor
    spyOn(roleService, 'isEditor').and.returnValue(true);

    const canActivate = guard.canActivate();

    expect(canActivate).toBeTrue(); // Expect true as the guard should allow activation
  });

  it('should not allow activation and navigate to "/" if user is not editor', () => {
    spyOn(roleService, 'isEditor').and.returnValue(false);  // Return false for the test
    const navigateSpy = spyOn(router, 'navigate');  // Spy on the router's navigate method

    const canActivate = guard.canActivate();

    expect(canActivate).toBeFalse();  // Expect false as the guard should prevent activation
    expect(navigateSpy).toHaveBeenCalledWith(['/']);  // Expect the guard to navigate to '/'
  });
});
