import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule } from '@angular/common/http';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatListModule } from '@angular/material/list';
import { MatSliderModule } from '@angular/material/slider';

import { HeaderComponent } from './components/header/header.component';
import { ProductListComponent } from './components/product-list/product-list.component';
import { ToggleRoleComponent } from './components/toggle-role/toggle-role.component';
import { AdminComponent } from './views/admin/admin.component';
import { AddProductComponent } from './components/add-product/add-product.component';
import { UpdateProductComponent } from './components/update-product/update-product.component';
import { CartComponent } from './views/cart/cart.component';
import { WishlistComponent } from './views/wishlist/wishlist.component';
import { ProductInfoComponent } from './components/product-info/product-info.component';
import { WishlistItemQuantityComponent } from './components/wishlist-item-quantity/wishlist-item-quantity.component';

@NgModule({
  declarations: [
    AppComponent,
    ToggleRoleComponent,
    ProductListComponent,
    HeaderComponent,
    AdminComponent,
    AddProductComponent,
    UpdateProductComponent,
    CartComponent,
    WishlistComponent,
    ProductInfoComponent,
    WishlistItemQuantityComponent
  ],
  exports: [
    HeaderComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatInputModule,
    MatButtonModule,
    MatSnackBarModule,
    MatSelectModule,
    MatFormFieldModule,
    MatCardModule,
    MatListModule,
    MatSliderModule
  ],
  providers: [
    provideAnimationsAsync()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
