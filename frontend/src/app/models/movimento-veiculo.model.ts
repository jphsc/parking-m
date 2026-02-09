export interface MovimentoVeiculo {
  id?: number;
  idVeiculo?: number;
  idRegra?: number;
  placa?: string;
  tipoMovimento?: any;
  dtHrEntrada?: Date;
  dtHrSaida?: Date;
  situacao?: any;
  versao?: Date;
}
