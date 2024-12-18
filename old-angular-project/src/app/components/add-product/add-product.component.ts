import { Component } from '@angular/core';
import { ProductService } from '../../services/product/product.service';
import { Product } from '../../models/product';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-add-product',
  templateUrl: './add-product.component.html',
  styleUrls: ['./add-product.component.css']
})
export class AddProductComponent {
  imageUrl: string | ArrayBuffer | null = null;
  labelsInput: string = ''; // Input for labels as a comma-separated string

  newProduct: Product = {
    name: '',
    description: '',
    price: 0,
    categoryName: '',
    labelNames: [],
    stock: 0,
    imageUrl: ''
  };

  constructor(private productService: ProductService, private snackBar: MatSnackBar) { }

  onSubmitAdd() {
    console.log('Before processing:', JSON.stringify(this.newProduct, null, 2));

    // Process the labels input
    this.newProduct.labelNames = this.labelsInput
      .split(',')
      .map(labelName => labelName.trim()) // Convert to string and trim extra spaces
      .filter(labelName => labelName.length > 0); // Filter out any empty labels

    // console.log('Processed labels:', this.newProduct.labels);

    console.log('After processing:', JSON.stringify(this.newProduct, null, 2));

    // Use the template-driven model object for submission
    this.productService.addProduct(this.newProduct).subscribe(
      response => {
        this.snackBar.open('Product added successfully!', 'Close', { duration: 3000 });
        this.resetNewProduct();
      },
      error => {
        console.error('Error adding product', error);
        this.snackBar.open('Failed to add product', 'Close', { duration: 3000 });
      }
    );
  }

  resetNewProduct() {
    this.newProduct = {
      name: '',
      description: '',
      price: 0,
      categoryName: '',
      labelNames: [],
      stock: 0,
      imageUrl: ''
    };
    this.labelsInput = ''; // Reset the labels input
    this.imageUrl = null;
  }
}
