import { Component, OnInit } from '@angular/core';
import { RoleService } from '../../services/role/role.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {
  currentRole: string = 'user';

  constructor(private roleService: RoleService) { }

  ngOnInit() {
    this.roleService.role$.subscribe(role => this.currentRole = role);
  }
}
