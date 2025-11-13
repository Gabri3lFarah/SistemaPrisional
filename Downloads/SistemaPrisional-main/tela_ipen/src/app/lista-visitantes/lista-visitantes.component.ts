import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ApiService } from '../services/api.service';

@Component({
  selector: 'app-lista-visitantes',
  templateUrl: './lista-visitantes.component.html',
  styleUrls: ['./lista-visitantes.component.css'],
  standalone: true,
  imports: [CommonModule, RouterModule]
})
export class ListaVisitantesComponent implements OnInit {
  prisioneiros: any[] = [];

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.apiService.getPrisoners().subscribe(data => {
      console.log('Prisioneiros recebidos:', data);
      this.prisioneiros = data;
    });
  }
}
