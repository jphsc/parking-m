package br.com.rhscdev.interfaces.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumeradorValidator.class)
@Documented
public @interface Enumerador {

	Class<? extends Enum<?>> tipo();
	
	String message() default "Valor inválido para o campo";

	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
}
