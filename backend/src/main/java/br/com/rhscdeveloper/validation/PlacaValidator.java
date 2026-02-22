package br.com.rhscdeveloper.validation;

import br.com.rhscdeveloper.util.Constantes;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PlacaValidator implements ConstraintValidator<Placa, String> {

	private static final String REGEX_PLACA = "^[a-zA-Z0-9]{7}$";
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value == null) {
			return true;
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
