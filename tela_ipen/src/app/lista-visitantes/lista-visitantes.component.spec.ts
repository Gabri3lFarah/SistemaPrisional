import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListaVisitantesComponent } from './lista-visitantes.component';

describe('ListaVisitantesComponent', () => {
  let component: ListaVisitantesComponent;
  let fixture: ComponentFixture<ListaVisitantesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListaVisitantesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListaVisitantesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
