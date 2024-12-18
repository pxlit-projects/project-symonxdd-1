import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../services/product/product.service';
import { CartService } from '../../services/cart/cart.service';
import { MatSliderChange } from '@angular/material/slider';
import { WishlistService } from '../../services/wishlist/wishlist.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css'],
})
export class ProductListComponent implements OnInit {
  public endpoint = `${environment.productCatalogEndpoint}`;

  products: any[] = [];
  filteredProducts: any[] = [];
  categories: string[] = [];
  labels: string[] = [];
  minPrice: number = 0;
  maxPrice: number = 0;
  selectedPrice: number = 0;
  selectedCategory: string = '';
  selectedLabel: string = '';
  searchText: string = '';

  constructor(
    private productService: ProductService,
    private cartService: CartService,
    private wishlistService: WishlistService,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.productService.getProducts().subscribe((data) => {
      this.products = data.map(product => ({
        ...product,
        quantity: 1
      }));

      if (this.products.length > 0) {
        this.minPrice = Math.min(...this.products.map(p => p.price));
        this.maxPrice = Math.max(...this.products.map(p => p.price));
        this.selectedPrice = this.minPrice; // Initialize slider to the minimum price
      }

      this.filteredProducts = [...this.products];

      this.snackBar.open('Products have been loaded', 'Close', { duration: 3000 });
    });

    this.productService.getCategories().subscribe(categories => {
      this.categories = categories;
    });

    this.productService.getLabels().subscribe(labels => {
      this.labels = labels;
    });
  }

  addToCart(product: any): void {
    const quantity = product.quantity || 1;
    this.cartService.addToCart(product.id, quantity).subscribe(() => {
      console.log(`${product.name} added to cart with quantity ${quantity}`);
      this.snackBar.open(`${product.name} added to cart with quantity ${quantity}`, 'Close', { duration: 3000 });
    });
  }

  onSearchChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.searchText = input.value.toLowerCase();
    this.filterProducts();
  }

  onCategoryChange(category: string): void {
    this.selectedCategory = category;
    this.filterProducts();
  }

  onLabelChange(label: string): void {
    this.selectedLabel = label;
    this.filterProducts();
  }

  onPriceChange(event: Event): void {
    this.selectedPrice = Number((event.target as HTMLInputElement).value);
    this.filterProducts();
  }

  displayPrice(value: number | null): string {
    return value != null ? `${value} €` : '';
  }

  filterProducts(): void {
    this.filteredProducts = this.products.filter(product => {
      const matchesCategory = this.selectedCategory ? product.categoryName === this.selectedCategory : true;
      const matchesLabel = this.selectedLabel ? product.labelNames.includes(this.selectedLabel) : true;
      const matchesPrice = product.price >= this.selectedPrice; // Filter for price >= selectedPrice
      const matchesSearch = this.searchText
        ? product.name.toLowerCase().includes(this.searchText) ||
        product.description.toLowerCase().includes(this.searchText) ||
        product.categoryName.toLowerCase().includes(this.searchText)
        : true;

      return matchesCategory && matchesLabel && matchesPrice && matchesSearch;
    });
  }

  addToWishlist(productId: number): void {
    this.wishlistService.addToWishlist(productId, 1).subscribe(() => {
      console.log(`Product with ID ${productId} added to wishlist`);
      this.snackBar.open(`Product with ID ${productId} added to wishlist`, 'Close', { duration: 3000 });
    });
  }

  getFullImageUrl(imageUrl: string): string {
    return `${this.endpoint}/${imageUrl}`;
  }
}
