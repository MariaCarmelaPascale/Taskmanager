package it.uniroma3.siw.taskmanager.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.taskmanager.model.Tag;
import it.uniroma3.siw.taskmanager.repository.TagRepository;

@Service
public class TagService {

	@Autowired
	TagRepository tagRepsitory;
	
	@Transactional
	public Tag getTag(Long id) {
		return this.tagRepsitory.findById(id).orElse(null);
	}
}
