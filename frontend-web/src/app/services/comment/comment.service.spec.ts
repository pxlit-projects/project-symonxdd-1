import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { CommentService } from './comment.service';
import { RoleService } from '../role/role.service';
import { Comment } from '../../models/comment';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { of, throwError } from 'rxjs';

describe('CommentService', () => {
  let service: CommentService;
  let httpMock: HttpTestingController;
  let roleServiceMock: jasmine.SpyObj<RoleService>;

  const mockComment: Comment = { id: 1, content: 'Test comment' };
  const mockCommentUpdated: Comment = { id: 1, content: 'Updated comment' };

  beforeEach(() => {
    const spyRoleService = jasmine.createSpyObj('RoleService', ['getHeaders']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        CommentService,
        { provide: RoleService, useValue: spyRoleService }
      ]
    });

    service = TestBed.inject(CommentService);
    httpMock = TestBed.inject(HttpTestingController);
    roleServiceMock = TestBed.inject(RoleService) as jasmine.SpyObj<RoleService>;
  });

  afterEach(() => {
    httpMock.verify(); // Ensures that no request is left unanswered
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('postComment', () => {
    it('should post a comment successfully', () => {
      const mockResponse: Comment = { ...mockComment };

      service.postComment(mockComment).subscribe((response) => {
        expect(response).toEqual(mockResponse);
      });

      const req = httpMock.expectOne(service['endpoint']);
      expect(req.request.method).toBe('POST');
      req.flush(mockResponse);
    });

    it('should handle error when posting a comment', () => {
      const mockError = new HttpErrorResponse({
        status: 400,
        statusText: 'Bad Request',
      });

      service.postComment(mockComment).subscribe({
        next: () => { },
        error: (err) => {
          expect(err).toBe('Something bad happened; please try again later.');
        }
      });

      const req = httpMock.expectOne(service['endpoint']);
      req.flush('Error', mockError);
    });
  });

  describe('deleteComment', () => {
    it('should delete a comment successfully', () => {
      const commentId = 1;

      // Call the service method
      service.deleteComment(commentId).subscribe(response => {
        // Expect no response body
        expect(response).toBeNull();
      });

      // Mock the DELETE request and respond with no content (status 200)
      const httpResponse = new HttpResponse({ status: 200 });

      // Use expectOne to match the DELETE URL and ensure the method is DELETE
      const req = httpMock.expectOne(`${service['endpoint']}/delete/${commentId}`);

      // Ensure the request method is DELETE
      expect(req.request.method).toBe('DELETE');

      // Simulate the response (no content for successful delete)
      req.flush(null, httpResponse);
    });

    it('should handle error when deleting a comment', () => {
      const commentId = 1;
      const mockError = new HttpErrorResponse({
        status: 400,
        statusText: 'Bad Request',
        error: 'Error',
        url: `http://localhost:6969/comment/api/comments/delete/${commentId}`,
      });

      // Simulate HTTP error for deleteComment
      service.deleteComment(commentId).subscribe({
        next: () => {
          fail('expected an error, not a successful response');
        },
        error: (err) => {
          // The error should match the message from handleError
          expect(err).toBe('Something bad happened; please try again later.');
        }
      });

      // Simulate the HTTP request and return an error
      const req = httpMock.expectOne(`${service['endpoint']}/delete/${commentId}`);
      req.flush('Error', mockError);
    });
  });

  describe('updateComment', () => {
    it('should update a comment successfully', () => {
      // Arrange: Set up the mock response for the HTTP request
      const updatedComment: Comment = { id: 1, content: 'Updated comment' };
      const mockHttpResponse = { status: 200, body: updatedComment };

      // Act: Call the service method
      service.updateComment(updatedComment).subscribe((response) => {
        // Assert: Check that the response is as expected
        expect(response.status).toBe(200);
        expect(response.body).toEqual(updatedComment);
      });

      // Simulate the HTTP response
      const req = httpMock.expectOne(`${service['endpoint']}/edit/${updatedComment.id}`);
      expect(req.request.method).toBe('PUT');
      req.flush(mockHttpResponse);
    });

    it('should handle error when updating a comment', () => {
      const mockError = new HttpErrorResponse({
        status: 400,
        statusText: 'Bad Request',
      });

      service.updateComment(mockCommentUpdated).subscribe({
        next: () => { },
        error: (err) => {
          expect(err).toBe('Something bad happened; please try again later.');
        }
      });

      const req = httpMock.expectOne(`${service['endpoint']}/edit/${mockCommentUpdated.id}`);
      req.flush('Error', mockError);
    });
  });

  describe('getCommentById', () => {
    it('should get a comment by ID successfully', () => {
      const commentId = 1;
      const mockResponse: Comment = { ...mockComment };

      service.getCommentById(commentId).subscribe((response) => {
        expect(response).toEqual(mockResponse);
      });

      const req = httpMock.expectOne(`${service['endpoint']}/${commentId}`);
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });

    it('should handle error when fetching comment by ID', () => {
      const commentId = 1;
      const mockError = new HttpErrorResponse({
        status: 404,
        statusText: 'Not Found',
      });

      service.getCommentById(commentId).subscribe({
        next: () => { },
        error: (err) => {
          expect(err).toBe('Something bad happened; please try again later.');
        }
      });

      const req = httpMock.expectOne(`${service['endpoint']}/${commentId}`);
      req.flush('Error', mockError);
    });
  });

  describe('handleError', () => {
    it('should return the correct error message', () => {
      const error = new HttpErrorResponse({
        status: 500,
        statusText: 'Internal Server Error',
      });

      service['handleError'](error).subscribe({
        next: () => { },
        error: (err) => {
          expect(err).toBe('Something bad happened; please try again later.');
        }
      });
    });
  });
});
