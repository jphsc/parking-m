package br.com.rhscdeveloper.enumerator;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

import br.com.rhscdeveloper.model.EnumCodId;
import br.com.rhscdeveloper.util.Constantes;

public class Enums {
	
	public static <E extends Enum<E> & EnumCodId> E getEnum(Class<E> enumClass, Integer id, String tipoEnum) {

		E resultado = Arrays.stream(enumClass.getEnumConstants())
				.filter(e -> Objects.equals(e.getId(), id))
				.findFirst()
				.orElseThrow(() -> new NoSuchElementException(String.format(Constantes.MGS_ENUMERADOR_INVALIDO, tipoEnum)));
		
		return Objects.requireNonNull(resultado);
		
	}
	
	public enum Booleano implements EnumCodId {
		
		NAO(0, "Não"),
		SIM(1, "SIM");
		
		private Integer id;
		private String descricao;
		
		private Booleano(Integer id, String descricao) {
			this.id = id;
			this.descricao = descricao;
		}

		@Override
		public Integer getId() {
			return id;
		}

		public String getDescricao() {
			return descricao;
		}
	}
	
	public enum Situacao implements EnumCodId {
		ATIVO(2, "Ativo"),
		INATIVO(3, "Inativo"),
		CADASTRADO(4, "Cadastrado");
		
		private Integer id;
		private String descricao;
		
		private Situacao(Integer id, String descricao) {
			this.id = id;
			this.descricao = descricao;
		}

		@Override
		public Integer getId() {
			return id;
		}

		public String getDescricao() {
			return descricao;
		}
	}
	
	public enum TipoCobranca implements EnumCodId {
		DINHEIRO(4, "Dinheiro"),
		DEBITO(5, "Débito"),
		CREDITO(6, "Crédito"),
		INDIFERENTE(7, "Indiferente");
		
		private Integer id;
		private String descricao;
		
		private TipoCobranca(Integer id, String descricao) {
			this.id = id;
			this.descricao = descricao;
		}

		@Override
		public Integer getId() {
			return id;
		}

		public String getDescricao() {
			return descricao;
		}
	}
	
	public enum TipoMovimento implements EnumCodId {
		HORA(18, "Hora"),
		DIA(8, "Dia Util"),
		FINAL_SEMANA(9, "Final de Semana"),
		MENSALISTA(10, "Mensalista"),
		INDIFERENTE(11, "Indiferente"), ;
		
		private Integer id;
		private String descricao;
		
		private TipoMovimento(Integer id, String descricao) {
			this.id = id;
			this.descricao = descricao;
		}

		@Override
		public Integer getId() {
			return id;
		}

		public String getDescricao() {
			return descricao;
		}
	}
	
	public enum SituacaoMovimento implements EnumCodId {
		
		ABERTO(12, "Aberto"),
		ENCERRADO(13, "Encerrado");
		
		private Integer id;
		private String descricao;
		
		private SituacaoMovimento(Integer id, String descricao) {
			this.id = id;
			this.descricao = descricao;
		}

		@Override
		public Integer getId() {
			return id;
		}

		public String getDescricao() {
			return descricao;
		}
	}
	
	public enum TipoOperacao implements EnumCodId {
		
		CONSULTAR_FILTRO(14, "Consultar"),
		CADASTRAR(15, "Cadastrar"),
		EDITAR(16, "Editar"),
		EXCLUIR(17, "Excluir");
		
		private Integer id;
		private String descricao;
		
		private TipoOperacao(Integer id, String descricao) {
			this.id = id;
			this.descricao = descricao;
		}

		@Override
		public Integer getId() {
			return id;
		}

		public String getDescricao() {
			return descricao;
		}
	}
}
