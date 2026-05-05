import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplyWfh } from '../../../core/services/apply-wfh';

describe('ApplyWfh', () => {
  let component: ApplyWfh;
  let fixture: ComponentFixture<ApplyWfh>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApplyWfh]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ApplyWfh);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
