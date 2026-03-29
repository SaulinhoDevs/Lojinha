import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CategoriaDetails } from './categoria-details';

describe('CategoriaDetails', () => {
  let component: CategoriaDetails;
  let fixture: ComponentFixture<CategoriaDetails>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CategoriaDetails]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CategoriaDetails);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
