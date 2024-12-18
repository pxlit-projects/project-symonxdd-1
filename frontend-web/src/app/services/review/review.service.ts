import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ReviewService {
  private endpoint = `${environment.reviewServiceBaseURL}/api/reviews`;

  private approveEndpoint = `${this.endpoint}/approve`;
  private rejectEndpoint = `${this.endpoint}/reject`;

  constructor(private http: HttpClient) { }

  submitReview(payload: { postId: number; remarks: string; approved: boolean }): Observable<void> {
    const url = payload.approved ? this.approveEndpoint : this.rejectEndpoint;
    return this.http.post<void>(url, payload).pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    console.error('An error occurred:', error.message);
    return throwError('Failed to process the review. Please try again later.');
  }
}
