import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Comment } from '../../models/comment';
import { RoleService } from '../role/role.service';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private endpoint = `${environment.commentServiceBaseURL}/api/comments`;

  constructor(private roleService: RoleService, private http: HttpClient) { }

  postComment(comment: Comment): Observable<Comment> {
    return this.http.post<Comment>(this.endpoint, comment).pipe(
      catchError(this.handleError) // Here we use the handleError method
    );
  }

  deleteComment(commentId: number): Observable<void> {
    const headers = this.roleService.getHeaders();
    return this.http.delete<void>(`${this.endpoint}/delete/${commentId}`, { headers })
      .pipe(catchError(this.handleError)); // Use catchError to forward the error to handleError
  }

  updateComment(updatedComment: Comment): Observable<HttpResponse<any>> {
    const headers = this.roleService.getHeaders();
    return this.http.put<HttpResponse<any>>(`${this.endpoint}/edit/${updatedComment.id}`, updatedComment, { headers })
      .pipe(catchError(this.handleError));
  }

  getCommentById(id: number): Observable<Comment> {
    return this.http.get<Comment>(`${this.endpoint}/${id}`).pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    console.error('An error occurred:', error.message);

    // Return an observable with a user-facing error message
    return throwError(
      'Something bad happened; please try again later.'
    );
  }
}
