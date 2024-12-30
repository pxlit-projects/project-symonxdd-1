import { Component, Input, Output, EventEmitter } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component';
import { Comment } from '../../models/comment';
import { CommonModule } from '@angular/common';
import { MatFormField } from '@angular/material/form-field';
import { MatIcon } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-comment-item',
  templateUrl: './comment-item.component.html',
  styleUrls: ['./comment-item.component.css'],
  standalone: true,
  imports: [CommonModule, MatFormField, FormsModule, MatIcon, MatInputModule, MatButtonModule]
})
export class CommentItemComponent {
  @Input() comment!: Comment;
  @Input() isEditable = false;
  @Output() commentEdited = new EventEmitter<Comment>();
  @Output() commentDeleted = new EventEmitter<number>();

  isEditing = false;
  editableContent = '';

  constructor(private dialog: MatDialog) { }

  onEdit(): void {
    this.isEditing = true;
    this.editableContent = this.comment.content;
  }

  saveEdit(): void {
    const updatedComment = { ...this.comment, content: this.editableContent };
    this.commentEdited.emit(updatedComment);
    this.isEditing = false;
  }

  cancelEdit(): void {
    this.isEditing = false;
  }

  onDelete(): void {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      width: '300px',
      data: { message: 'Are you sure you want to delete this comment?' },
    });

    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (confirmed) {
        this.commentDeleted.emit(this.comment.id);
        console.log('Pressed Delete');
      }
    });
  }
}
