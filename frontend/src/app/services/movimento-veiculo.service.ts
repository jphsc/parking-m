import { HttpClient, HttpStatusCode } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../environment/environment.dev';
import { Observable } from 'rxjs';
import { MovimentoVeiculo } from '../models/movimento-veiculo.model';
import { RespostaReqBackend } from '../models/resposta.model';
import { MovimentoEncerrar } from '../models/movimento-encerrar-model';

@Injectable({
  providedIn: 'root',
})
export class MovimentoVeiculoService {
  private baseUrlBackend = `${environment.baseUrlBackend}/movimento-veiculo`;

  constructor(private http: HttpClient) {}

  getMovimentosAbertos(pagina: number, qtdRegistros:number): Observable<RespostaReqBackend<MovimentoVeiculo>> {
    return this.http
      .get<RespostaReqBackend<MovimentoVeiculo>>(`${this.baseUrlBackend}/movimentos?pagina=${pagina}&quantidade=${qtdRegistros}`);
  }

  getAllMovimentos(pagina: number): Observable<RespostaReqBackend<MovimentoVeiculo>> {
    return this.http
      .get<RespostaReqBackend<MovimentoVeiculo>>(`${this.baseUrlBackend}?pagina=${pagina}`);
  }

  getMovimentoPorId(id: number): Observable<RespostaReqBackend<MovimentoVeiculo>> {
    return this.http
      .get<RespostaReqBackend<MovimentoVeiculo>>(`${this.baseUrlBackend}/${id}`);
  }

  fecharMovimento(movimento: MovimentoEncerrar): Observable<RespostaReqBackend<MovimentoVeiculo>> {
    return this.http
      .put<RespostaReqBackend<MovimentoVeiculo>>(`${this.baseUrlBackend}`, movimento);
  }

  criarMovimento(movimento: MovimentoVeiculo): Observable<RespostaReqBackend<MovimentoVeiculo>> {
    return this.http
      .post<RespostaReqBackend<MovimentoVeiculo>>(`${this.baseUrlBackend}`, movimento);
  }
}
