package fuca.model;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

public class TerminValidator  {

	private String required = "Obavezno polje";

	public boolean supports(Class candidate) {
		return Termin.class.isAssignableFrom(candidate);
	}

	public void validate(Object obj, Errors errors) {

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "required",
				required);
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "date", "required",
				required);
	}
}