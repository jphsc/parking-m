package br.com.rhscdeveloper.util;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import br.com.rhscdeveloper.dto.BaseDTO;
import br.com.rhscdeveloper.enumerator.Enums.TipoOperacao;
import br.com.rhscdeveloper.exception.GlobalException;
import br.com.rhscdeveloper.model.BaseVO;
import jakarta.persistence.Column;

public abstract class Utils {

	public static <T extends BaseDTO, C extends BaseVO> boolean validarAtributosObrigatoriosOperacao(T obj, Class<C> clazz, TipoOperacao op) {
		
		if (op == TipoOperacao.EXCLUIR) {
            return validarCampoEspecifico(obj, "id");
        }

        Field[] camposClass = clazz.getDeclaredFields();
        
        try {
            boolean obrigatoriosPreenchidos = Arrays.stream(camposClass)
                    .map(f -> getAtributo(clazz, f.getName()))
                    .filter(f -> f != null && f.isAnnotationPresent(Column.class))
                    .filter(f -> !f.getAnnotation(Column.class).nullable())
                    .allMatch(f -> validar(f, obj));

            if (op == TipoOperacao.EDITAR) {
                return obrigatoriosPreenchidos && validarCampoEspecifico(obj, "id");
            }

            return obrigatoriosPreenchidos;
            
        } catch (Exception e) {
            throw new GlobalException("Falha na validação de atributos: " + e.getMessage());
        }
    }

    private static <T extends BaseDTO> boolean validarCampoEspecifico(T obj, String nomeCampo) {
    	Objects.requireNonNull(obj, "O objeto não pode ser nulo!");
        
        try {
            Field f = getAtributo(obj.getClass(), nomeCampo);
            if (f == null) return false;
            f.setAccessible(true);
            return f.get(obj) != null;
        } catch (Exception e) {
            return false;
        }
    }

    private static <T extends BaseDTO> boolean validar(Field field, T obj) {
        Objects.requireNonNull(obj, "O dto do objeto não pode ser nulo!");
        try {
            Field campoNoDto = getAtributo(obj.getClass(), field.getName());
            if (campoNoDto == null) return false;

            campoNoDto.setAccessible(true);
            Object valor = campoNoDto.get(obj);
            return valor != null;
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Erro ao acessar campo " + field.getName(), e);
        }
    }

    private static Field getAtributo(Class<?> clazzTipo, String fieldName) {
        try {
            return clazzTipo.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            if (clazzTipo.getSuperclass() != null) {
                return getAtributo(clazzTipo.getSuperclass(), fieldName);
            }
        }
        return null;
    }
    
    public static Integer getNroPaginaConsulta(Integer nroPagina) {
    	Integer aux = isNull(nroPagina) ? 0 : nroPagina;
    	return aux < 0 ? 0 : aux - 1;
    }
    
    public static Integer getNroPaginaResp(Integer nroPagina) {
    	Integer aux = isNull(nroPagina) ? 0 : nroPagina;
    	return aux < 0 ? 1 : aux + 1;
    }
    
	public static <T extends BaseVO> String getMensagemBuscaRegistro(T objeto) {
		return isNull(objeto) ? Constantes.MSG_REGISTROS_NAO_ENCONTRADOS : Constantes.MSG_REGISTROS_ENCONTRADOS;
	}
    
	public static <T extends BaseVO> String getMensagemBuscaRegistro(List<T> objeto) {
		return objeto.isEmpty() ? Constantes.MSG_REGISTROS_NAO_ENCONTRADOS : Constantes.MSG_REGISTROS_ENCONTRADOS;
	}
	
	@SuppressWarnings({ "null", "unlikely-arg-type" })
	public static <V extends BaseVO, D extends BaseDTO> void updateVoToDto(V vo, D dto) {
		
		List<String> camposIgnorados = new ArrayList<String>(Arrays.asList("id", "versao"));
		Field[] camposDto = dto.getClass().getDeclaredFields();
		
		try {
			for(Field campoDto : camposDto) {
				if(camposIgnorados.contains(campoDto)) continue;
				
				campoDto.setAccessible(true);
				Object valorCampoDto = campoDto.get(dto);
				
				if(nonNull(valorCampoDto)) {
					Field campoVo = vo.getClass().getDeclaredField(campoDto.getName());
					
					campoVo.setAccessible(true);
					
					Object currentValue = campoVo.get(vo);
                    if (!Objects.equals(valorCampoDto, currentValue)) {
                    	campoVo.set(vo, valorCampoDto);
                    }
				}
			}
		} catch (Exception e) {
			throw new GlobalException(Constantes.COD_ERRO_SERVIDOR_INTERNO, Constantes.MSG_ERRO_GENERICO);
		}
		
	}
}
