import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Notification } from '../../models/notification';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  private endpoint = `${environment.postServiceBaseURL}/api/notifications`;

  constructor(private http: HttpClient) { }

  // getNotifications(): Observable<Notification[]> {
  //   return this.http.get<Notification[]>(this.endpoint);
  // }

  getNotifications(): Observable<Notification[]> {
    return this.http.get<Notification[]>(this.endpoint).pipe(
      tap((data) => console.log('Fetched notifications from API:', data)) // Log fetched data
    );
  }
}
