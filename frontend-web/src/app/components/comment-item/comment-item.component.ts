import { Component, Input, LOCALE_ID } from '@angular/core';
import { Comment } from '../../models/comment';
import { MatCardModule } from '@angular/material/card';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-comment-item',
  imports: [
    CommonModule,
    MatCardModule
  ],
  templateUrl: './comment-item.component.html',
  styleUrl: './comment-item.component.css',
  providers: [{ provide: LOCALE_ID, useValue: 'nl-BE' }],
})
export class CommentItemComponent {

  @Input() comment!: Comment; // Input for comment data
}
