package it.uniroma3.siw.taskmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import javax.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import it.uniroma3.siw.taskmanager.model.Credentials;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.service.CredentialsService;


@Controller
public class AuthenticationController {

	@Autowired
	CredentialsService credentialsService;
	
	@Autowired
	UserValidator userValidator;
	
	@Autowired
	CredentialsValidator credentialsValidator;
	
	/**
	 * Method called when a GET request is sent by the user to URL "/register".
	 * This method prepares and dispatches the User registration view
	 * 
	 * @param model is the Request model
	 * @return the name of the target view "/register" 
	 */
	@RequestMapping(value = { "/users/register" }, method = RequestMethod.GET)
	public String showRegisterForm(Model model) {
		model.addAttribute("userForm", new User());
		model.addAttribute("credentialsForm", new Credentials());
		
		return "registerUser";
	}
	
	// Dopo che l'utente ha premuto il bottone Register nella form registerUser.html
	@RequestMapping(value = { "/users/register" }, method = RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("userForm") User user,
								BindingResult userBindingResult, //risultati del processo di validazione
								@Valid @ModelAttribute("credentialsForm") Credentials credentials,
								BindingResult credentialsBindingResult) {
		
		// validate user and credentials fields
		this.userValidator.validate(user, userBindingResult);
		this.credentialsValidator.validate(credentials, credentialsBindingResult);
		
		// if neither of them had invalid content, store the User and the Credentials into the DB
		if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
			credentials.setUser(user);
			credentialsService.saveCredentials(credentials);
			return "registrationSuccessful";	
		}
		return "registerUser";
	}
}
