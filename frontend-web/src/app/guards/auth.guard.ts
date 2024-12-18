import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { RoleService } from '../services/role/role.service';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {

  constructor(private roleService: RoleService, private router: Router) { }

  canActivate(): boolean {
    if (this.roleService.isEditor()) {
      return true;
    } else {
      this.router.navigate(['/']);
      return false;
    }
  }
}
