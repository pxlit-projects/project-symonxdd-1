import { Component, OnInit } from '@angular/core';
import { WishlistService } from '../../services/wishlist/wishlist.service';
import { WishlistItem } from '../../models/wishlist-item';

@Component({
  selector: 'app-wishlist',
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.css'],
})
export class WishlistComponent implements OnInit {
  wishlist: WishlistItem[] = [];

  constructor(private wishlistService: WishlistService) { }

  ngOnInit(): void {
    this.loadWishlist();
  }

  loadWishlist(): void {
    this.wishlistService.getWishlist().subscribe((data) => {
      this.wishlist = data;
    });
  }

  removeFromWishlist(productId: number | undefined): void {
    if (productId !== undefined) {
      this.wishlistService.removeFromWishlist(productId).subscribe(() => {
        console.log(`Product with ID ${productId} removed from wishlist`);
        this.loadWishlist(); // Refresh the wishlist after removal
      });
    } else {
      console.error('Product ID is undefined');
    }
  }

  updateQuantity(productId: number | undefined, quantity: number): void {
    if (productId !== undefined) {
      this.wishlistService.updateWishlistItemQuantity(productId, quantity).subscribe(() => {
        console.log(`Product with ID ${productId} quantity updated to ${quantity}`);
        this.loadWishlist(); // Refresh the wishlist after quantity update
      });
    } else {
      console.error('Product ID is undefined');
    }
  }
}
