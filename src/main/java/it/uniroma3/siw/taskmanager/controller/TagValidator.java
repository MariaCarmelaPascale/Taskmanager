package it.uniroma3.siw.taskmanager.controller;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.taskmanager.model.Tag;

@Component
public class TagValidator implements Validator{
	
	final Integer MIN_NAME_LENGTH = 2;
	final Integer MAX_NAME_LENGTH = 15;
	final Integer MIN_COLOR_LENGTH = 2;
	final Integer MAX_COLOR_LENGTH = 20;
	final Integer MIN_DESC_LENGTH = 4;
	final Integer MAX_DESC_LENGTH = 200;

	@Override
	public boolean supports(Class<?> clazz) {
		return Tag.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		Tag tag = (Tag) target;
		String name = tag.getName();
		String color = tag.getColor();
		String description = tag.getDescription();
		
		if(name.isEmpty())
			errors.rejectValue("name", "requirded");
		else if(name.length()<MIN_NAME_LENGTH || name.length()>MAX_NAME_LENGTH)
			errors.rejectValue("name", "size");
		
		if(color.isEmpty())
			errors.rejectValue("color", "requirded");
		else if(color.length()<MIN_COLOR_LENGTH || color.length()>MAX_COLOR_LENGTH)
			errors.rejectValue("color", "size");

		if(description.length()<MIN_DESC_LENGTH || description.length()>MAX_DESC_LENGTH)
			errors.rejectValue("description", "size");
	}

}
