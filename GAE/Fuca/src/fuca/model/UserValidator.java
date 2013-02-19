package fuca.model;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

public class UserValidator  {

	private String required = "Obavezno polje";

	public boolean supports(Class candidate) {
		return User.class.isAssignableFrom(candidate);
	}

	public void validate(Object obj, Errors errors) {

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "required",
				required);
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastname", "required",
				required);
	}
}