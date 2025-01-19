import { ComponentFixture, TestBed } from '@angular/core/testing';
// import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { HttpResponse } from '@angular/common/http';
import { PostDetailsComponent } from './post-details.component';
import { PostService } from '../../services/post/post.service';
import { Comment } from '../../models/comment';
import { CommentService } from '../../services/comment/comment.service';
import { RoleService } from '../../services/role/role.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { BehaviorSubject, of, throwError } from 'rxjs';
import { Post } from '../../models/post';
import { UserRoles } from '../../enums/user-roles.enum';
import { NgForm } from '@angular/forms';

import { registerLocaleData } from '@angular/common';
import localeNl from '@angular/common/locales/nl';
import { LOCALE_ID } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

registerLocaleData(localeNl, 'nl-BE');

describe('PostDetailsComponent', () => {
  let component: PostDetailsComponent;
  let fixture: ComponentFixture<PostDetailsComponent>;
  let mockPostService: jasmine.SpyObj<PostService>;
  let mockCommentService: jasmine.SpyObj<CommentService>;
  let mockRoleService: jasmine.SpyObj<RoleService>;
  let mockRoleSubject: BehaviorSubject<UserRoles>;
  let mockRouter: jasmine.SpyObj<Router>;
  let mockDialog: jasmine.SpyObj<MatDialog>;
  let mockActivatedRoute: Partial<ActivatedRoute>;

  const mockPost: Post = {
    id: 1,
    title: 'Test Post',
    content: 'This is a test post.',
    author: UserRoles.EDITOR, // Added author field
    status: 'published',
    comments: [
      { id: 1, content: 'Test comment', author: UserRoles.EDITOR, createdAt: new Date().toISOString(), postId: 1 }
    ]
  };

  const mockComment: Comment = {
    id: 1,
    postId: 1,
    content: 'Mock Comment',
    author: UserRoles.EDITOR,
    createdAt: new Date().toISOString()
  };

  beforeEach(() => {
    // Create mock services with Jasmine
    mockPostService = jasmine.createSpyObj('PostService', ['getPostById']);
    mockCommentService = jasmine.createSpyObj<CommentService>('CommentService', [
      'postComment',
      'updateComment',
      'deleteComment'
    ]);

    // Setup mock return values for postComment, updateComment, deleteComment
    mockCommentService.postComment.and.returnValue(of(mockComment));
    mockCommentService.updateComment.and.returnValue(of(new HttpResponse({ status: 200, body: { success: true } })));
    mockCommentService.deleteComment.and.returnValue(of());

    mockRoleSubject = new BehaviorSubject<UserRoles>(UserRoles.USER1);

    mockRoleService = jasmine.createSpyObj<RoleService>('RoleService', [
      'setRole',
      'getRole',
      'isEditor',
      'getHeaders',
    ]);

    mockRoleService.role$ = mockRoleSubject.asObservable();
    mockRoleService.getRole.and.callFake(() => mockRoleSubject.getValue());
    mockRoleService.setRole.and.callFake((role: UserRoles) => mockRoleSubject.next(role));

    mockRouter = jasmine.createSpyObj('Router', ['navigate']);
    mockDialog = jasmine.createSpyObj('MatDialog', ['open']);

    const mockParamMap = {
      has: jasmine.createSpy('has').and.returnValue(true),
      get: jasmine.createSpy('get').and.returnValue('1'),
      getAll: jasmine.createSpy('getAll').and.returnValue(['1']),
      keys: []
    };

    mockActivatedRoute = {
      snapshot: {
        paramMap: mockParamMap,
        url: [],
        params: {},
        queryParams: {},
        fragment: null,
        data: {},
      } as any,
    };

    TestBed.configureTestingModule({
      imports: [BrowserAnimationsModule],
      providers: [
        { provide: LOCALE_ID, useValue: 'nl-BE' },
        { provide: PostService, useValue: mockPostService },
        { provide: CommentService, useValue: mockCommentService },
        { provide: RoleService, useValue: mockRoleService },
        { provide: Router, useValue: mockRouter },
        { provide: MatDialog, useValue: mockDialog },
        { provide: ActivatedRoute, useValue: mockActivatedRoute }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(PostDetailsComponent);
    component = fixture.componentInstance;
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should load post details on initialization', () => {
    mockPostService.getPostById.and.returnValue(of(mockPost));
    fixture.detectChanges();
    expect(mockPostService.getPostById).toHaveBeenCalledWith(1);
    expect(component.post).toEqual(mockPost);
  });

  it('should delete a comment', () => {
    // Mock the deleteComment method to return an empty Observable (void)
    mockCommentService.deleteComment.and.returnValue(of(undefined));

    // Setup component data
    component.post = mockPost;

    // Simulate comment deletion
    component.onCommentDeleted(1);

    // Assert: Ensure deleteComment was called with the correct comment id
    expect(mockCommentService.deleteComment).toHaveBeenCalledWith(1);

    // Assert: Ensure the comment list is empty
    expect(component.post.comments!.length).toBe(0);
  });

  it('should handle error when fetching post details', () => {
    const errorResponse = 'Error fetching post details!';
    mockPostService.getPostById.and.returnValue(throwError(errorResponse));  // Simulating error

    component.ngOnInit();  // Call ngOnInit to trigger the service call

    // Assert that errorMessage is set
    expect(component.errorMessage).toBe('Error fetching post details!');
  });

  it('should handle case when post has no comments', () => {
    let postWithoutComments: Post = { id: 1, title: 'Test Post', content: 'This is a test post.', author: UserRoles.EDITOR, status: 'published' };

    // { id: 1, comments: [] };
    mockPostService.getPostById.and.returnValue(of(postWithoutComments));  // Return post with no comments

    component.ngOnInit();  // Call ngOnInit

    // Assert that comments are handled correctly
    expect(component.post.comments).toBeUndefined();
  });

  it('should show error when postId is missing on comment submission', () => {
    component.newComment.content = 'Test comment';
    component.newComment.postId = 0;  // Invalid postId

    const commentForm = { invalid: false } as NgForm;
    component.onSubmit(commentForm);

    // Assert that an error message is shown
    expect(component.errorMessage).toBe('Post ID is missing!');
  });

  it('should not save comment if it does not exist in the post comments', () => {
    component.post = { id: 1, title: "", content: "", comments: [], author: "" };  // Post with no comments

    const updatedComment = { id: 2, content: 'Updated comment', postId: 1 };

    component.onSaveComment(updatedComment);  // Attempt to save a non-existing comment

    // Assert that comments are not modified
    expect(component.post.comments).toEqual([]);
  });

  it('should cancel comment edit and reset editableComment', () => {
    component.editingCommentId = 1;
    component.editableComment = { id: 1, content: 'Editable comment' };

    component.onCancelEdit();  // Cancel the edit

    // Assert that editingCommentId is reset and editableComment is cleared
    expect(component.editingCommentId).toBeUndefined();
    expect(component.editableComment).toEqual({ id: undefined, content: '' });
  });

  // New tests for onSubmit

  it('should return if commentForm is invalid', () => {
    const mockForm = { invalid: true } as NgForm; // Simulate invalid form
    // spyOn(mockCommentService, 'postComment'); // Ensure postComment is not called

    component.onSubmit(mockForm);

    expect(mockCommentService.postComment).not.toHaveBeenCalled(); // postComment should not be called
  });

  it('should show error if postId is missing', () => {
    const mockForm = { invalid: false } as NgForm; // Simulate valid form
    component.post = { id: undefined, comments: [], author: 'Editor', title: 'new title', content: 'some nice text' }; // Simulate missing postId
    // spyOn(mockCommentService, 'postComment'); // Ensure postComment is not called

    component.onSubmit(mockForm);

    expect(mockCommentService.postComment).not.toHaveBeenCalled(); // Ensure comment is not posted
    expect(component.errorMessage).toBe('Post ID is missing!'); // Error message should be set
  });

  it('should post comment when postId is available', () => {
    const mockForm = { invalid: false } as NgForm; // Simulate valid form
    component.post = { id: 1, comments: [], author: "Editor", title: 'new title', content: 'Mock Comment' }; // Simulate valid postId
    component.newComment.content = ''; // Add content to the comment
    component.newComment.author = UserRoles.EDITOR; // Add author to the comment

    // Call onSubmit which should trigger the postComment method
    component.onSubmit(mockForm);

    // Assert that postComment was called with the new comment
    // expect(mockCommentService.postComment).toHaveBeenCalledWith(component.newComment);

    // Assert that the comment has been added to the post
    expect(component.post.comments!.length).toBe(1);
  });


});
