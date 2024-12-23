import { HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { UserRoles } from '../../enums/user-roles.enum';

// export enum UserRoles {
//   EDITOR = 'Editor',
//   USER1 = 'Moka',
//   USER2 = 'Andrew Tate',
// }

@Injectable({
  providedIn: 'root',
})
export class RoleService {
  // Default role to USER1 ('Moka') if no role is stored
  private roleSubject: BehaviorSubject<UserRoles> = new BehaviorSubject<UserRoles>(
    this.getValidRole(localStorage.getItem('role')) || UserRoles.USER1
  );

  private getValidRole(role: string | null): UserRoles | null {
    // Check if the role exists in the UserRoles enum
    return role && Object.values(UserRoles).includes(role as UserRoles) ? (role as UserRoles) : null;
  }

  role$ = this.roleSubject.asObservable();

  constructor() { }

  setRole(role: UserRoles) {
    this.roleSubject.next(role);
    localStorage.setItem('role', role); // Save the role to localStorage
  }

  getRole(): UserRoles {
    return this.roleSubject.getValue();
  }

  isEditor(): boolean {
    return this.getRole() === UserRoles.EDITOR;
  }

  getHeaders() {
    const role = this.getRole();
    return new HttpHeaders().set('Role', role);
  }
}
