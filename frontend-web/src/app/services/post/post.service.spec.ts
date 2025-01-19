import { TestBed } from '@angular/core/testing';
import { PostService } from './post.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Post } from '../../models/post';
import { RoleService } from '../role/role.service';
import { HttpResponse } from '@angular/common/http';

describe('PostService', () => {
  let service: PostService;
  let httpMock: HttpTestingController;

  const mockPost: Post = { id: 1, title: 'Test Post', content: 'Test content', status: 'published', author: 'Test Author' };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [PostService, RoleService]
    });
    service = TestBed.inject(PostService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should retrieve posts', () => {
    service.getPosts().subscribe(posts => {
      expect(posts).toBeTruthy();
      expect(posts.length).toBeGreaterThan(0);
    });

    const req = httpMock.expectOne(`${service['endpoint']}`);
    expect(req.request.method).toBe('GET');
    req.flush([mockPost]);
  });

  it('should retrieve drafts', () => {
    service.getDrafts().subscribe(drafts => {
      expect(drafts).toBeTruthy();
      expect(drafts.length).toBeGreaterThan(0);
    });

    const req = httpMock.expectOne(`${service['endpoint']}/drafts`);
    expect(req.request.method).toBe('GET');
    req.flush([mockPost]);
  });

  it('should submit post for review', () => {
    service.submitPostForReview(mockPost.id!).subscribe(response => {
      expect(response).toBeTruthy();
    });

    const req = httpMock.expectOne(`${service['endpoint']}/submit-for-review`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toBe(mockPost.id);
    req.flush(mockPost);
  });

  it('should retrieve post by ID', () => {
    service.getPostById(mockPost.id!).subscribe(post => {
      expect(post).toEqual(mockPost);
    });

    const req = httpMock.expectOne(`${service['endpoint']}/${mockPost.id}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockPost);
  });

  it('should retrieve unpublished posts', () => {
    service.getUnpublishedPosts().subscribe(posts => {
      expect(posts).toBeTruthy();
      expect(posts.length).toBeGreaterThan(0);
    });

    const req = httpMock.expectOne(`${service['endpoint']}/unpublished`);
    expect(req.request.method).toBe('GET');
    req.flush([mockPost]);
  });

  it('should create a new post', () => {
    const newPost: Post = { id: 2, title: 'New Post', content: 'New content', status: 'draft', author: 'New Author' };

    service.createPost(newPost).subscribe(post => {
      expect(post).toEqual(newPost);
    });

    const req = httpMock.expectOne(`${service['endpoint']}`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(newPost);
    req.flush(newPost);
  });

  it('should update an existing post', () => {
    const updatedPost: Post = { ...mockPost, title: 'Updated Post Title' };

    service.updatePost(updatedPost).subscribe(response => {
      expect(response.status).toBe(200);
      expect(response.body).toEqual(updatedPost);
    });

    const req = httpMock.expectOne(`${service['endpoint']}/${updatedPost.id}`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(updatedPost);
    req.flush(updatedPost, { status: 200, statusText: 'OK' });
  });

  it('should handle error correctly', () => {
    service.getPosts().subscribe(
      () => fail('should have failed with the 500 error'),
      (error) => {
        expect(error).toBe('Something bad happened; please try again later.');
      }
    );

    const req = httpMock.expectOne(`${service['endpoint']}`);
    req.flush('Something went wrong', { status: 500, statusText: 'Server Error' });
  });
});
