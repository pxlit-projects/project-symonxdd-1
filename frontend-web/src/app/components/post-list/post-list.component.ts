import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { PostService } from '../../services/post/post.service';
import { Router } from '@angular/router';

import { MatCardModule } from '@angular/material/card';
import { MatGridListModule } from '@angular/material/grid-list'; // For the grid layout
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { MatToolbarModule } from '@angular/material/toolbar';
import { Post } from '../../models/post';
import { FormsModule } from '@angular/forms';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-post-list',
  imports: [
    CommonModule,
    MatCardModule,
    MatGridListModule,
    MatButtonModule,
    MatDividerModule,
    MatToolbarModule,
    FormsModule,
    MatDatepickerModule,
    MatFormFieldModule,
    MatInputModule
  ],
  templateUrl: './post-list.component.html',
  styleUrl: './post-list.component.css',
  providers: [PostService]
})
export class PostListComponent implements OnInit {

  posts: Post[] = [];
  filteredPosts: Post[] = [];
  authorFilter: string = '';
  contentFilter: string = '';
  dateFilter: Date | null = null; // Use Date object here
  currentDate: string = new Date().toISOString().split('T')[0]; // Format as YYYY-MM-DD for date picker

  constructor(private postService: PostService, private router: Router) { }

  navigateToDetails(postId: number): void {
    this.router.navigate(['/post', postId]);
  }

  navigateToCreatePost(): void {
    this.router.navigate(['/post/create']);
  }

  // Filter function
  filterPosts(): void {
    this.filteredPosts = this.posts.filter(post => {
      const postDate = new Date(post.createdAt!); // Convert string to Date object

      return (
        (this.authorFilter ? post.author.toLowerCase().includes(this.authorFilter.toLowerCase()) : true) &&
        (this.contentFilter ? post.content.toLowerCase().includes(this.contentFilter.toLowerCase()) : true) &&
        (this.dateFilter ? this.isSameDate(postDate, this.dateFilter) : true) // Compare dates only
      );
    });
  }

  // Compare two dates without considering time
  isSameDate(date1: Date, date2: Date): boolean {
    return date1.getFullYear() === date2.getFullYear() &&
      date1.getMonth() === date2.getMonth() &&
      date1.getDate() === date2.getDate();
  }

  ngOnInit(): void {
    this.postService.getPosts().subscribe((data) => {
      this.posts = data;
      this.filteredPosts = data; // Initialize filtered posts with all posts
      console.log(this.posts);
    });
  }
}

