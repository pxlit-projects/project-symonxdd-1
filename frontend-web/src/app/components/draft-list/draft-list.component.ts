import { Component, OnInit } from '@angular/core';
import { PostService } from '../../services/post/post.service';
import { Post } from '../../models/post';
import { MatToolbarModule } from '@angular/material/toolbar';
import { Router } from '@angular/router';
import { RoleService } from '../../services/role/role.service';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-draft-list',
  imports: [
    MatToolbarModule,
    MatButtonModule,
    CommonModule
  ],
  templateUrl: './draft-list.component.html',
  styleUrl: './draft-list.component.css',
  standalone: true
})
export class DraftListComponent implements OnInit {

  posts: Post[] = [];
  currentRole: string = 'user';

  constructor(private postService: PostService, private router: Router, private roleService: RoleService) { }

  navigateToCreatePost(): void {
    this.router.navigate(['/post/create']);
  }

  navigateToPostEditor(postId: number): void {
    this.router.navigate(['/post/edit', postId]);
  }

  ngOnInit(): void {
    this.postService.getDrafts().subscribe((data) => {
      this.posts = data;
      console.log(this.posts);
    });

    this.roleService.role$.subscribe(role => this.currentRole = role);
  }
}
