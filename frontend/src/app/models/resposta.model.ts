export interface RespostaReqBackend<T>{
  registros: T[];
  mensagem: string;
  quantidade: number;
  pagina: number;
}
