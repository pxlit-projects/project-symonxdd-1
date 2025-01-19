import { ActivatedRoute, Router } from '@angular/router';

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EditPostComponent } from './edit-post.component';
import { ReactiveFormsModule, FormBuilder } from '@angular/forms';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { PostService } from '../../services/post/post.service';
import { ActivatedRouteSnapshot } from '@angular/router';
import { of } from 'rxjs';
import { Post } from '../../models/post';

describe('EditPostComponent - Branch Coverage', () => {
  let component: EditPostComponent;
  let fixture: ComponentFixture<EditPostComponent>;
  let postServiceMock: jasmine.SpyObj<PostService>;
  // let activatedRouteMock: jasmine.SpyObj<ActivatedRoute>;
  let mockActivatedRoute: Partial<ActivatedRoute>;

  beforeEach(async () => {
    postServiceMock = jasmine.createSpyObj('PostService', ['getPostById', 'updatePost', 'submitPostForReview']);

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

    await TestBed.configureTestingModule({
      imports: [EditPostComponent, ReactiveFormsModule, MatToolbarModule, MatButtonModule, CommonModule],
      providers: [
        { provide: PostService, useValue: postServiceMock },
        { provide: ActivatedRoute, useValue: mockActivatedRoute }
      ]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EditPostComponent);
    component = fixture.componentInstance;

    // Simulating a response for getting a post
    const mockPost: Post = {
      id: 1,
      title: 'Test Post',
      content: 'This is a test post.',
      status: 'pending',
      createdAt: '2025-01-01',
      author: ''
    };
    postServiceMock.getPostById.and.returnValue(of(mockPost));

    fixture.detectChanges(); // Trigger the change detection
  });

  it('should check the if condition for post status in HTML template (approved)', () => {
    // Set the status to 'approved'
    component.post = { ...component.post, status: 'approved' };
    fixture.detectChanges();
    const statusElement: HTMLElement = fixture.nativeElement.querySelector('.status');
    expect(statusElement).toHaveClass('approved');
  });

  it('should check the if condition for post status in HTML template (rejected)', () => {
    // Set the status to 'rejected'
    component.post = { ...component.post, status: 'rejected' };
    fixture.detectChanges();
    const statusElement: HTMLElement = fixture.nativeElement.querySelector('.status');
    expect(statusElement).toHaveClass('rejected');
  });

  it('should check the if condition for post status in HTML template (pending)', () => {
    // Set the status to 'pending'
    component.post = { ...component.post, status: 'pending' };
    fixture.detectChanges();
    const statusElement: HTMLElement = fixture.nativeElement.querySelector('.status');
    expect(statusElement).toHaveClass('pending');
  });

  it('should call savePost when the form is valid and the update button is clicked', () => {
    // Set the form to be valid
    component.postForm.setValue({ title: 'Updated Title', content: 'Updated Content' });
    spyOn(component, 'savePost');
    const button: HTMLButtonElement = fixture.nativeElement.querySelector('button[type="submit"]');
    button.click();
    expect(component.savePost).toHaveBeenCalled();
  });

  it('should call savePost when the form is invalid', () => {
    // Set the form to be invalid
    component.postForm.setValue({ title: '', content: '' });
    spyOn(component, 'savePost');
    const button: HTMLButtonElement = fixture.nativeElement.querySelector('button[type="submit"]');
    button.click();
    expect(component.savePost).toHaveBeenCalled();
  });

  it('should disable the update button when the form is invalid', () => {
    // Set the form to be invalid
    component.postForm.setValue({ title: '', content: '' });
    fixture.detectChanges();
    const button: HTMLButtonElement = fixture.nativeElement.querySelector('button[type="submit"]');
    expect(button.disabled).toBeTrue();
  });

  it('should enable the update button when the form is valid', () => {
    // Set the form to be valid
    component.postForm.setValue({ title: 'Valid Title', content: 'Valid Content' });
    fixture.detectChanges();
    const button: HTMLButtonElement = fixture.nativeElement.querySelector('button[type="submit"]');
    expect(button.disabled).toBeFalse();
  });

  it('should display errors when the title field is invalid and touched', () => {
    component.postForm.controls['title'].setValue('');
    component.postForm.controls['title'].markAsTouched();
    fixture.detectChanges();
    const errorMessage: HTMLElement = fixture.nativeElement.querySelector('.error-message small');
    expect(errorMessage).toBeTruthy();
    expect(errorMessage.textContent).toContain('Title is required.');
  });

  it('should display errors when the content field is invalid and touched', () => {
    component.postForm.controls['content'].setValue('');
    component.postForm.controls['content'].markAsTouched();
    fixture.detectChanges();
    const errorMessage: HTMLElement = fixture.nativeElement.querySelector('.error-message small');
    expect(errorMessage).toBeTruthy();
    expect(errorMessage.textContent).toContain('Content is required.');
  });

  it('should call submitPostForReview when submit for review button is clicked', () => {
    spyOn(component, 'submitPostForReview');
    const submitButton: HTMLButtonElement = fixture.nativeElement.querySelector('.submit-for-review-btn');
    submitButton.click();
    expect(component.submitPostForReview).toHaveBeenCalled();
  });
});
