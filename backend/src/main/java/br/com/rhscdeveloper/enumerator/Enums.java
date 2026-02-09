package br.com.rhscdeveloper.enumerator;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.NoSuchElementException;

import br.com.rhscdeveloper.model.EnumCodId;
import br.com.rhscdeveloper.util.Constantes;
import jakarta.validation.constraints.NotNull;

public class Enums {

	// último id reservado = 18
	public static Object getEnum(Integer id) throws NoSuchMethodException, SecurityException, ClassNotFoundException {
		
		return Arrays.stream(Enums.class.getDeclaredClasses())
		        .filter(Class::isEnum)
		        .flatMap(c -> Arrays.stream(c.getEnumConstants()))
		        .filter(e -> {
		            try {
		                Method getId = e.getClass().getMethod("getId");
		                return id.equals(getId.invoke(e));
		            } catch (Exception ex) {
		                return false;
		            }
		        })
		        .findFirst()
		        .orElseThrow(() -> new NoSuchElementException("Enumerador não encontrado"));
	}
	
	public static <E extends Enum<E> & EnumCodId> @NotNull E getEnum(Class<E> enumClass, @NotNull Integer id, String tipoEnum) {
		return Arrays.stream(enumClass.getEnumConstants())
			.filter(e -> e.getId().equals(id))
			.findFirst()
			.orElseThrow(() -> new NoSuchElementException(String.format(Constantes.MGS_ENUMERADOR_INVALIDO, tipoEnum)));
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
		
		CONSULTAR(14, "Consultar"),
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
