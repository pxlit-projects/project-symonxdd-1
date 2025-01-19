import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ReviewService } from './review.service';
import { HttpErrorResponse } from '@angular/common/http';
import { of } from 'rxjs';

describe('ReviewService', () => {
  let service: ReviewService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule], // Import HttpClientTestingModule for mocking HTTP requests
      providers: [ReviewService],
    });
    service = TestBed.inject(ReviewService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    // Ensure that no outstanding HTTP requests are left after the test completes
    httpMock.verify();
  });

  it('should submit a review for approval', () => {
    const payload = { postId: 1, remarks: 'Great post!', approved: true };

    service.submitReview(payload).subscribe((response) => {
      expect(response).toBeNull(); // Expect no response body (void)
    });

    const req = httpMock.expectOne(`${service['approveEndpoint']}`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);

    req.flush(null); // Simulate a successful response (no content)
  });

  it('should submit a review for rejection', () => {
    const payload = { postId: 1, remarks: 'Needs improvement', approved: false };

    service.submitReview(payload).subscribe((response) => {
      expect(response).toBeNull(); // Expect no response body (void)
    });

    const req = httpMock.expectOne(`${service['rejectEndpoint']}`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);

    req.flush(null); // Simulate a successful response (no content)
  });

  it('should handle error on submitReview failure', () => {
    const payload = { postId: 1, remarks: 'Poor post', approved: true };

    service.submitReview(payload).subscribe(
      () => { },
      (error) => {
        expect(error).toBe('Failed to process the review. Please try again later.');
      }
    );

    const req = httpMock.expectOne(`${service['approveEndpoint']}`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);

    const errorMessage = 'Server error';
    req.flush(errorMessage, { status: 500, statusText: 'Server Error' }); // Simulate server error
  });

  it('should handle error on submitReview failure (reject endpoint)', () => {
    const payload = { postId: 1, remarks: 'Needs improvement', approved: false };

    service.submitReview(payload).subscribe(
      () => { },
      (error) => {
        expect(error).toBe('Failed to process the review. Please try again later.');
      }
    );

    const req = httpMock.expectOne(`${service['rejectEndpoint']}`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);

    const errorMessage = 'Server error';
    req.flush(errorMessage, { status: 500, statusText: 'Server Error' }); // Simulate server error
  });
});
