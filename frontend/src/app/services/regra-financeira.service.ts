import { Injectable } from '@angular/core';
import { environment } from '../environment/environment.dev';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RegraFinanceira } from '../models/regra-financeira';
import { RespostaReqBackend } from '../models/resposta.model';

@Injectable({
  providedIn: 'root'
})
export class RegraFinanceiraService {

  private baseUrlBackend = `${environment.baseUrlBackend}/regra-financeira`;

  constructor(private http: HttpClient) { }

  getRegraById(id:number): Observable<RespostaReqBackend<RegraFinanceira>> {
    return this.http.get<RespostaReqBackend<RegraFinanceira>>(`${this.baseUrlBackend}/${id}`);
  }

  getAllRegras(pagina: number, qtdRegistrosPorPagina: number): Observable<RespostaReqBackend<RegraFinanceira>> {
    return this.http.get<RespostaReqBackend<RegraFinanceira>>(`${this.baseUrlBackend}?pagina=${pagina}&quantidade=${qtdRegistrosPorPagina}`);
  }

  createRegra(regra:RegraFinanceira): Observable<RespostaReqBackend<RegraFinanceira>> {
    return this.http.post<RespostaReqBackend<RegraFinanceira>>(`${this.baseUrlBackend}`, regra);
  }

  updateRegra(regra:RegraFinanceira): Observable<RespostaReqBackend<RegraFinanceira>> {
    return this.http.put<RespostaReqBackend<RegraFinanceira>>(`${this.baseUrlBackend}`, regra);
  }

  deleteRegra(id:number): Observable<RespostaReqBackend<RegraFinanceira>> {
    return this.http.delete<RespostaReqBackend<RegraFinanceira>>(`${this.baseUrlBackend}/${id}`);
  }
}
