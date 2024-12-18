import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RoleService } from '../role/role.service';
import { WishlistItem } from '../../models/wishlist-item';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class WishlistService {
  private endpoint = `${environment.wishlistEndpoint}/api/wishlist`;

  constructor(private http: HttpClient, private roleService: RoleService) { }

  getWishlist(): Observable<WishlistItem[]> {
    const headers = this.roleService.getHeaders();
    return this.http.get<WishlistItem[]>(`${this.endpoint}/items`, { headers });
  }

  addToWishlist(productId: number, quantity: number): Observable<string> {
    const headers = this.roleService.getHeaders();
    return this.http.post<string>(`${this.endpoint}/add/${productId}?quantity=${quantity}`, {}, { headers });
  }

  removeFromWishlist(productId: number): Observable<void> {
    const headers = this.roleService.getHeaders();
    return this.http.delete<void>(`${this.endpoint}/products/delete/${productId}`, { headers });
  }

  updateWishlistItemQuantity(productId: number, quantity: number): Observable<string> {
    const headers = this.roleService.getHeaders();
    return this.http.put<string>(`${this.endpoint}/${productId}?quantity=${quantity}`, {}, { headers });
  }
}
