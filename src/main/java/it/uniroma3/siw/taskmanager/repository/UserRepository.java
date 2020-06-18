package it.uniroma3.siw.taskmanager.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	public List<User> findByVisibleProjects(Project project);
}
