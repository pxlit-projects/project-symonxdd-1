import { Component, OnInit } from '@angular/core';
import { RoleService } from '../../services/role/role.service';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnInit {
  currentRole: string = 'user';

  constructor(private roleService: RoleService) { }

  ngOnInit() {
    this.roleService.role$.subscribe(role => this.currentRole = role);
  }
}
