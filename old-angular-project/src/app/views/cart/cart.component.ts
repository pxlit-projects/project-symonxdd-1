import { Component, OnInit } from '@angular/core';
import { CartService } from '../../services/cart/cart.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  cartItems: any[] = []; // Replace with your cart item model

  constructor(private cartService: CartService, private snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.loadCartItems();
  }

  loadCartItems(): void {
    this.cartService.getCartItems().subscribe(items => {
      this.cartItems = items;
    });
  }

  // removeItem(itemId: number): void {
  //   this.cartService.removeFromCart({ id: itemId }).subscribe(() => {
  //     this.loadCartItems(); // Refresh the cart items after removing an item
  //   });
  // }

  removeItem(itemId: number): void {
    this.cartService.removeFromCart({ id: itemId }).subscribe(() => {
      this.loadCartItems(); // Refresh the cart items after removing an item
    });
  }

  placeOrder(): void {
    this.cartService.placeOrder().subscribe(() => {
      this.loadCartItems(); // Refresh the cart items after placing the order
      this.snackBar.open('Order placed successfully! Your cart has been cleared.', 'Close', {
        duration: 3000,
      });
    });
  }
}
