// import { ComponentFixture, TestBed } from '@angular/core/testing';
// import { ProductListComponent } from './product-list.component';
// import { ProductService } from '../../services/product/product.service';
// import { CartService } from '../../services/cart/cart.service';
// import { WishlistService } from '../../services/wishlist/wishlist.service';
// import { MatSnackBarModule } from '@angular/material/snack-bar';
// import { MatFormFieldModule } from '@angular/material/form-field';
// import { MatInputModule } from '@angular/material/input';
// import { MatSelectModule } from '@angular/material/select';
// import { MatSliderModule } from '@angular/material/slider';
// import { of } from 'rxjs';
// import { Product } from '../../models/product';
// import { MatListModule } from '@angular/material/list';
// import { MatButtonModule } from '@angular/material/button';
// import { MatCardModule } from '@angular/material/card';
// import { ProductInfoComponent } from '../product-info/product-info.component';

// describe('ProductListComponent', () => {
//   let component: ProductListComponent;
//   let fixture: ComponentFixture<ProductListComponent>;
//   let mockProductService: jasmine.SpyObj<ProductService>;
//   let mockCartService: jasmine.SpyObj<CartService>;
//   let mockWishlistService: jasmine.SpyObj<WishlistService>;

//   beforeEach(async () => {
//     mockProductService = jasmine.createSpyObj('ProductService', ['getProducts', 'getCategories', 'getLabels']);
//     mockCartService = jasmine.createSpyObj('CartService', ['addToCart']);
//     mockWishlistService = jasmine.createSpyObj('WishlistService', ['addToWishlist']);

//     await TestBed.configureTestingModule({
//       declarations: [ProductListComponent],
//       imports: [
//         MatInputModule,
//         MatButtonModule,
//         MatSnackBarModule,
//         MatSelectModule,
//         MatFormFieldModule,
//         MatCardModule,
//         MatListModule,
//         MatSliderModule,
//         ProductInfoComponent
//       ],
//       providers: [
//         { provide: ProductService, useValue: mockProductService },
//         { provide: CartService, useValue: mockCartService },
//         { provide: WishlistService, useValue: mockWishlistService }
//       ]
//     }).compileComponents();

//     fixture = TestBed.createComponent(ProductListComponent);
//     component = fixture.componentInstance;

//     // Set up mock data
//     // Corrected mock data to match the Product interface
//     const mockProducts: Product[] = [
//       {
//         id: 1,
//         name: 'Product A',
//         price: 100,
//         categoryName: 'Category 1',
//         labelNames: ['Label 1'],
//         description: 'Description A',
//         stock: 10,
//         imageUrl: 'image-a.jpg'
//       },
//       {
//         id: 2,
//         name: 'Product B',
//         price: 200,
//         categoryName: 'Category 2',
//         labelNames: ['Label 2'],
//         description: 'Description B',
//         stock: 15,
//         imageUrl: 'image-b.jpg'
//       }
//     ];
//     const mockCategories = ['Category 1', 'Category 2'];
//     const mockLabels = ['Label 1', 'Label 2'];

//     // Mock the observables returned by the service methods
//     mockProductService.getProducts.and.returnValue(of(mockProducts));
//     mockProductService.getCategories.and.returnValue(of(mockCategories));
//     mockProductService.getLabels.and.returnValue(of(mockLabels));

//     fixture.detectChanges(); // Initialize the component
//   });

//   it('should create the component', () => {
//     expect(component).toBeTruthy();
//   });

//   it('should load products, categories, and labels on initialization', () => {
//     expect(component.products.length).toBe(2);
//     expect(component.categories).toEqual(['Category 1', 'Category 2']);
//     expect(component.labels).toEqual(['Label 1', 'Label 2']);
//   });

//   it('should filter products based on category', () => {
//     component.onCategoryChange('Category 1');
//     expect(component.filteredProducts.length).toBe(1);
//     expect(component.filteredProducts[0].categoryName).toBe('Category 1');
//   });

//   it('should filter products based on label', () => {
//     component.onLabelChange('Label 2');
//     expect(component.filteredProducts.length).toBe(1);
//     expect(component.filteredProducts[0].labelNames).toContain('Label 2');
//   });

//   it('should filter products based on price', () => {
//     component.onPriceChange({ target: { value: '150' } } as any);
//     expect(component.filteredProducts.length).toBe(1);
//     expect(component.filteredProducts[0].price).toBeGreaterThanOrEqual(150);
//   });

//   it('should return the full image URL', () => {
//     const imageUrl = 'image.jpg';
//     const fullUrl = component.getFullImageUrl(imageUrl);
//     expect(fullUrl).toBe(`${component.endpoint}/${imageUrl}`);
//   });
// });
