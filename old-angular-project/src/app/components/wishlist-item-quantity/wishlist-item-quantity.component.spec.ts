import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WishlistItemQuantityComponent } from './wishlist-item-quantity.component';

describe('WishlistItemQuantityComponent', () => {
  let component: WishlistItemQuantityComponent;
  let fixture: ComponentFixture<WishlistItemQuantityComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WishlistItemQuantityComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WishlistItemQuantityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
