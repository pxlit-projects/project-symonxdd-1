import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Comment } from '../../models/comment';
import { CommentService } from '../../services/comment/comment.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-edit-comment',
  templateUrl: './edit-comment.component.html',
  styleUrls: ['./edit-comment.component.css'],
  standalone: true,
  imports: [CommonModule, MatFormFieldModule, FormsModule]
})
export class EditCommentComponent {
  @Input() commentId!: number;
  @Output() commentUpdated = new EventEmitter<Comment>();
  @Output() editCancelled = new EventEmitter<void>();

  editableComment: Comment = { id: undefined, content: '' };

  constructor(private commentService: CommentService) { }

  ngOnInit(): void {
    this.commentService.getCommentById(this.commentId).subscribe((comment) => {
      this.editableComment = { ...comment };
    });
  }

  saveComment(): void {
    this.commentService.updateComment(this.editableComment).subscribe(() => {
      this.commentUpdated.emit(this.editableComment); // Emit updated comment
    });
  }

  cancelEdit(): void {
    this.editCancelled.emit(); // Emit cancel event
  }
}
