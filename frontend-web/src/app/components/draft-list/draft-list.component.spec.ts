import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DraftListComponent } from './draft-list.component';

describe('DraftComponent', () => {
  let component: DraftListComponent;
  let fixture: ComponentFixture<DraftListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DraftListComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(DraftListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
