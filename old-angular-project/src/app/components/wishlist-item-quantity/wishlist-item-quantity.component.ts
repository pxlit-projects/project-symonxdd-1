import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-wishlist-item-quantity',
  templateUrl: './wishlist-item-quantity.component.html',
  styleUrl: './wishlist-item-quantity.component.css'
})
export class WishlistItemQuantityComponent {
  @Input() quantity: number = 1;
  @Output() quantityChange: EventEmitter<number> = new EventEmitter<number>();

  incrementQuantity(): void {
    this.quantity++;
    this.quantityChange.emit(this.quantity);
  }

  decrementQuantity(): void {
    if (this.quantity > 1) {
      this.quantity--;
      this.quantityChange.emit(this.quantity);
    }
  }
}
