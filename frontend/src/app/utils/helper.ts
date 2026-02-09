export interface ItemEnum {
  id: number;
  descricao: string;
}

export interface Enumerador{
  [key: string]: ItemEnum;
}

export enum Acao {
  EDITAR,
  CADASTRAR
}

export abstract class Enumeradores {

  public static factory<T extends keyof typeof Enumeradores>(tipo: T) {

    const enumerador = this[tipo] as Enumerador;

    return {
      getDescricao: (id: number): string => {
        let desc: string = "";
        for(const key in enumerador) {
          if(enumerador.hasOwnProperty(key)) {
            if(enumerador[key].id === id) {
              desc = enumerador[key].descricao;
            }
          }
        }
        return desc;
      }
    }
  }

  public static readonly Booleano: Enumerador = {
    NAO: { id: 0, descricao: 'Não' },
    SIM: { id: 1, descricao: 'Sim' }
  };

  public static readonly Situacao: Enumerador = {
    ATIVO :{ id: 2, descricao: 'Ativo' },
    INATIVO: { id: 3, descricao: 'Inativo' },
    CADASTRADO: { id: 4, descricao: 'Cadastrado' }
  };

  public static readonly TipoCobranca: Enumerador = {
    DINHIEIRO: { id: 5, descricao: 'Dinheiro' },
    DEBITO: { id: 6, descricao: 'Débito' },
    CREDITO: { id: 7, descricao: 'Crédito' },
    INDIFERENTE: { id: 8, descricao: 'Indiferente' }
  };

  public static readonly TipoMovVeiculo: Enumerador = {
    HORA: { id: 18, descricao: 'Por Hora' },
    DIA: { id: 8, descricao: 'Diário' },
    FINAL_SEMANA: { id: 9, descricao: 'Final de Semana' },
    MENSALISTA: { id: 10, descricao: 'Mensalista' },
    INDIFERENTE: { id: 11, descricao: 'Indiferente' }
  };

  public static readonly SituacaoMovimento: Enumerador = {
    ABERTO: { id: 12, descricao: 'Aberto' },
    ENCERRADO: { id: 13, descricao: 'Encerrado'}
  };

  public static readonly TipoRequest: Enumerador = {
    CONSULTAR: { id: 14, descricao: 'Consultar' },
    CADASTRAR: { id: 15, descricao: 'Cadastrar' },
    EDITAR: { id: 16, descricao: 'Editar' },
    EXCLUIR: { id: 17, descricao: 'Excluir' }
  };
}
