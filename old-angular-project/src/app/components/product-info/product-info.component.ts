import { Component, Input } from '@angular/core';


@Component({
  selector: 'app-product-info',
  templateUrl: './product-info.component.html',
  styleUrl: './product-info.component.css'
})
export class ProductInfoComponent {
  @Input() categoryName!: string;
  @Input() labelNames: string[] = [];
}
