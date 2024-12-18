import { TestBed } from '@angular/core/testing';
import { RoleService } from './role.service';
import { HttpHeaders } from '@angular/common/http';

describe('RoleService', () => {
  let service: RoleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [RoleService],
    });

    service = TestBed.inject(RoleService);

    // Clear localStorage before each test to avoid interference
    localStorage.clear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });


  it('should allow setting and getting a role', () => {
    service.setRole('admin');
    expect(service.getRole()).toBe('admin');
  });

  it('should save the role to localStorage when setRole is called', () => {
    service.setRole('admin');
    expect(localStorage.getItem('role')).toBe('admin');
  });

  it('should correctly identify when the role is "admin"', () => {
    service.setRole('admin');
    expect(service.isAdmin()).toBeTrue();
  });

  it('should correctly identify when the role is not "admin"', () => {
    service.setRole('user');
    expect(service.isAdmin()).toBeFalse();
  });

  it('should return HttpHeaders with the current role in "Role" header', () => {
    service.setRole('admin');
    const headers: HttpHeaders = service.getHeaders();
    expect(headers.get('Role')).toBe('admin');
  });
});
