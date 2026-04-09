import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VendaForm } from './venda-form';

describe('VendaForm', () => {
  let component: VendaForm;
  let fixture: ComponentFixture<VendaForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VendaForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VendaForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
