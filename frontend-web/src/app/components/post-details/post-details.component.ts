import { Component, OnInit, LOCALE_ID, Inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PostService } from '../../services/post/post.service';
import { CommentService } from '../../services/comment/comment.service'; // Import CommentService
import { Post } from '../../models/post';
import { RoleService } from '../../services/role/role.service';  // Import RoleService

import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';  // Import NgForm
import { Comment } from '../../models/comment';  // Import Comment
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

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
    RouterModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule
  ],
  providers: [{ provide: LOCALE_ID, useValue: 'nl-BE' }],
  templateUrl: './post-details.component.html',
  styleUrls: ['./post-details.component.css'],
})
export class PostDetailsComponent implements OnInit {
  post: Post | null = null;
  isLoading = true;
  errorMessage: string | null = null;
  newComment: Comment = { content: '', createdAt: '', author: '', postId: 0 };  // Initialize postId to 0 initially
  currentRole: string = '';  // Holds the current role (author)

  constructor(
    private postService: PostService,
    private commentService: CommentService, // Inject CommentService
    private roleService: RoleService,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    const postId = +this.route.snapshot.paramMap.get('id')!; // Get the post ID from the route

    this.postService.getPostById(postId).subscribe({
      next: (data) => {
        this.post = data;
        // Assign postId only if data.id is defined, else fallback to a default value (e.g., 0)
        this.newComment.postId = data.id ?? 0; // Use the nullish coalescing operator to fall back to 0 if undefined
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = 'Error fetching post details!';
        this.isLoading = false;
      },
    });

    // Subscribe to role$ and update currentRole accordingly
    this.roleService.role$.subscribe((role) => {
      this.currentRole = role;  // Update the role (author)
    });
  }

  onSubmit(commentForm: NgForm): void {
    if (commentForm.invalid) return;

    // Ensure postId is defined before creating the comment
    if (!this.post?.id) {
      this.errorMessage = 'Post ID is missing!'; // Display an error message if postId is undefined
      return;
    }

    const commentToPost: Comment = {
      postId: this.newComment.postId,  // Use postId from newComment
      content: this.newComment.content,
      author: this.currentRole,  // Use the role (user/editor/etc.) as the author
      createdAt: new Date().toISOString(), // Add the current date/time
    };

    this.commentService.postComment(commentToPost) // Use CommentService to post the comment
      .subscribe({
        next: () => {
          this.newComment.content = ''; // Clear the comment content after successful submission
          this.post?.comments?.push(commentToPost); // Add the new comment to the post's comment list
        },
        error: (err) => {
          this.errorMessage = 'Error posting comment!';
        }
      });
  }
}
