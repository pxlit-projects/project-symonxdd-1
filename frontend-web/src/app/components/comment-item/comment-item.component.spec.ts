import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CommentItemComponent } from './comment-item.component';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component';
import { Comment } from '../../models/comment';
import { MatButtonModule } from '@angular/material/button';
import { of } from 'rxjs';
import { By } from '@angular/platform-browser';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('CommentItemComponent', () => {
  let component: CommentItemComponent;
  let fixture: ComponentFixture<CommentItemComponent>;
  let dialogSpy: jasmine.SpyObj<MatDialog>;

  let activatedRouteSpy: jasmine.SpyObj<ActivatedRoute>;

  beforeEach(async () => {
    // Create a spy for MatDialog
    dialogSpy = jasmine.createSpyObj('MatDialog', ['open']);

    let mockActivatedRoute: Partial<ActivatedRoute>;

    // Simulate route parameters if needed (assuming you're using snapshot.paramMap.get)
    // activatedRouteSpy.snapshot = {
    //   paramMap: { get: (key: string) => key === 'id' ? '123' : null }
    // } as any;

    await TestBed.configureTestingModule({
      imports: [MatButtonModule, CommentItemComponent, RouterModule],
      providers: [
        { provide: MatDialog, useValue: dialogSpy },
        // { provide: ActivatedRoute, useValue: activatedRouteSpy }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CommentItemComponent);
    component = fixture.componentInstance;

    // Mock the input data
    component.comment = { id: 1, content: 'This is a comment' } as Comment;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should toggle editing mode when onEdit is called', () => {
    component.onEdit();

    expect(component.isEditing).toBeTrue();
    expect(component.editableContent).toBe('This is a comment');
  });

  it('should emit commentEdited when saveEdit is called', () => {
    spyOn(component.commentEdited, 'emit');

    component.onEdit();
    component.editableContent = 'Updated comment';
    component.saveEdit();

    expect(component.commentEdited.emit).toHaveBeenCalledWith({
      id: 1,
      content: 'Updated comment',
    });
    expect(component.isEditing).toBeFalse();
  });

  it('should not emit commentEdited when cancelEdit is called', () => {
    spyOn(component.commentEdited, 'emit');

    component.onEdit();
    component.cancelEdit();

    expect(component.commentEdited.emit).not.toHaveBeenCalled();
    expect(component.isEditing).toBeFalse();
  });

  it('should open the confirmation dialog when onDelete is called', () => {
    const dialogRefSpyObj = jasmine.createSpyObj('MatDialogRef', ['afterClosed']);
    dialogSpy.open.and.returnValue(dialogRefSpyObj);

    // Simulate a dialog close with a confirmation
    dialogRefSpyObj.afterClosed.and.returnValue(of(true));

    spyOn(component.commentDeleted, 'emit');
    component.onDelete();

    expect(dialogSpy.open).toHaveBeenCalledWith(ConfirmationDialogComponent, {
      width: '300px',
      data: { message: 'Are you sure you want to delete this comment?' }
    });

    expect(component.commentDeleted.emit).toHaveBeenCalledWith(1);
  });

  it('should not emit commentDeleted if the dialog is cancelled in onDelete', () => {
    const dialogRefSpyObj = jasmine.createSpyObj('MatDialogRef', ['afterClosed']);
    dialogSpy.open.and.returnValue(dialogRefSpyObj);

    // Simulate a dialog close with no confirmation (false)
    dialogRefSpyObj.afterClosed.and.returnValue(of(false));

    spyOn(component.commentDeleted, 'emit');
    component.onDelete();

    expect(dialogSpy.open).toHaveBeenCalledWith(ConfirmationDialogComponent, {
      width: '300px',
      data: { message: 'Are you sure you want to delete this comment?' }
    });

    expect(component.commentDeleted.emit).not.toHaveBeenCalled();
  });
});
