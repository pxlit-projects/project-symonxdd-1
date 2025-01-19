import { TestBed } from '@angular/core/testing';
import { RoleService } from './role.service';
import { UserRoles } from '../../enums/user-roles.enum';

describe('RoleService', () => {
  let service: RoleService;

  beforeEach(() => {
    // Clear localStorage before each test to prevent any conflicts
    localStorage.clear();

    TestBed.configureTestingModule({
      providers: [RoleService],
    });
    service = TestBed.inject(RoleService);
  });

  it('should have a default role of USER1 if no role is stored in localStorage', () => {
    const role = service.getRole();
    expect(role).toBe(UserRoles.USER1); // Default role should be 'USER1'
  });

  it('should set the role and store it in localStorage', () => {
    service.setRole(UserRoles.EDITOR);

    const role = service.getRole();
    expect(role).toBe(UserRoles.EDITOR); // The role should be updated in the service

    const storedRole = localStorage.getItem('role');
    expect(storedRole).toBe(UserRoles.EDITOR); // The role should be saved in localStorage
  });

  it('should return true for isEditor() if the role is EDITOR', () => {
    service.setRole(UserRoles.EDITOR);
    const isEditor = service.isEditor();
    expect(isEditor).toBe(true); // isEditor should return true for the 'EDITOR' role
  });

  it('should return false for isEditor() if the role is not EDITOR', () => {
    service.setRole(UserRoles.USER1);
    const isEditor = service.isEditor();
    expect(isEditor).toBe(false); // isEditor should return false for roles other than 'EDITOR'
  });

  it('should return the correct headers with the role', () => {
    service.setRole(UserRoles.EDITOR);
    const headers = service.getHeaders();
    expect(headers.get('Role')).toBe(UserRoles.EDITOR); // The 'Role' header should contain the current role
  });

  it('should return null for an invalid role stored in localStorage', () => {
    localStorage.setItem('role', 'INVALID_ROLE' as any);

    const role = service.getRole();
    expect(role).toBe(UserRoles.USER1); // Invalid role should fallback to default 'USER1'
  });
});
