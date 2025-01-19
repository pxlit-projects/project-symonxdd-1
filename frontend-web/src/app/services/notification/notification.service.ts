import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Notification } from '../../models/notification';
import { environment } from '../../../environments/environment';
import { RoleService } from '../role/role.service';

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  private endpoint = `${environment.postServiceBaseURL}/api/notifications`;

  constructor(private http: HttpClient, private roleService: RoleService) { }

  getNotifications(): Observable<Notification[]> {
    const headers = this.roleService.getHeaders();

    return this.http.get<Notification[]>(this.endpoint, { headers }).pipe(
      tap((data) => console.log('Fetched notifications from API:', data)) // Log fetched data
    );
  }
}
