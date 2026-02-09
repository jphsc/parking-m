import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../environment/environment.dev';
import { Observable } from 'rxjs';
import { MovimentoVeiculo } from '../models/movimento-veiculo.model';
import { RespostaReqBackend } from '../models/resposta.model';

@Injectable({
  providedIn: 'root',
})
export class MovimentoVeiculoService {
  private baseUrlBackend = `${environment.baseUrlBackend}/movimento-veiculo`;

  constructor(private http: HttpClient) {}

  getMovimentosAbertos(): Observable<RespostaReqBackend<MovimentoVeiculo>> {
    let filtro: MovimentoVeiculo = {
      situacao: 12, // 12 ABERTO | 13 ENCERRADO
    };

    return this.http
      .post<RespostaReqBackend<MovimentoVeiculo>>(this.baseUrlBackend, filtro);
  }

  getAllMovimentos():Observable<RespostaReqBackend<MovimentoVeiculo>> {
    return this.http
      .get<RespostaReqBackend<MovimentoVeiculo>>(`${this.baseUrlBackend}/movimentos`);
  }

  getMovimentoById(id: number):Observable<RespostaReqBackend<MovimentoVeiculo>> {
    return this.http
      .get<RespostaReqBackend<MovimentoVeiculo>>(`${this.baseUrlBackend}/${id}`);
  }
}
