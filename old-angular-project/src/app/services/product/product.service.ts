import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Product } from '../../models/product';
import { RoleService } from './../role/role.service';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private endpoint = `${environment.productCatalogEndpoint}/api/products`;

  constructor(private roleService: RoleService, private http: HttpClient) { }

  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.endpoint).pipe(
      catchError(this.handleError)
    );
  }

  addProduct(product: Product): Observable<Product> {
    const headers = this.roleService.getHeaders();
    return this.http.post<Product>(this.endpoint, product, { headers }).pipe(
      catchError(this.handleError)
    );;
  }

  updateProduct(productId: number, product: Product): Observable<Product> {
    const headers = this.roleService.getHeaders();
    return this.http.put<Product>(`${this.endpoint}/${productId}`, product, { headers }).pipe(
      catchError(this.handleError)
    );;
  }

  getCategories(): Observable<string[]> {
    return this.http.get<string[]>(`${this.endpoint}/categories`);
  }

  getLabels(): Observable<string[]> {
    return this.http.get<string[]>(`${this.endpoint}/labels`).pipe(
      catchError(this.handleError)
    );;
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    // Log the error to the console (or send it to a logging infrastructure)
    console.error('An error occurred:', error.message);

    // Return an observable with a user-facing error message
    return throwError(
      'Something bad happened; please try again later.'
    );
  }
}
