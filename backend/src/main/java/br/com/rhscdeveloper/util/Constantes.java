package br.com.rhscdeveloper.util;

public abstract class Constantes {

	// Codigos erro do sistema
	public static final int COD_ERRO_INEXISTENTE = 100;
	public static final int COD_ERRO_INTERNO = 300;
	public static final int COD_ERRO_VALIDACAO_REGISTRO = 400;
	public static final int COD_ERRO_VALIDACAO_METHOD = 401;
	public static final int COD_ERRO_SERVIDOR_INTERNO = 999;
	public static final int NRO_MAX_REGISTROS_PAGINACAO = 20;
	
	// generico
	public static final String MSG_ERRO_VINCULO = "O registro solicitado possui vínculos que impedem a exclusão, verifique os vínculos.";
	public static final String MSG_ERRO_PAGINA_INVALIDA = "É necessário informar um número de página válido como parâmetro query ?pagina=1";
	public static final String MSG_ERRO_CAMPOS = "Há campos obrigatórios que não foram informados.";
	public static final String MSG_ERRO_GENERICO = "Sistema temporariamente indisponível, tente novamente mais tarde";
	public static final String MSG_SEM_ID_REGISTRO = "O id do registro não foi informado ou é inválido, informe um id válido para realizar a operação desejada.";
	public static final String MSG_SUCESSO_REGISTROS_ENCONTRADOS = "Registro(s) encontrado(s) com sucesso";
	public static final String MSG_REGISTROS_NAO_ENCONTRADOS = "Registro(s) solicitados não encontrado(s)!";
	public static final String MSG_ERRO_METODO_INVALIDO = "O método requisitado não está válido, verifique o path e parâmtros";
	public static final String MSG_SUCESSO_CADASTRADO = "Registro cadastrado com sucesso";
	public static final String MSG_SUCESSO_ATUALIZADO = "Registro atualizado com sucesso";
	public static final String MSG_SUCESSO_REGISTRO_EXCLUIDO = "Registro excluído com sucesso";

	// enumeradores
	public static final String DESC_ENUM_TIPO_COBRANCA = "Tipo de Cobranca";
	public static final String DESC_ENUM_SITUACAO = "Situacao";
	public static final String DESC_ENUM_TIPO_MOVIMENTO = "Tipo de Movimento";
	public static final String MGS_ENUMERADOR_INVALIDO = "%s informado(a) é inválido(a)";
	
	// veiculo
	public static final String MSG_ERRO_VEICULO_NAO_ENCONTRADO = "Veículo de id %d não encontrado.";
	public static final String MSG_ERRO_TAMANHO_PLACA = "A placa deve ter no 7 caracteres alfanuméricos, sem caracteres especiais";
	public static final String MSG_ERRO_PLACA_EXISTE = "A placa informada já existe no sistema. Verifique se a placa está correta";
	public static final String MSG_SEM_PLACA = "É necessário informar a placa do veículo";
	public static final String MSG_SEM_MODELO = "É necessário informar o modelo do veículo";
	public static final String MSG_SEM_MONTADORA = "É necessário informar a montadora do veículo";
	
	// movimento veiculo
	public static final String MSG_SUCESSO_MOV_ENCERRADO = "Movimento encerrado com sucesso";
	
}
