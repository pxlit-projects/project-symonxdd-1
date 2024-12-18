import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { PostService } from '../../services/post/post.service';
import { Router } from '@angular/router';

import { MatCardModule } from '@angular/material/card';
import { MatGridListModule } from '@angular/material/grid-list'; // For the grid layout
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { MatToolbarModule } from '@angular/material/toolbar';

@Component({
  selector: 'app-post-list',
  imports: [
    CommonModule,
    MatCardModule,
    MatGridListModule,
    MatButtonModule,
    MatDividerModule,
    MatToolbarModule
  ],
  templateUrl: './post-list.component.html',
  styleUrl: './post-list.component.css',
  providers: [PostService]
})
export class PostListComponent implements OnInit {

  posts: any[] = [];

  constructor(private postService: PostService, private router: Router) { }

  navigateToDetails(postId: number): void {
    this.router.navigate(['/post', postId]);
  }

  ngOnInit(): void {
    this.postService.getPosts().subscribe((data) => {
      this.posts = data;
      console.log(this.posts);
    });
  }
}

