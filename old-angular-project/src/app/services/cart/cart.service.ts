import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RoleService } from '../role/role.service';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private endpoint = `${environment.shoppingCartEndpoint}/api/carts`;

  constructor(private http: HttpClient, private roleService: RoleService) { }

  getCartItems(): Observable<any[]> {
    const headers = this.roleService.getHeaders();
    return this.http.get<any[]>(`${this.endpoint}/items`, { headers });
  }

  updateCartItemQuantity(cartItemId: number, quantity: number): Observable<void> {
    const headers = this.roleService.getHeaders();
    return this.http.put<void>(`${this.endpoint}/items/${cartItemId}?quantity=${quantity}`, null, { headers });
  }

  removeFromCart(productId: any): Observable<void> {
    console.log('item to be del', productId.id);

    const headers = this.roleService.getHeaders();
    return this.http.delete<void>(`${this.endpoint}/products/delete/${productId.id}`, { headers });
  }

  addToCart(productId: number, quantity: number): Observable<void> {
    const headers = this.roleService.getHeaders();
    const url = `${this.endpoint}/products/add/${productId}?quantity=${quantity}`;
    return this.http.post<void>(url, null, { headers });
  }

  placeOrder(): Observable<void> {
    const headers = this.roleService.getHeaders();
    return this.http.delete<void>(`${this.endpoint}/products/delete/all`, { headers });
  }
}
