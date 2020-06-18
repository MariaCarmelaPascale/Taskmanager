package it.uniroma3.siw.taskmanager.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.taskmanager.controller.session.SessionData;
import it.uniroma3.siw.taskmanager.model.Credentials;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.service.UserService;

@Controller
public class UserController {

	@Autowired
	SessionData sessionData;
	
	@Autowired
	UserValidator userValidator;
	
	@Autowired
	UserService userService;
	
	@RequestMapping(value = { "/home" }, method = RequestMethod.GET)
	public String home(Model model) {
		User loggedUser = this.sessionData.getLoggedUser();
		model.addAttribute("user", loggedUser);
		return "home";  //restituisce la vista
	}
	
	@RequestMapping(value = {"/users/me"}, method = RequestMethod.GET)
	public String me(Model model) {
		//prendo l'utente loggato dalla sessione
		User loggedUser = this.sessionData.getLoggedUser();
		//prendo le credenziali dell'utente loggato
		Credentials credentials= this.sessionData.getLoggedCredentials();
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("credentials", credentials);
		return "userProfile";
	}
	
	@RequestMapping(value = {"/users/editProfile"}, method = RequestMethod.GET)
	public String formEdit(Model model) {
		User user = new User();
		model.addAttribute("userForm", user);
		return "editProfileUser";
	}
	
	@RequestMapping(value = {"/users/editProfile"}, method = RequestMethod.POST)
	public String editProfile(@Valid @ModelAttribute("userForm") User user, BindingResult userBindingResult) {
		
		this.userValidator.validate(user, userBindingResult);
		
		if(!userBindingResult.hasErrors()) {
			User loggedUser = this.sessionData.getLoggedUser();
			loggedUser.setFirstName(user.getFirstName());
			loggedUser.setLastName(user.getLastName());
			this.userService.saveUser(loggedUser);
			return "updateProfileSuccessful";
		}
		return "editProfileUser";
	}
}
