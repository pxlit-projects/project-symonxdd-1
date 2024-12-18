import { HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class RoleService {
  // Initialize roleSubject directly with a default value
  private roleSubject: BehaviorSubject<string> = new BehaviorSubject<string>(
    localStorage.getItem('role') || 'user' // Default to 'user' if no role is saved
  );

  role$ = this.roleSubject.asObservable();

  constructor() { }

  setRole(role: string) {
    this.roleSubject.next(role);
    localStorage.setItem('role', role); // Save the role to localStorage
  }

  getRole() {
    return this.roleSubject.getValue();
  }

  isEditor(): boolean {
    return this.getRole() === 'editor';
  }

  getHeaders() {
    const role = this.roleSubject.getValue();
    return new HttpHeaders().set('Role', role);
  }
}
