package it.uniroma3.siw.taskmanager.controller;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.taskmanager.model.Task;

@Component
public class TaskValidator implements Validator {
	
	final Integer MIN_NAME_LENGTH = 2;
	final Integer MAX_NAME_LENGTH = 20;
	
	final Integer MIN_DESC_LENGTH = 4;
	final Integer MAX_DESC_LENGTH = 200;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Task.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Task task = (Task) target;
		String name = task.getName();
		String description = task.getDescription();
		
		if(name.isEmpty())
			errors.rejectValue("name", "required");
		else if(name.length()<MIN_NAME_LENGTH || name.length()>MAX_NAME_LENGTH)
			errors.rejectValue("name", "size");
		
		if(description.isEmpty())
			errors.rejectValue("description", "required");
		else if(description.length()<MIN_DESC_LENGTH || description.length()>MAX_DESC_LENGTH)
			errors.rejectValue("description", "size");		
	}
}
