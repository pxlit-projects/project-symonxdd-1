import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
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

  deleteComment(commentId: number): Observable<void> {
    return this.http.delete<void>(`${this.endpoint}/delete/${commentId}`);
  }

  updateComment(updatedComment: Comment): Observable<HttpResponse<any>> {
    return this.http.put<HttpResponse<any>>(`${this.endpoint}/edit/${updatedComment.id}`, updatedComment, { observe: 'response' })
      .pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    console.error('An error occurred:', error.message);

    // Return an observable with a user-facing error message
    return throwError(
      'Something bad happened; please try again later.'
    );
  }

  getCommentById(id: number): Observable<Comment> {
    return this.http.get<Comment>(`${this.endpoint}/${id}`);
  }
}
