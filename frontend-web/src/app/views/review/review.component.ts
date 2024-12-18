import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatToolbarModule } from '@angular/material/toolbar';
import { FormsModule } from '@angular/forms';
import { PostService } from '../../services/post/post.service';
import { ReviewService } from '../../services/review/review.service';
import { Post } from '../../models/post';
import { MatIconModule } from '@angular/material/icon';

@Component({
  standalone: true,
  selector: 'app-review',
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatInputModule,
    MatFormFieldModule,
    MatToolbarModule,
    FormsModule,
    MatIconModule
  ],
  templateUrl: './review.component.html',
  styleUrls: ['./review.component.css'],
})
export class ReviewComponent implements OnInit {
  unpublishedPosts: Post[] = [];
  comments: { [postId: number]: string } = {}; // Temporary storage for comments
  errorMessage: string = '';

  constructor(
    private postService: PostService,
    private reviewService: ReviewService
  ) { }

  ngOnInit(): void {
    this.fetchUnpublishedPosts();
  }

  fetchUnpublishedPosts(): void {
    this.postService.getUnpublishedPosts().subscribe({
      next: (posts) => {
        this.unpublishedPosts = posts;
      },
      error: (err) => {
        this.errorMessage = 'Failed to load unpublished posts.';
        console.error(err);
      },
    });
  }

  approvePost(postId: number): void {
    const payload = { postId, remarks: '', approved: true };
    this.reviewService.submitReview(payload).subscribe({
      next: () => {
        this.unpublishedPosts = this.unpublishedPosts.filter(
          (post) => post.id !== postId
        );
        delete this.comments[postId]; // Clean up temporary comment storage
      },
      error: (err) => {
        this.errorMessage = 'Failed to approve the post.';
        console.error(err);
      },
    });
  }

  rejectPost(postId: number | undefined, comment: string, form: any): void {
    if (!postId) return; // Safety check for postId
    if (!comment || form.invalid) {
      this.errorMessage = 'You forgot to write a comment...';
      return;
    }

    const payload = { postId, remarks: comment, approved: false };
    this.reviewService.submitReview(payload).subscribe({
      next: () => {
        this.unpublishedPosts = this.unpublishedPosts.filter(
          (post) => post.id !== postId
        );
        delete this.comments[postId]; // Clean up temporary comment storage
      },
      error: (err) => {
        this.errorMessage = 'Failed to reject the post.';
        console.error(err);
      },
    });
  }
}
