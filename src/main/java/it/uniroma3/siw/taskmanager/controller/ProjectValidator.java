package it.uniroma3.siw.taskmanager.controller;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.taskmanager.model.Project;

import org.springframework.stereotype.Component;

@Component
public class ProjectValidator implements Validator{
	
	final Integer MIN_LENGHT = 4;
	final Integer MAX_LENGTH = 20;

	@Override
	public boolean supports(Class<?> clazz) {
		return Project.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		Project project = (Project) target;
		String name = project.getName();
		
		if(name.isEmpty()) 
			errors.rejectValue("name", "required");
		else if(name.length()< MIN_LENGHT || name.length()>MAX_LENGTH)
			errors.rejectValue("name", "size");
	}

}
