import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
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

  getPostById(id: number): Observable<Post> {
    return this.http.get<Post>(`${this.endpoint}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  getUnpublishedPosts(): Observable<Post[]> {
    return this.http.get<Post[]>(`${this.endpoint}/unpublished`).pipe(
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
