package br.com.rhscdeveloper.util;

public abstract class Constantes {

	// Codigos erro do sistema
	public static final int COD_ERRO_INEXISTENTE = 100;
	public static final int COD_ERRO_INTERNO = 300;
	public static final int COD_ERRO_VALIDACAO_REGISTRO = 400;
	public static final int COD_ERRO_VALIDACAO_METHOD = 401;
	public static final int COD_ERRO_SERVIDOR_INTERNO = 999;
	
	// Mensagens de erro
	public static final String MSG_ERRO_GENERICO = "Sistema temporariamente indisponível, tente novamente mais tarde";
	public static final String MSG_ERRO_NAO_ENCONTRADO = "O registro solicitado não foi encontrado, verifique a solicitação";
	public static final String MSG_ERRO_VINCULO = "O registro solicitado possui vínculos que impedem a exclusão, verifique os vínculos.";
	public static final String MSG_ERRO_ID = "O identificador do registro não foi informado, informe o identificador para realizar a operção desejada.";
	public static final String MSG_ERRO_NULL = "Há campos obrigatórios que não foram informados no registro.";
	public static final String MSG_ERRO_CAMPOS = "Há campos obrigatórios que não foram informados no registro.";
	public static final String MSG_ERRO_ID_REGISTRO = "É necessário informar o id do registro";
	public static final String MGS_ENUMERADOR_INVALIDO = "%s informado(a) é inválido(a)";
	public static final String DESC_ENUM_TIPO_COBRANCA = "Tipo de Cobranca";
	public static final String DESC_ENUM_SITUACAO = "Situacao";
	public static final String DESC_ENUM_TIPO_MOVIMENTO = "Tipo de Movimento";
	
	public static final String MSG_SUCESSO_REGISTROS_ENCONTRADOS = "Registro(s) encontrado(s) com sucesso";
	public static final String MSG_SUCESSO_REGISTROS_NAO_ENCONTRADOS = "Registro(s) solicitados não encontrado(s)!";
	public static final String MSG_SUCESSO_REGISTRO_EXCLUIDO = "Registro excluído com sucesso";
	public static final String MSG_SUCESSO_REGISTRO_ATUALIZADO = "Registro atualizado com sucesso";
	public static final String MSG_ERRO_PLACA_EXISTE = "A placa informada já existe no sistema. Verifique se o veículo está correto";
	public static final String MSG_ERRO_METODO_INVALIDO = "O método requisitado não está válido, verifique o path e parâmtros";
	public static final String MSG_SUCESSO_CADASTRADO = "Registro cadastrado com sucesso";
	public static final String MSG_SUCESSO_ATUALIZADO = "Registro atualizado com sucesso";
	public static final String MSG_SUCESSO_EXCLUIDO = "Registro excluído com sucesso";
}
