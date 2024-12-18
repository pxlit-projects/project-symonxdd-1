import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ProductService } from './product.service';
import { Product } from '../../models/product';
import { RoleService } from './../role/role.service';
import { environment } from '../../../environments/environment';
import { HttpHeaders } from '@angular/common/http';

describe('ProductService', () => {
  let service: ProductService;
  let httpMock: HttpTestingController;
  let roleService: jasmine.SpyObj<RoleService>;

  beforeEach(() => {
    const spy = jasmine.createSpyObj('RoleService', ['getHeaders']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        ProductService,
        { provide: RoleService, useValue: spy }
      ]
    });

    service = TestBed.inject(ProductService);
    httpMock = TestBed.inject(HttpTestingController);
    roleService = TestBed.inject(RoleService) as jasmine.SpyObj<RoleService>;

    // Correctly mock getHeaders to return HttpHeaders
    roleService.getHeaders.and.returnValue(new HttpHeaders());
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch products', () => {
    const mockProducts: Product[] = [
      {
        id: 1,
        name: 'Product A',
        description: 'A great product',
        price: 100,
        categoryName: 'Electronics',
        labelNames: ['New', 'Popular'],
        stock: 10,
        imageUrl: 'http://example.com/productA.jpg'
      },
      {
        id: 2,
        name: 'Product B',
        description: 'Another great product',
        price: 200,
        categoryName: 'Books',
        labelNames: ['Best Seller'],
        stock: 20,
        imageUrl: 'http://example.com/productB.jpg'
      }
    ];

    service.getProducts().subscribe(products => {
      expect(products.length).toBe(2);
      expect(products).toEqual(mockProducts);
    });

    const req = httpMock.expectOne(`${environment.productCatalogEndpoint}/api/products`);
    expect(req.request.method).toBe('GET');
    req.flush(mockProducts);
  });

  it('should add a product', () => {
    const newProduct: Product = {
      id: 3,
      name: 'Product C',
      description: 'A brand new product',
      price: 300,
      categoryName: 'Clothing',
      labelNames: ['New Arrival'],
      stock: 5,
      imageUrl: 'http://example.com/productC.jpg'
    };

    service.addProduct(newProduct).subscribe(product => {
      expect(product).toEqual(newProduct);
    });

    const req = httpMock.expectOne(`${environment.productCatalogEndpoint}/api/products`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(newProduct);
    req.flush(newProduct);
  });

  it('should fetch categories', () => {
    const mockCategories: string[] = ['Electronics', 'Books'];

    service.getCategories().subscribe(categories => {
      expect(categories).toEqual(mockCategories);
    });

    const req = httpMock.expectOne(`${environment.productCatalogEndpoint}/api/products/categories`);
    expect(req.request.method).toBe('GET');
    req.flush(mockCategories);
  });

  it('should fetch labels', () => {
    const mockLabels: string[] = ['New', 'Sale'];

    service.getLabels().subscribe(labels => {
      expect(labels).toEqual(mockLabels);
    });

    const req = httpMock.expectOne(`${environment.productCatalogEndpoint}/api/products/labels`);
    expect(req.request.method).toBe('GET');
    req.flush(mockLabels);
  });
});
