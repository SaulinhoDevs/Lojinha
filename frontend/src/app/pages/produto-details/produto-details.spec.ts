import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProdutoDetails } from './produto-details';

describe('ProdutoDetails', () => {
  let component: ProdutoDetails;
  let fixture: ComponentFixture<ProdutoDetails>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProdutoDetails]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProdutoDetails);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
