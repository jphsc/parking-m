package br.com.rhscdeveloper.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PlacaValidator implements ConstraintValidator<Placa, String> {

	private static final String REGEX_PLACA = "^[a-zA-Z0-9]{7}$";
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value == null) {
			return true;
		}
		
		return value.matches(REGEX_PLACA);
	}

}
