package it.uniroma3.siw.taskmanager.controller;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import it.uniroma3.siw.taskmanager.model.User;

@Component
public class UserValidator implements Validator {

	final Integer MAX_NAME_LENGTH = 200;
	final Integer MIN_NAME_LENGTH = 2;

	@Override
	public void validate(Object o, Errors errors) {

		User user = (User) o;
		String firstName = user.getFirstName().trim();
		String lastName = user.getLastName().trim();

		// per i campi vuoti e lunghezze minori o superiori
		if(firstName.isEmpty())  //.isBlank() ??
			errors.rejectValue("firstName", "required");  // con messaggio di errrore "required"
		else if(firstName.length() < MIN_NAME_LENGTH || firstName.length() > MAX_NAME_LENGTH)
			errors.rejectValue("firstName", "size");

		if(lastName.isEmpty())  //.isBlank() ??
			errors.rejectValue("lastName", "required");  // con messaggio di errrore "required"
		else if(lastName.length() < MIN_NAME_LENGTH || lastName.length() > MAX_NAME_LENGTH)
			errors.rejectValue("lastName", "size");

	}

	// Specifica che la classe che andiamo ad utilizzare è la classe User
	@Override
	public boolean supports(Class<?> aClass) {
		return User.class.equals(aClass);
	}
}

