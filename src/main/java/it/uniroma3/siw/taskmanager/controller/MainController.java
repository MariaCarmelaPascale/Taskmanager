package it.uniroma3.siw.taskmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {

	public MainController() { }

	// ACCESSO ALLA PAGINA DI BENVENUTO
	@RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
	public String index(Model model) { // model è riferimento alla richiesta http
		return "index"; 
	}
}
