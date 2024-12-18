import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProductService } from '../../services/product/product.service';
import { Product } from '../../models/product';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-update-product',
  templateUrl: './update-product.component.html',
  styleUrls: ['./update-product.component.css']
})
export class UpdateProductComponent implements OnInit {
  updateForm: FormGroup;
  products: Product[] = [];
  selectedProduct: Product | null = null;
  imageUrl: string | ArrayBuffer | null = null;

  constructor(
    private productService: ProductService,
    private snackBar: MatSnackBar,
    private fb: FormBuilder) {
    this.updateForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      price: [0, [Validators.required, Validators.min(0)]],
      categoryName: [''], // Assuming category is a string representation of ID or name
      labelNames: [''], // Comma-separated string
      stock: [0, [Validators.required, Validators.min(0)]],
      imageUrl: ['']
    });
  }

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts() {
    this.productService.getProducts().subscribe(
      products => {
        this.products = products;
        if (this.products.length > 0) {
          this.onSelectProduct(this.products[0]); // Select the first product by default
        }
      },
      error => console.error('Error loading products', error)
    );
  }

  onSelectProduct(selectedProduct: Product) {
    console.log('Selected product:', selectedProduct);
    this.selectedProduct = selectedProduct;

    // Convert labelNames array to comma-separated string for the form
    const labelsString = selectedProduct.labelNames?.join(', ') || '';

    this.updateForm.patchValue({
      name: selectedProduct.name,
      description: selectedProduct.description,
      price: selectedProduct.price,
      categoryName: selectedProduct.categoryName,
      labelNames: labelsString,
      stock: selectedProduct.stock,
      imageUrl: selectedProduct.imageUrl || ''
    });

    this.imageUrl = selectedProduct.imageUrl;
  }

  onSubmitUpdate() {
    const productId = this.selectedProduct?.id;

    if (productId === undefined) {
      console.error('Product ID is undefined.');
      this.snackBar.open('Product ID is missing.', 'Close', { duration: 3000 });
      return;
    }

    if (this.updateForm.valid) {
      const updatedFormValue = this.updateForm.value;

      // Convert comma-separated string to labels array
      const labelsArray = updatedFormValue.labelNames.split(',').map((label: string) => label.trim());

      const updatedProduct: Product = {
        ...updatedFormValue,
        labelNames: labelsArray, // Convert labels back to array
      };

      console.log('updatedProduct:', JSON.stringify(updatedProduct, null, 2));

      this.productService.updateProduct(productId, updatedProduct).subscribe(
        response => {
          this.snackBar.open('Product updated successfully!', 'Close', { duration: 3000 });
          this.loadProducts();
          this.updateForm.reset();

          this.selectedProduct = null;
        },
        error => {
          console.error('Error updating product', error);
          this.snackBar.open('Failed to update product', 'Close', { duration: 3000 });
        }
      );
    }
  }
}
