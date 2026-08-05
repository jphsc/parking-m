import { AbstractControl, FormGroup, ValidationErrors, ValidatorFn } from "@angular/forms";
import { Enumeradores } from "./helper";

export abstract class Utils {
  public static readonly PAGINA_UM = 1;
  public static readonly VERDADEIRO = true;
  public static readonly FALSO = false;
  public static readonly REGISTROS_POR_PAGINA = 10;

  public static readonly CRIAR_VEICULO: number = 1;
  public static readonly INFORMAR_VEICULO: number = 2;

  public static readonly ERRO_PADRAO_CARREGAR_DADOS = 'Não foi possível carregar os dados, tente novamente mais tarde';
  public static readonly ERRO_GERAL_PADRAO = 'Não foi possível realizar a operação, tente novamente mais tarde';
  public static readonly ERRO_ATUALIZAR_REGISTRO = "Erro ao atualizar o registro";

  public static readonly SUCESSO_CRIAR_VEICULO = "Veículo criado com a placa: ";

  public static formatarPlaca(placa: string): string {
    return placa
      .replaceAll(/[^a-zA-Z0-9]/g, '')
      .toUpperCase()
      .substring(0, 7);
  }

  public static uppercaseOnly(valor: string): string {
    return valor.toUpperCase();
  }

  public static obterQtdPaginas(totalRegistros: number, qtdRegistrosPorPagina: number): number[] {
    return Array.from(
      { length: Math.ceil(totalRegistros / qtdRegistrosPorPagina) }, (_, i) => i + 1,
    );
  }

  public static paginarRegistros(totalRegistros: number, qtdRegistrosPorPagina: number): boolean {
    return totalRegistros >= qtdRegistrosPorPagina
      ? Utils.VERDADEIRO
      : Utils.FALSO;
  }

  public static getMensagemErro(erro: any, msgAlternativa: string): string {
    return (erro == undefined || erro == null) ? msgAlternativa : erro.error.mensagem;
  }
}
