import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RoleService } from '../../services/role/role.service';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule } from '@angular/forms'; // For two-way data binding
import { UserRoles } from '../../enums/user-roles.enum';

import { routes } from '../../app.routes';
import { AdminGuard } from '../../guards/auth.guard';

@Component({
  standalone: true,
  imports: [
    MatButtonModule,
    MatSelectModule,
    MatFormFieldModule,
    FormsModule
  ],
  selector: 'app-toggle-role',
  templateUrl: './toggle-role.component.html',
  styleUrls: ['./toggle-role.component.css'],
})
export class ToggleRoleComponent implements OnInit {
  routes = routes; // Reference to the routes array

  currentRole!: UserRoles; // Use the enum type
  userRoles = UserRoles; // Enum reference for the template
  roleKeys: (keyof typeof UserRoles)[]; // Explicitly typed as enum keys

  constructor(private roleService: RoleService, private router: Router) {
    this.currentRole = this.roleService.getRole(); // Initialize with the current role
    this.roleKeys = Object.keys(UserRoles) as (keyof typeof UserRoles)[]; // Explicit cast
  }

  ngOnInit() {
    this.roleService.role$.subscribe((role) => (this.currentRole = role));
  }

  // onRoleChange(newRole: UserRoles) {
  //   this.roleService.setRole(newRole);
  //   if (newRole === UserRoles.USER1 && this.router.url !== '/') {
  //     this.router.navigate(['/']);
  //   }
  // }

  onRoleChange(newRole: UserRoles) {
    // Set the new role
    this.roleService.setRole(newRole);

    // Check if the current route is an admin-protected route
    const currentUrl = this.router.url;

    const isAdminRoute = this.router.config.some(route => {
      // Check if the route has a dynamic path, e.g., '/post/:id'
      const routePath = route.path || '';

      // Check if the current route matches the admin route
      const isMatch = currentUrl.includes(routePath);

      // Check if the route has an AdminGuard
      const hasGuard = route.canActivate?.includes(AdminGuard);

      // Return true if both match and the route is protected by AdminGuard
      return isMatch && hasGuard;
    });


    // If switching to a non-admin role (USER1 or USER2) on an admin route, redirect to '/'
    if (isAdminRoute && (newRole === UserRoles.USER1 || newRole === UserRoles.USER2)) {
      this.router.navigate(['/']);
    }
  }

}
