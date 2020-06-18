package it.uniroma3.siw.taskmanager.controller;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.taskmanager.model.Comment;

@Component
public class CommentValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Comment.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		final Integer MIN_LENGTH = 2;
		final Integer MAX_LENGTH = 500;
		
		Comment comment = (Comment) target;
		String note = comment.getComment();
		if(note.isEmpty())
			errors.rejectValue("commentForm", "required");
		else if(note.length()<MIN_LENGTH || note.length()>MAX_LENGTH)
			errors.rejectValue("commentForm", "size");
	}

}
