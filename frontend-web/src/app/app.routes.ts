import { Routes } from '@angular/router';

import { HomeComponent } from './views/home/home.component';
import { NotificationsComponent } from './views/notifications/notifications.component';
import { AdminDashboardComponent } from './views/admin-dashboard/admin-dashboard.component';
import { AdminGuard } from './guards/auth.guard';
import { PostDetailsComponent } from './components/post-details/post-details.component';
import { ReviewComponent } from './views/review/review.component';

export const routes: Routes = [
  { path: '', component: HomeComponent }, // Root path
  { path: 'post/:id', component: PostDetailsComponent },
  { path: 'review', component: ReviewComponent, canActivate: [AdminGuard] }, // Protected route
  { path: 'notifications', component: NotificationsComponent, canActivate: [AdminGuard] }, // Protected route
  { path: 'admin', component: AdminDashboardComponent, canActivate: [AdminGuard] }, // Protected route
];
