import { Component, OnInit, LOCALE_ID } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
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
import { CommentItemComponent } from "../comment-item/comment-item.component";
import { MatToolbarModule } from '@angular/material/toolbar';
import { UserRoles } from '../../enums/user-roles.enum';

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
    MatInputModule,
    CommentItemComponent,
    MatToolbarModule
  ],
  providers: [{ provide: LOCALE_ID, useValue: 'nl-BE' }],
  templateUrl: './post-details.component.html',
  styleUrls: ['./post-details.component.css'],
})
export class PostDetailsComponent implements OnInit {
  post!: Post;
  errorMessage: string | null = null;
  newComment: Comment = { content: '', createdAt: '', author: UserRoles.EDITOR, postId: 0 };  // Initialize postId to 0 initially
  currentRole!: UserRoles; // Holds the current role (author)
  // editingCommentId: number | null = null;
  editingCommentId: number | undefined = undefined;
  editableComment: Comment = { postId: undefined, id: undefined, content: '' };

  constructor(
    private postService: PostService,
    private commentService: CommentService, // Inject CommentService
    private roleService: RoleService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    const postId = +this.route.snapshot.paramMap.get('id')!; // Get the post ID from the route

    this.postService.getPostById(postId).subscribe({
      next: (data) => {
        this.post = data;

        // Sort comments by date (most recent first), handling undefined `createdAt`
        if (this.post.comments) {
          this.post.comments.sort((a, b) => {
            const dateA = a.createdAt ? new Date(a.createdAt).getTime() : 0;
            const dateB = b.createdAt ? new Date(b.createdAt).getTime() : 0;
            return dateB - dateA;
          });
        }

        // Assign postId only if data.id is defined, else fallback to a default value (e.g., 0)
        this.newComment.postId = data.id; // Use the nullish coalescing operator to fall back to 0 if undefined

        console.log(this.post);
      },
      error: (err) => {
        this.errorMessage = 'Error fetching post details!';
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

          // Sort comments after adding a new one, handling undefined `createdAt`
          this.post.comments?.sort((a, b) => {
            const dateA = a.createdAt ? new Date(a.createdAt).getTime() : 0;
            const dateB = b.createdAt ? new Date(b.createdAt).getTime() : 0;
            return dateB - dateA;
          });
        },
        error: (err) => {
          this.errorMessage = 'Error posting comment!';
        }
      });
  }

  navigateToPostEditor(postId: number): void {
    this.router.navigate(['/post/edit', postId]);
  }

  // Check if the current user's role matches the one required
  isCorrectRole(commentAuthor: UserRoles): boolean {
    return this.roleService.getRole() === commentAuthor;
  }

  onEditComment(comment: Comment): void {
    this.editingCommentId = comment.id;
    this.editableComment = { ...comment }; // Clone the comment for editing
  }

  onSaveComment(updatedComment: Comment): void {
    const commentIndex = this.post.comments!.findIndex((c) => c.id === updatedComment.id);
    if (commentIndex > -1) {
      this.post.comments![commentIndex] = updatedComment; // Update the local comments list

      // Sort comments after editing, handling undefined `createdAt`
      this.post.comments!.sort((a, b) => {
        const dateA = a.createdAt ? new Date(a.createdAt).getTime() : 0;
        const dateB = b.createdAt ? new Date(b.createdAt).getTime() : 0;
        return dateB - dateA;
      });
    }

    this.editingCommentId = undefined; // Exit edit mode
  }

  onCancelEdit(): void {
    this.editingCommentId = undefined; // Exit edit mode without saving
    this.editableComment = { id: undefined, content: '' };
  }

  onCommentDeleted(commentId: number): void {
    this.commentService.deleteComment(commentId).subscribe(() => {
      console.log('Comment deleted successfully!');
      this.post.comments = this.post.comments!.filter((c) => c.id !== commentId);
    });
  }

  onCommentEdited(updatedComment: Comment): void {
    const index = this.post.comments!.findIndex((c) => c.id === updatedComment.id);
    this.post.comments![index] = updatedComment;

    // Sort comments after editing, handling undefined `createdAt`
    this.post.comments!.sort((a, b) => {
      const dateA = a.createdAt ? new Date(a.createdAt).getTime() : 0;
      const dateB = b.createdAt ? new Date(b.createdAt).getTime() : 0;
      return dateB - dateA;
    });

    this.commentService.updateComment(updatedComment).subscribe(() => {
      console.log('Comment edited successfully!');
    });
  }

  isEditor(): boolean {
    return this.currentRole === UserRoles.EDITOR;
  }
}
