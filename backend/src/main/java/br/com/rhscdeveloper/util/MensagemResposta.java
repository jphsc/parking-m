package br.com.rhscdeveloper.util;

import br.com.rhscdeveloper.enumerator.Enums.TipoOperacao;

public class MensagemResposta {
	
	public String gerarMensagem(TipoOperacao tipoOperacao, Class<?> classe) {

		String mensagem = "";
		String objeto = classe.getCanonicalName();
		String nomeHierarquiaObj[] = objeto.split("\\.");
		String nomeClasseVO = nomeHierarquiaObj[nomeHierarquiaObj.length - 1];
		String nomeClasse = nomeClasseVO.substring(0, nomeClasseVO.length()-2);
		
		switch(tipoOperacao){
		case CONSULTAR:
			mensagem = "Registro(s) de "+nomeClasse+" obtido(s) com sucesso";
			break;
		case CADASTRAR:
			mensagem = "Registro de "+nomeClasse+" realizado com sucesso";
			break;
		case EDITAR:
			mensagem = "Registro de "+nomeClasse+" alterado com sucesso";
			break;
		case EXCLUIR:
			mensagem = "Registro de "+nomeClasse+" removido com sucesso";
			break;
		default:
			mensagem = "Não foi possível realizar a operação solicitada";
			
		}
		return mensagem;
	}
}
