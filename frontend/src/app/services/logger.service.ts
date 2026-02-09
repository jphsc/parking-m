import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../environment/environment.prd';
import { Logger } from '../models/logger.model';

@Injectable({
  providedIn: 'root'
})
export class LoggerService {

  private readonly urlBackend = `${environment.baseUrlBackend}/log`;
  private logger!:Logger;

  constructor(private http:HttpClient) { }

  log(msg:string):void {
    console.log(msg);

    this.logger.mensagem = msg;
    this.logger.tipo = 'log';

    this.http.post(this.urlBackend, this.logger);
  }

  warn(msg:string):void {
    console.warn(msg);

    this.logger.mensagem = msg;
    this.logger.tipo = 'warn';

    this.http.post(this.urlBackend, this.logger);
  }

  error(msg:string):void {
    console.error(msg);

    this.logger.mensagem = msg;
    this.logger.tipo = 'error';

    this.http.post(this.urlBackend, this.logger);
  }
}
