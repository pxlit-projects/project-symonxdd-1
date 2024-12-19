import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PostService } from '../../services/post/post.service';
import { Post } from '../../models/post';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatToolbarModule } from '@angular/material/toolbar';
import { CommonModule } from '@angular/common';

import { MatInputModule } from '@angular/material/input'; // For matInput
import { MatButtonModule } from '@angular/material/button'; // For mat-raised-button
import { RoleService } from '../../services/role/role.service';


@Component({
  selector: 'app-create-post',
  imports: [
    MatFormFieldModule,
    FormsModule,
    ReactiveFormsModule,
    MatToolbarModule,
    CommonModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ],
  templateUrl: './create-post.component.html',
  styleUrl: './create-post.component.css',
  standalone: true
})
export class CreatePostComponent implements OnInit {
  // Define the form group with form controls
  postForm: FormGroup;
  currentRole: string = '';  // Holds the current role (author)

  constructor(private postService: PostService, private router: Router, private roleService: RoleService) {
    // Initialize the form with controls and validators
    this.postForm = new FormGroup({
      title: new FormControl('', [Validators.required, Validators.maxLength(100)]), // Title is required with max length
      content: new FormControl('', [Validators.required]), // Content is required
    });
  }

  ngOnInit(): void {
    // Subscribe to role$ and update currentRole accordingly
    this.roleService.role$.subscribe((role) => {
      this.currentRole = role;
    });
  }

  // Method to create a new post
  createPost(): void {
    if (this.postForm.valid) {
      const newPost: Post = this.postForm.value;
      newPost.author = this.currentRole; // Assign the current role as the author

      this.postService.createPost(newPost).subscribe(
        (createdPost) => {
          console.log('Post created successfully:', createdPost);
          this.router.navigate(['/']); // Navigate to post list after creation
        },
        (error) => {
          console.error('Error creating post:', error);
          // Optionally, show an error message
        }
      );
    } else {
      console.log('Form is invalid');
    }
  }
}
