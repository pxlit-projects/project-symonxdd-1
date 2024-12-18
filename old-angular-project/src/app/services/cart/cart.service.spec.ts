import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpHeaders } from '@angular/common/http';
import { CartService } from './cart.service';
import { RoleService } from '../role/role.service';

describe('CartService', () => {
  let service: CartService;
  let httpMock: HttpTestingController;
  let roleServiceSpy: jasmine.SpyObj<RoleService>;

  beforeEach(() => {
    const spy = jasmine.createSpyObj('RoleService', ['getHeaders']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        CartService,
        { provide: RoleService, useValue: spy }
      ]
    });

    service = TestBed.inject(CartService);
    httpMock = TestBed.inject(HttpTestingController);
    roleServiceSpy = TestBed.inject(RoleService) as jasmine.SpyObj<RoleService>;
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get cart items', () => {
    const mockCartItems = [{ id: 1, name: 'Item 1', quantity: 2 }];
    // Mocking headers using HttpHeaders
    const mockHeaders = new HttpHeaders();
    roleServiceSpy.getHeaders.and.returnValue(mockHeaders);

    service.getCartItems().subscribe(items => {
      expect(items).toEqual(mockCartItems);
    });

    const req = httpMock.expectOne(`${service['endpoint']}/items`);
    expect(req.request.method).toBe('GET');
    expect(req.request.headers).toEqual(mockHeaders);
    req.flush(mockCartItems);
  });

  it('should update cart item quantity', () => {
    const mockHeaders = new HttpHeaders();
    roleServiceSpy.getHeaders.and.returnValue(mockHeaders);
    const cartItemId = 1;
    const quantity = 3;

    service.updateCartItemQuantity(cartItemId, quantity).subscribe();

    const req = httpMock.expectOne(`${service['endpoint']}/items/${cartItemId}?quantity=${quantity}`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.headers).toEqual(mockHeaders);
    req.flush({});
  });

  it('should remove item from cart', () => {
    const mockHeaders = new HttpHeaders();
    roleServiceSpy.getHeaders.and.returnValue(mockHeaders);
    const productId = { id: 1 };

    service.removeFromCart(productId).subscribe();

    const req = httpMock.expectOne(`${service['endpoint']}/products/delete/${productId.id}`);
    expect(req.request.method).toBe('DELETE');
    expect(req.request.headers).toEqual(mockHeaders);
    req.flush({});
  });

  it('should add item to cart', () => {
    const mockHeaders = new HttpHeaders();
    roleServiceSpy.getHeaders.and.returnValue(mockHeaders);
    const productId = 1;
    const quantity = 2;

    service.addToCart(productId, quantity).subscribe();

    const req = httpMock.expectOne(`${service['endpoint']}/products/add/${productId}?quantity=${quantity}`);
    expect(req.request.method).toBe('POST');
    expect(req.request.headers).toEqual(mockHeaders);
    req.flush({});
  });

  it('should place an order', () => {
    const mockHeaders = new HttpHeaders();
    roleServiceSpy.getHeaders.and.returnValue(mockHeaders);

    service.placeOrder().subscribe();

    const req = httpMock.expectOne(`${service['endpoint']}/products/delete/all`);
    expect(req.request.method).toBe('DELETE');
    expect(req.request.headers).toEqual(mockHeaders);
    req.flush({});
  });
});
