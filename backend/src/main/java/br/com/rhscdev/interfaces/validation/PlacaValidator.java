package br.com.rhscdev.interfaces.validation;

import br.com.rhscdev.infrastructure.config.Constantes;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PlacaValidator implements ConstraintValidator<Placa, String> {

	private static final String REGEX_PLACA = "^[a-zA-Z0-9]{7}$";
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value == null) {
			return false;
		}
		
		boolean isValid = value.matches(REGEX_PLACA);
		
		if(!isValid) {
			context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(Constantes.MSG_ERRO_TAMANHO_PLACA)
                   .addConstraintViolation();
		}
		
		return isValid;
	}

}
