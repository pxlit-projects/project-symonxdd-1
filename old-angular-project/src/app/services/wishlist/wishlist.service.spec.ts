import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { WishlistService } from './wishlist.service';
import { WishlistItem } from '../../models/wishlist-item';
import { RoleService } from '../role/role.service';
import { environment } from '../../../environments/environment';
import { HttpHeaders } from '@angular/common/http';

describe('WishlistService', () => {
  let service: WishlistService;
  let httpMock: HttpTestingController;
  let roleService: jasmine.SpyObj<RoleService>;

  beforeEach(() => {
    const spy = jasmine.createSpyObj('RoleService', ['getHeaders']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        WishlistService,
        { provide: RoleService, useValue: spy }
      ]
    });

    service = TestBed.inject(WishlistService);
    httpMock = TestBed.inject(HttpTestingController);
    roleService = TestBed.inject(RoleService) as jasmine.SpyObj<RoleService>;

    roleService.getHeaders.and.returnValue(new HttpHeaders());
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve wishlist items', () => {
    const mockWishlistItems: WishlistItem[] = [
      { id: 1, productId: 1, productName: 'Product A', quantity: 2, productPrice: 100 },
      { id: 2, productId: 2, productName: 'Product B', quantity: 1, productPrice: 200 }
    ];

    service.getWishlist().subscribe(items => {
      expect(items.length).toBe(2);
      expect(items).toEqual(mockWishlistItems);
    });

    const req = httpMock.expectOne(`${environment.wishlistEndpoint}/api/wishlist/items`);
    expect(req.request.method).toBe('GET');
    req.flush(mockWishlistItems);
  });

  it('should add an item to the wishlist', () => {
    const responseMessage = 'Item added to wishlist';

    service.addToWishlist(1, 2).subscribe(response => {
      expect(response).toBe(responseMessage);
    });

    const req = httpMock.expectOne(`${environment.wishlistEndpoint}/api/wishlist/add/1?quantity=2`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({});
    req.flush(responseMessage);
  });

  it('should update the quantity of an item in the wishlist', () => {
    const responseMessage = 'Wishlist item quantity updated';

    service.updateWishlistItemQuantity(1, 3).subscribe(response => {
      expect(response).toBe(responseMessage);
    });

    const req = httpMock.expectOne(`${environment.wishlistEndpoint}/api/wishlist/1?quantity=3`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual({});
    req.flush(responseMessage);
  });
});
