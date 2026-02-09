import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../environment/environment.dev';
import { Veiculo } from '../models/veiculo.model';
import { Observable } from 'rxjs';
import { RespostaReqBackend } from '../models/resposta.model';

@Injectable({
  providedIn: 'root',
})
export class VeiculoService {
  private baseUrlBackend = `${environment.baseUrlBackend}/veiculo`;

  constructor(private http: HttpClient) {}

  getVeiculos(): Observable<RespostaReqBackend<Veiculo>> {
    return this.http.get<RespostaReqBackend<Veiculo>>(`${this.baseUrlBackend}/veiculos`);
  }

  getVeiculoById(id: number): Observable<RespostaReqBackend<Veiculo>> {
    return this.http.get<RespostaReqBackend<Veiculo>>(`${this.baseUrlBackend}/${id}`);
  }

  getVeiculoByFilter(v: Veiculo): Observable<RespostaReqBackend<Veiculo>> {
    return this.http.post<RespostaReqBackend<Veiculo>>(`${this.baseUrlBackend}`, v);
  }

  createVeiculo(v: Veiculo): Observable<RespostaReqBackend<Veiculo>> {
    return this.http.post<RespostaReqBackend<Veiculo>>(`${this.baseUrlBackend}/cadastrar`, v);
  }

  updateVeiculo(v: Veiculo): Observable<RespostaReqBackend<Veiculo>> {
    return this.http.put<RespostaReqBackend<Veiculo>>(`${this.baseUrlBackend}/atualizar`, v);
  }
}
