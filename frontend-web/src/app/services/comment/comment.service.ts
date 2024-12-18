import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Comment } from '../../models/comment';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private endpoint = `${environment.commentServiceBaseURL}/api/comments`;

  constructor(private http: HttpClient) { }

  postComment(comment: Comment): Observable<Comment> {
    return this.http.post<Comment>(this.endpoint, comment);
  }
}
