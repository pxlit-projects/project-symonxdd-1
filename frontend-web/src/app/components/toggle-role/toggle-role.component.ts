import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RoleService } from '../../services/role/role.service';
import { MatButtonModule } from '@angular/material/button';

@Component({
  standalone: true,
  imports: [MatButtonModule],
  selector: 'app-toggle-role',
  templateUrl: './toggle-role.component.html',
  styleUrls: ['./toggle-role.component.css'],
})
export class ToggleRoleComponent implements OnInit {
  currentRole!: string;

  constructor(private roleService: RoleService, private router: Router) {
    this.currentRole = this.roleService.getRole(); // Initialize with the current role
  }

  ngOnInit() {
    this.roleService.role$.subscribe((role) => (this.currentRole = role));
  }

  toggleRole() {
    const newRole = this.currentRole === 'user' ? 'admin' : 'user';
    this.roleService.setRole(newRole);

    // Redirect to home if switching from admin to user while on /admin route
    if (newRole === 'user' && this.router.url === '/admin') {
      this.router.navigate(['/']);
    }
  }
}
