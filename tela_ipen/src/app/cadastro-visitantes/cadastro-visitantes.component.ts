import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ApiService } from '../services/api.service';

@Component({
  selector: 'app-cadastro-visitantes',
  templateUrl: './cadastro-visitantes.component.html',
  styleUrls: ['./cadastro-visitantes.component.css'],
  standalone: true,
  imports: [FormsModule, CommonModule, RouterModule]
})
export class CadastroVisitantesComponent implements OnInit {
  visitante = { nome: '', eAdvogado: false, nomeAdvogado: '', prisioneiroId: null as number | null, authorizationCode: null as number | null };
  velhoVirilId = 41;
  prisioneiros: any[] = [];

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.apiService.getPrisoners().subscribe({
      next: data => {
        this.prisioneiros = data;
      },
      error: err => {
        console.error('Erro ao buscar prisioneiros:', err);
      }
    });
  }

  verificarAdvogado() {
    if (!this.visitante.eAdvogado) {
      this.visitante.nomeAdvogado = '';
      this.visitante.authorizationCode = null;
    }
  }

  cadastrarVisitante() {
    // Garantir que um prisioneiro foi selecionado antes do cadastro
    if (!this.visitante.prisioneiroId) {
      alert('Erro: É necessário selecionar um preso antes de cadastrar um visitante.');
      return;
    }

    // Regras para o Velho Viril
    if (!this.visitante.eAdvogado && +this.visitante.prisioneiroId === this.velhoVirilId) {
      alert('Só advogados podem visitar o Velho Viril!');
      return;
    }

    if (this.visitante.eAdvogado && +this.visitante.prisioneiroId === this.velhoVirilId) {
      if (this.visitante.authorizationCode !== 666) {
        alert('Só advogados com código 666 podem visitar o Velho Viril!');
        return;
      }
    }

    // Montar o objeto esperado pelo backend
    const payload = {
      visitorName: this.visitante.nome,
      lawyer: this.visitante.eAdvogado,
      authorizationCode: this.visitante.authorizationCode,
      prisioneiroId: this.visitante.prisioneiroId
    };

    this.apiService.registerVisit(payload).subscribe({
      next: () => alert('Cadastro de visitante realizado!'),
      error: err => alert('Erro ao cadastrar visitante: ' + err.error.message)
    });
  }
}
