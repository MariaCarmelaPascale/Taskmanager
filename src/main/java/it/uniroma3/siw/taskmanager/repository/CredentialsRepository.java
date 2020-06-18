package it.uniroma3.siw.taskmanager.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.taskmanager.model.Credentials;

public interface CredentialsRepository extends CrudRepository<Credentials, Long> {

	public Optional<Credentials> findByUserName(String username);
}
