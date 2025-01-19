import { Post } from '../../models/post';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { PostListComponent } from './post-list.component'; // Import your component
import { BehaviorSubject, of } from 'rxjs'; // For testing Observables
import { PostService } from '../../services/post/post.service'; // Import PostService
import { RoleService } from '../../services/role/role.service'; // Import RoleService
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MAT_DATE_LOCALE, provideNativeDateAdapter } from '@angular/material/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { UserRoles } from '../../enums/user-roles.enum';

describe('PostListComponent', () => {
  let component: PostListComponent;
  let fixture: ComponentFixture<PostListComponent>;

  let postServiceSpy: jasmine.SpyObj<PostService>;
  let roleServiceSpy: jasmine.SpyObj<RoleService>;

  let roleSubject: BehaviorSubject<string>;

  beforeEach(async () => {
    postServiceSpy = jasmine.createSpyObj('PostService', ['getPosts']);
    roleSubject = new BehaviorSubject<string>('user');

    // Create a mock RoleService
    roleServiceSpy = jasmine.createSpyObj('RoleService', ['getRole'], { role$: roleSubject.asObservable() });
    roleServiceSpy.getRole.and.returnValue(UserRoles.EDITOR); // Default mock return value

    postServiceSpy.getPosts.and.returnValue(of([
      { id: 1, title: 'Post 1', author: 'Author 1', content: 'Content 1', status: 'Published', createdAt: '2023-11-15T10:00:00Z' },
      { id: 2, title: 'Post 2', author: 'Author 2', content: 'Content 2', status: 'Draft', createdAt: '2023-11-16T11:00:00Z' },
    ]));

    await TestBed.configureTestingModule({
      imports: [
        PostListComponent,
        HttpClientTestingModule,
        BrowserAnimationsModule,
        // MatToolbarModule,
        // MatButtonModule
      ],
      providers: [
        provideNativeDateAdapter(),
        { provide: PostService, useValue: postServiceSpy }, // Use the mock service
        { provide: RoleService, useValue: roleServiceSpy },// Use the mock service
        { provide: MAT_DATE_LOCALE, useValue: 'en-GB' }, // Example locale
      ],
    }).compileComponents(); // compile template and css
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PostListComponent);
    component = fixture.componentInstance;
    fixture.autoDetectChanges();
  });

  it('should filter posts by author', () => {
    // Manually set the posts
    component.posts = [
      { id: 1, title: 'Post 1', author: 'John', content: 'Content 1', status: 'Published', createdAt: '2023-11-15T10:00:00Z' },
      { id: 2, title: 'Post 2', author: 'Author 2', content: 'Content 2', status: 'Draft', createdAt: '2023-11-16T11:00:00Z' },
    ];

    console.log('component.posts before filtering:', component.posts);

    component.authorFilter = 'John';
    component.filterPosts();

    expect(component.filteredPosts.length).toBe(1);
    expect(component.filteredPosts[0].author).toBe('John');
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should display "Create New Post" button only for role "editor"', () => {
    // Editor role
    roleSubject.next('Editor');
    fixture.detectChanges();
    let createButton = fixture.debugElement.query(By.css('.create-post-btn'));
    expect(createButton).not.toBeNull();

    // User role (should not have access to create post button)
    // roleSubject.next('User');
    component.currentRole = 'User';
    fixture.detectChanges();
    createButton = fixture.debugElement.query(By.css('.create-post-btn'));
    expect(createButton).toBeFalsy();
  });

  it('should handle empty posts', () => {
    postServiceSpy.getPosts.and.returnValue(of([])); // Return empty posts
    fixture.detectChanges();

    expect(component.filteredPosts.length).toBe(0);
  });

  it('should navigate to post details on click', () => {
    const navigateSpy = spyOn(component['router'], 'navigate');
    const postId = 1;

    component.navigateToDetails(postId);

    expect(navigateSpy).toHaveBeenCalledWith(['/post', postId]);
  });

  it('should navigate to create post on click', () => {
    const navigateSpy = spyOn(component['router'], 'navigate');

    component.navigateToCreatePost();

    expect(navigateSpy).toHaveBeenCalledWith(['/post/create']);
  });

  it('should return true for same date', () => {
    const date1 = new Date('2023-11-15T10:00:00Z');
    const date2 = new Date('2023-11-15T15:30:00Z');

    expect(component.isSameDate(date1, date2)).toBe(true);
  });

  it('should return false for different dates', () => {
    const date1 = new Date('2023-11-15');
    const date2 = new Date('2023-11-16');

    expect(component.isSameDate(date1, date2)).toBe(false);
  });

  it('should update currentRole when role changes', () => {
    roleSubject.next('Editor');
    fixture.detectChanges();

    expect(component.currentRole).toBe('Editor');

    roleSubject.next('User');
    fixture.detectChanges();

    expect(component.currentRole).toBe('User');
  });

  it('should render posts in filteredPosts', () => {
    // Set up mock filteredPosts
    component.filteredPosts = [
      { id: 1, title: 'Post 1', author: 'Author 1', content: 'Content 1', status: 'Published', createdAt: '2023-11-15T10:00:00Z' },
      { id: 2, title: 'Post 2', author: 'Author 2', content: 'Content 2', status: 'Draft', createdAt: '2023-11-16T11:00:00Z' },
    ];
    fixture.detectChanges();

    // Check that the correct number of posts are rendered
    const postCards = fixture.debugElement.queryAll(By.css('.post-card'));
    expect(postCards.length).toBe(2); // Two posts

    // Check content of the first post
    expect(postCards[0].nativeElement.textContent).toContain('Post 1');
    expect(postCards[0].nativeElement.textContent).toContain('Author 1');
  });

  it('should filter posts by content', () => {
    // Manually set the posts
    component.posts = [
      { id: 1, title: 'Post 1', author: 'John', content: 'Content 1', status: 'Published', createdAt: '2023-11-15T10:00:00Z' },
      { id: 2, title: 'Post 2', author: 'Author 2', content: 'Content 2', status: 'Draft', createdAt: '2023-11-16T11:00:00Z' },
    ];

    component.contentFilter = 'Content 2';
    component.filterPosts();
    expect(component.filteredPosts.length).toBe(1);
    expect(component.filteredPosts[0].content).toBe('Content 2');
  });

  it('should filter posts by date', () => {
    // Manually set the posts
    component.posts = [
      { id: 1, title: 'Post 1', author: 'John', content: 'Content 1', status: 'Published', createdAt: '2023-11-15T10:00:00Z' },
      { id: 2, title: 'Post 2', author: 'Author 2', content: 'Content 2', status: 'Draft', createdAt: '2025-01-01' },
    ];

    component.dateFilter = new Date('2025-01-01');
    component.filterPosts();
    expect(component.filteredPosts.length).toBe(1);
    expect(component.filteredPosts[0].createdAt).toBe('2025-01-01');
  });

  it('should filter posts by author and content', () => {
    // Manually set the posts
    component.posts = [
      { id: 1, title: 'Post 1', author: 'John', content: 'Content 1', status: 'Published', createdAt: '2023-11-15T10:00:00Z' },
      { id: 2, title: 'Post 2', author: 'Author 2', content: 'Content 2', status: 'Draft', createdAt: '2023-11-16T11:00:00Z' },
    ];

    component.authorFilter = 'John';
    component.contentFilter = 'Content 1';
    component.filterPosts();
    expect(component.filteredPosts.length).toBe(1);
    expect(component.filteredPosts[0].author).toBe('John');
    expect(component.filteredPosts[0].content).toBe('Content 1');
  });

  it('should filter posts by author and date', () => {
    // Manually set the posts
    component.posts = [
      { id: 1, title: 'Post 1', author: 'John', content: 'Content 1', status: 'Published', createdAt: '2023-11-15T10:00:00Z' },
      { id: 2, title: 'Post 2', author: 'Jane', content: 'Content 2', status: 'Draft', createdAt: '2025-01-02' },
    ];

    component.authorFilter = 'Jane';
    component.dateFilter = new Date('2025-01-02');

    component.filterPosts();
    expect(component.filteredPosts.length).toBe(1);
    expect(component.filteredPosts[0].author).toBe('Jane');
    expect(component.filteredPosts[0].createdAt).toBe('2025-01-02');
  });

  it('should filter posts by content and date', () => {
    // Manually set the posts
    component.posts = [
      { id: 1, title: 'Post 1', author: 'John', content: 'Content 1', status: 'Published', createdAt: '2023-11-15T10:00:00Z' },
      { id: 2, title: 'Post 2', author: 'Author 2', content: 'Content 2', status: 'Draft', createdAt: '2025-01-02' },
    ];

    component.contentFilter = 'Content 2';
    component.dateFilter = new Date('2025-01-02');
    component.filterPosts();
    expect(component.filteredPosts.length).toBe(1);
    expect(component.filteredPosts[0].content).toBe('Content 2');
    expect(component.filteredPosts[0].createdAt).toBe('2025-01-02');
  });

  it('should filter posts by author, content, and date', () => {
    // Manually set the posts
    component.posts = [
      { id: 1, title: 'Post 1', author: 'John', content: 'Content 1', status: 'Published', createdAt: '2023-11-15T10:00:00Z' },
      { id: 2, title: 'Post 2', author: 'Jane', content: 'Content 2', status: 'Draft', createdAt: '2025-01-02' },
    ];

    component.authorFilter = 'Jane';
    component.contentFilter = 'Content 2';
    component.dateFilter = new Date('2025-01-02');

    component.filterPosts();
    expect(component.filteredPosts.length).toBe(1);
    expect(component.filteredPosts[0].author).toBe('Jane');
    expect(component.filteredPosts[0].content).toBe('Content 2');
    expect(component.filteredPosts[0].createdAt).toBe('2025-01-02');
  });

  it('should return all posts if no filters are applied', () => {
    // Manually set the posts
    component.posts = [
      { id: 1, title: 'Post 1', author: 'John', content: 'Content 1', status: 'Published', createdAt: '2023-11-15T10:00:00Z' },
      { id: 2, title: 'Post 2', author: 'Author 2', content: 'Content 2', status: 'Draft', createdAt: '2023-11-16T11:00:00Z' },
    ];

    component.authorFilter = '';
    component.contentFilter = '';
    component.dateFilter = null;
    component.filterPosts();
    expect(component.filteredPosts.length).toBe(2);
  });

});
