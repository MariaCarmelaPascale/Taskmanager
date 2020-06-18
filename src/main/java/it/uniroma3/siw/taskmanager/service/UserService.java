package it.uniroma3.siw.taskmanager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	protected UserRepository userRepository;
	
	// RICERCA NEL DB
	@Transactional
	public User getUser(Long id) {
		Optional<User> user = this.userRepository.findById(id);
		return user.orElse(null);
	}
	
	@Transactional
	public List<User> getAllUsers(Long id) {
		List<User> users = new ArrayList<>();
		Iterable<User> i = this.userRepository.findAll();
		for(User u: i) 
			users.add(u);
		return users;
	}
	
	// SALVARE UNO USER NEL DB
	@Transactional
	public User saveUser(User user) {
		return this.userRepository.save(user);
	}
	
	@Transactional
	public List<User> getMembers(Project project){
		return this.userRepository.findByVisibleProjects(project);
	}
}
