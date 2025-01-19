import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Post } from '../../models/post';
import { RoleService } from './../role/role.service';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private endpoint = `${environment.postServiceBaseURL}/api/posts`;

  constructor(private roleService: RoleService, private http: HttpClient) { }

  getPosts(): Observable<Post[]> {
    return this.http.get<Post[]>(this.endpoint).pipe(
      catchError(this.handleError)
    );
  }

  getDrafts(): Observable<Post[]> {
    const headers = this.roleService.getHeaders();

    return this.http.get<Post[]>(`${this.endpoint}/drafts`, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  submitPostForReview(postId: number): Observable<Post> {
    const headers = this.roleService.getHeaders();

    return this.http.post<Post>(`${this.endpoint}/submit-for-review`, postId, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  getPostById(id: number): Observable<Post> {
    return this.http.get<Post>(`${this.endpoint}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  getUnpublishedPosts(): Observable<Post[]> {
    const headers = this.roleService.getHeaders();
    return this.http.get<Post[]>(`${this.endpoint}/unpublished`, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  createPost(newPost: Post): Observable<Post> {
    const headers = this.roleService.getHeaders();

    return this.http.post<Post>(this.endpoint, newPost, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  updatePost(updatedPost: Post): Observable<HttpResponse<any>> {
    const headers = this.roleService.getHeaders();

    return this.http.put<HttpResponse<any>>(`${this.endpoint}/${updatedPost.id}`, updatedPost, { headers, observe: 'response' }).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    // Log the error to the console (or send it to a logging infrastructure)
    console.error('An error occurred:', error.message);

    // Return an observable with a user-facing error message
    return throwError(
      'Something bad happened; please try again later.'
    );
  }
}
