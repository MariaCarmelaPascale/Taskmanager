package it.uniroma3.siw.taskmanager.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.User;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {

	public List<Project> findByMembers(User user);

	public List<Project> findByOwner(User user);

}
