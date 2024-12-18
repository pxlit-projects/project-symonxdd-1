import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProductListComponent } from './components/product-list/product-list.component';
import { CartComponent } from './views/cart/cart.component';
import { AdminComponent } from './views/admin/admin.component';
import { WishlistComponent } from './views/wishlist/wishlist.component';

import { AdminGuard } from './auth/admin.guard';

const routes: Routes = [
  { path: '', component: ProductListComponent }, // Root path
  { path: 'cart', component: CartComponent },
  { path: 'admin', component: AdminComponent, canActivate: [AdminGuard] }, // Protected route
  { path: 'wishlist', component: WishlistComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule { }
