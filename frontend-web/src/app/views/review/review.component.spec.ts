import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { ReviewComponent } from './review.component';
import { PostService } from '../../services/post/post.service';
import { ReviewService } from '../../services/review/review.service';
import { of, throwError } from 'rxjs';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule, NgModel } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('ReviewComponent', () => {
  let component: ReviewComponent;
  let fixture: ComponentFixture<ReviewComponent>;
  let postServiceSpy: jasmine.SpyObj<PostService>;
  let reviewServiceSpy: jasmine.SpyObj<ReviewService>;

  beforeEach(async () => {
    // Spies for dependencies
    postServiceSpy = jasmine.createSpyObj('PostService', ['getUnpublishedPosts']);
    reviewServiceSpy = jasmine.createSpyObj('ReviewService', ['submitReview']);

    // Mock data
    const mockPosts = [
      { id: 1, title: 'Post 1', author: 'Author 1', content: 'Content 1', status: 'PENDING_REVIEW' },
      { id: 2, title: 'Post 2', author: 'Author 2', content: 'Content 2', status: 'REJECTED' },
    ];

    // Mock responses
    postServiceSpy.getUnpublishedPosts.and.returnValue(of(mockPosts));
    reviewServiceSpy.submitReview.and.returnValue(of());

    await TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        ReviewComponent,
        MatToolbarModule,
        MatCardModule,
        MatButtonModule,
        MatInputModule,
        MatFormFieldModule,
        MatIconModule,
        FormsModule,
        BrowserAnimationsModule,
      ],
      providers: [
        { provide: PostService, useValue: postServiceSpy },
        { provide: ReviewService, useValue: reviewServiceSpy },
      ],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges(); // Trigger ngOnInit and initial rendering
  });

  // --- Tests ---

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch unpublished posts on init', () => {
    expect(postServiceSpy.getUnpublishedPosts).toHaveBeenCalled();
    expect(component.unpublishedPosts.length).toBe(2);
  });

  it('should display error message if fetching unpublished posts fails', () => {
    // Simulate an error response
    postServiceSpy.getUnpublishedPosts.and.returnValue(throwError(() => new Error('Network error')));
    component.fetchUnpublishedPosts();
    fixture.detectChanges();

    const errorMsg = fixture.debugElement.query(By.css('.error-message'));
    expect(component.errorMessage).toBe('Failed to load unpublished posts.');
    expect(errorMsg).toBeTruthy();
    expect(errorMsg.nativeElement.textContent).toContain('Failed to load unpublished posts.');
  });

  it('should approve a post and remove it from the list', () => {
    const postId = 1;

    // Simulate the call to submitReview
    reviewServiceSpy.submitReview.and.callFake((payload: { postId: number; remarks: string; approved: boolean }) => {
      // Manually remove the post from the unpublishedPosts array to simulate successful removal
      component.unpublishedPosts = component.unpublishedPosts.filter(post => post.id !== payload.postId);
      return of(undefined); // Simulate a successful API response
    });

    component.approvePost(postId);
    fixture.detectChanges();

    expect(reviewServiceSpy.submitReview).toHaveBeenCalledWith({ postId, remarks: '', approved: true });
    expect(component.unpublishedPosts.length).toBe(1); // Expect 1 remaining post
    expect(component.comments[postId]).toBeUndefined(); // Comment should be cleared
  });

  it('should handle approval failure and show error', () => {
    reviewServiceSpy.submitReview.and.returnValue(throwError(() => new Error('Approval failed')));
    component.approvePost(1);
    fixture.detectChanges();

    expect(component.errorMessage).toBe('Failed to approve the post.');
  });

  it('should reject a post and remove it from the list with a valid comment', () => {
    const postId = 2;
    const comment = 'Inappropriate content';

    // Initialize comments
    component.comments[postId] = comment;

    // Ensure mock form is valid
    const form = { invalid: false };

    // Simulate the call to submitReview
    reviewServiceSpy.submitReview.and.callFake((payload: { postId: number; remarks: string; approved: boolean }) => {
      // Manually remove the post from the unpublishedPosts array
      component.unpublishedPosts = component.unpublishedPosts.filter(post => post.id !== payload.postId);
      return of(undefined); // Simulate successful API response with void
    });

    // Call the method
    component.rejectPost(postId, comment, form);
    fixture.detectChanges();

    // Assertions
    expect(reviewServiceSpy.submitReview).toHaveBeenCalledWith({
      postId,
      remarks: comment,
      approved: false,
    });
    expect(component.unpublishedPosts.length).toBe(1); // Post removed
    expect(component.unpublishedPosts.find(post => post.id === postId)).toBeUndefined(); // Ensure post is gone
    expect(component.comments[postId]).toBeUndefined(); // Comment cleared
  });


  it('should not reject a post without a comment', () => {
    const postId = 2;
    const form = { invalid: true }; // Simulate invalid form
    component.rejectPost(postId, '', form);
    fixture.detectChanges();

    expect(reviewServiceSpy.submitReview).not.toHaveBeenCalled();
    expect(component.errorMessage).toBe('You forgot to write a comment...');
  });

  it('should render the correct number of posts', () => {
    const postCards = fixture.debugElement.queryAll(By.css('.post-card'));
    expect(postCards.length).toBe(2); // Two posts in mock data
  });

  it('should display a "No posts to review" message when there are no posts', () => {
    component.unpublishedPosts = []; // Simulate no posts
    fixture.detectChanges();

    const noPostsMessage = fixture.debugElement.query(By.css('.no-posts'));
    expect(noPostsMessage).toBeTruthy();
    expect(noPostsMessage.nativeElement.textContent).toContain('No posts to review.');
  });

  it('should render the approval and rejection buttons based on post status', () => {
    const approveButtons = fixture.debugElement.queryAll(By.css('button[color="primary"]'));
    const rejectButtons = fixture.debugElement.queryAll(By.css('button[color="warn"]'));

    expect(approveButtons.length).toBe(2); // Two approve buttons (for both statuses)
    expect(rejectButtons.length).toBe(1); // One reject button (for PENDING_REVIEW)
  });

  it('should display a mat-error when comment input is invalid', async () => {
    const postId = 1;
    component.comments[postId] = ''; // Set an empty comment
    fixture.detectChanges(); // Trigger initial rendering

    // Query for the input field
    const commentInput = fixture.debugElement.query(By.css('input[matInput]'));

    // Ensure input is marked invalid
    const commentNgModel = commentInput.injector.get(NgModel); // Get the NgModel
    commentNgModel.control.markAsTouched(); // Mark as touched to trigger validation
    commentNgModel.control.setErrors({ required: true }); // Manually set validation error

    fixture.detectChanges(); // Trigger re-rendering

    await fixture.whenStable(); // Ensure everything is stable

    // Look for the mat-error element
    const matError = fixture.debugElement.query(By.css('mat-error'));
    expect(matError).toBeTruthy(); // Check if the error element exists
    expect(matError.nativeElement.textContent).toContain('You forgot to write a comment...');
  });

  it('should not call submitReview or modify posts if postId is undefined', () => {
    const postId = undefined; // Undefined postId
    const comment = 'Some comment';
    const form = { invalid: false }; // Mock valid form

    component.rejectPost(postId, comment, form);

    // Verify that submitReview was not called
    expect(reviewServiceSpy.submitReview).not.toHaveBeenCalled();

    // Ensure the unpublishedPosts array is unchanged
    expect(component.unpublishedPosts.length).toBe(2); // Assuming the initial length is 2
  });

});
