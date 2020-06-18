package it.uniroma3.siw.taskmanager.controller;

import org.springframework.validation.Errors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import it.uniroma3.siw.taskmanager.model.Credentials;
import it.uniroma3.siw.taskmanager.service.CredentialsService;

import org.springframework.validation.Validator;


@Component
public class CredentialsValidator implements Validator{

	@Autowired
	CredentialsService credentialsService;
	
	
	final Integer MAX_USERNAME_LENGTH = 20;
	final Integer MIN_USERNAME_LENGTH = 4;
	final Integer MAX_PASSWORD_LENGTH = 20;
	final Integer MIN_PASSWORD_LENGTH = 6;

	@Override
	public void validate(Object o, Errors errors) {

		Credentials credentials = (Credentials) o;
		String userName = credentials.getUserName();
		String password = credentials.getPassword();

		// per i campi vuoti e lunghezze minori o superiori
		if(userName.isEmpty())  
			errors.rejectValue("userName", "required");  // con messaggio di errrore "required"
		else if(userName.length() < MIN_USERNAME_LENGTH || userName.length() > MAX_USERNAME_LENGTH)
			errors.rejectValue("userName", "size");	
		else if(this.credentialsService.getCredentialsByUserName(userName) != null)
			errors.rejectValue("userName", "duplicate");
		
		if(password.isEmpty())  
			errors.rejectValue("password", "required");  // con messaggio di errrore "required"
		else if(password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH)
			errors.rejectValue("password", "size");
	}

	@Override
	// Specifica che la classe che andiamo ad utilizzare è la classe User
	public boolean supports(Class<?> aClass) {
		return Credentials.class.equals(aClass);
	}
}
