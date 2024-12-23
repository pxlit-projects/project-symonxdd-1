import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RoleService } from '../../services/role/role.service';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule } from '@angular/forms'; // For two-way data binding
import { UserRoles } from '../../enums/user-roles.enum';

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

  onRoleChange(newRole: UserRoles) {
    this.roleService.setRole(newRole);
    if (newRole === UserRoles.USER1 && this.router.url !== '/') {
      this.router.navigate(['/']);
    }
  }
}
