import { Component, OnInit } from '@angular/core';
import { environment } from '../../../environments/environment';
import { PostService } from '../../services/post/post.service';
import { PostListComponent } from "../../components/post-list/post-list.component";

@Component({
  standalone: true,
  selector: 'app-home',
  imports: [PostListComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

}
