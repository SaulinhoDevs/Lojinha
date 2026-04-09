import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VendaDetails } from './venda-details';

describe('VendaDetails', () => {
  let component: VendaDetails;
  let fixture: ComponentFixture<VendaDetails>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VendaDetails]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VendaDetails);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
