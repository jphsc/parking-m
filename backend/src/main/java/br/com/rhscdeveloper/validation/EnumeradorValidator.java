package br.com.rhscdeveloper.validation;

import java.util.Arrays;
import java.util.Objects;

import br.com.rhscdeveloper.model.EnumCodId;
import br.com.rhscdeveloper.util.Constantes;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumeradorValidator implements ConstraintValidator<Enumerador, Integer> {

	private Class<? extends Enum<?>> tipoEnum;
	private String nomeEnum;

	@Override
	public void initialize(Enumerador annotation) {
		this.tipoEnum = annotation.tipo();
		this.nomeEnum = tipoEnum.getSimpleName();
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {

		if (value == null)
			return true;

		if (!EnumCodId.class.isAssignableFrom(tipoEnum))
			return true;
		
		boolean valido = Arrays.stream(tipoEnum.getEnumConstants())
				.map(constante -> (EnumCodId) constante)
				.anyMatch(enumCodId -> Objects.equals(enumCodId.getId(), value));

		if (!valido) {
            context.disableDefaultConstraintViolation();
            context
            	.buildConstraintViolationWithTemplate(String.format(Constantes.MGS_ENUMERADOR_INVALIDO, nomeEnum))
            	.addConstraintViolation();
            return false;
        }
        
        return true;
	}

}
