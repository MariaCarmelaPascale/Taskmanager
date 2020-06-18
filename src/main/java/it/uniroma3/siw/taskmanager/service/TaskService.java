package it.uniroma3.siw.taskmanager.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.taskmanager.model.Comment;
import it.uniroma3.siw.taskmanager.model.Tag;
import it.uniroma3.siw.taskmanager.model.Task;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.repository.TaskRepository;

@Service
public class TaskService {

	@Autowired
	private TaskRepository taskRepository;


	// SALVARE/AGGIORNARE UN TASK DI UN MIO PROGETTO
	@Transactional
	public Task saveTask(Task t) {
		return this.taskRepository.save(t);
	}

	// CANCELLARE UN TASK DA UN MIO PROGETTO
	@Transactional
	public void deleteTask(Task t) {
		this.taskRepository.delete(t);
	}
	
	@Transactional
	public Task getTask(Long id) {
		return this.taskRepository.findById(id).orElse(null);
	}

	// ASSEGNARE UN TASK DI UN PROGETTO AD UN UTENTE CHE HA VISIBILITA' DEL PROGETTO
	@Transactional
	public void addTaskToUser(User u, Task t) {
		t.setAssignedUser(u);
		this.taskRepository.save(t);
	}

	// AGGIUNGERE UN TAG A UN TASK DI UN MIO PROGETTO
	@Transactional
	public void addTagToTask(Tag tag, Task task) {
		task.addTag(tag);
		this.taskRepository.save(task);
	}

	// AGGIUNGERE UN COMMENT A UN TASK DI UN MIO PROGETTO
	@Transactional
	public void addCommentToTask(Comment c, Task t) {
		t.addComment(c);
		this.taskRepository.save(t);
	}
	
	@Transactional
	public void deleteTagToTask(Tag tag, Task task) {
		List<Tag> tags = task.getTags();
		tags.remove(tag);
		this.taskRepository.save(task);
	}
}
