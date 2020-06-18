package it.uniroma3.siw.taskmanager.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import it.uniroma3.siw.taskmanager.model.Tag;

@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {

}
