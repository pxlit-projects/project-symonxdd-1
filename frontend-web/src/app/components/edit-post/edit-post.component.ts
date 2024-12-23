import { Component, Input, OnInit } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { PostService } from '../../services/post/post.service';
import { ActivatedRoute } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { Post } from '../../models/post';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-edit-post',
  imports: [CommonModule, MatToolbarModule, MatButtonModule, ReactiveFormsModule],
  templateUrl: './edit-post.component.html',
  styleUrl: './edit-post.component.css',
  standalone: true
})
export class EditPostComponent implements OnInit {

  post!: Post;
  postForm!: FormGroup;

  constructor(private postService: PostService, private route: ActivatedRoute, private fb: FormBuilder) { }

  ngOnInit(): void {
    const postId = +this.route.snapshot.paramMap.get('id')!; // Extract post ID from the route
    this.postService.getPostById(postId).subscribe(post => {
      this.post = post; // Bind fetched post to `post`
      this.initializeForm(); // Initialize form with fetched post data
    });
  }

  // Initialize reactive form
  initializeForm(): void {
    this.postForm = this.fb.group({
      title: [this.post?.title || '', [Validators.required, Validators.minLength(5)]],
      content: [this.post?.content || '', [Validators.required, Validators.minLength(10)]]
    });
  }

  // Save the edited post
  savePost(): void {
    if (this.postForm.valid) {
      const updatedPost = {
        ...this.post,
        ...this.postForm.value
      };
      this.postService.updatePost(updatedPost).subscribe(() => {
        // alert('Post update successfully!');
      });
    }
  }

  submitPostForReview(): void {
    const postId = +this.route.snapshot.paramMap.get('id')!; // Get the post ID from the route

    this.postService.submitPostForReview(postId).subscribe(() => {
      // display a Toast message...
    });
  }
}
