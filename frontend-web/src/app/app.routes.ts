import { Routes } from '@angular/router';

import { HomeComponent } from './views/home/home.component';
import { NotificationsComponent } from './views/notifications/notifications.component';
import { AdminGuard } from './guards/auth.guard';
import { PostDetailsComponent } from './components/post-details/post-details.component';
import { ReviewComponent } from './views/review/review.component';
import { CreatePostComponent } from './components/create-post/create-post.component';
import { DraftListComponent } from './components/draft-list/draft-list.component';
import { EditPostComponent } from './components/edit-post/edit-post.component';
import { EditCommentComponent } from './components/edit-comment/edit-comment.component';

export const routes: Routes = [
  { path: '', component: HomeComponent }, // Root path
  { path: 'post/create', component: CreatePostComponent, canActivate: [AdminGuard] },
  { path: 'post/:id', component: PostDetailsComponent },
  { path: 'post/edit/:id', component: EditPostComponent, canActivate: [AdminGuard] },
  { path: 'comment/edit/:id', component: EditCommentComponent },
  { path: 'review', component: ReviewComponent, canActivate: [AdminGuard] }, // Protected route
  { path: 'draft', component: DraftListComponent, canActivate: [AdminGuard] }, // Protected route
  { path: 'notifications', component: NotificationsComponent, canActivate: [AdminGuard] }, // Protected route
];
