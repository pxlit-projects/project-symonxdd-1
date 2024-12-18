import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router'; // To access the route parameter
import { PostService } from '../../services/post/post.service'; // Your PostService to fetch post data
import { Post } from '../../models/post'; // Assuming you have a Post model

import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner'; // For mat-spinner
import { RouterModule } from '@angular/router'; // For routerLink navigation
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-post-details',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatDividerModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    RouterModule
  ],
  templateUrl: './post-details.component.html',
  styleUrls: ['./post-details.component.css'],
})
export class PostDetailsComponent implements OnInit {
  post: Post | null = null;
  isLoading = true;
  errorMessage: string | null = null;

  constructor(
    private postService: PostService,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    const postId = +this.route.snapshot.paramMap.get('id')!; // Get the post ID from the route

    this.postService.getPostById(postId).subscribe({
      next: (data) => {
        this.post = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = 'Error fetching post details!';
        this.isLoading = false;
      },
    });
  }
}
