package it.uniroma3.siw.taskmanager.model;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;

@Entity
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false, length = 200)
	private String name;
	
	@Column(nullable = false, length = 200)
	private String description;
	
	@Column(nullable = false, updatable = false)
	private LocalDateTime creationTimeStamp;
	
	@ManyToMany(fetch = FetchType.LAZY)
	private List<Tag> tags;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "task_id")
	private List<Comment> comments;
	
	@OneToOne
	private User assignedUser;
	
	public Task() { }
	
	public Task(String name, String description) {
		this.name = name;
		this.description = description;
	}

	@PrePersist
	protected void onPersist() {
		this.creationTimeStamp = LocalDateTime.now();
	}
	
	public void addTag(Tag tag) {
		this.tags.add(tag);
	}
	
	public void addComment(Comment c) {
		this.comments.add(c);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;

	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getCreationTimeStamp() {
		return creationTimeStamp;
	}

	public void setCreationTimeStamp(LocalDateTime creationTimeStamp) {
		this.creationTimeStamp = creationTimeStamp;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	
	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	

	public User getAssignedUser() {
		return assignedUser;
	}

	public void setAssignedUser(User assignedUser) {
		this.assignedUser = assignedUser;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Task [name=" + name + ", description=" + description + ", creationTimeStamp=" + creationTimeStamp
				+ ", tags=" + tags + ", comments=" + comments + ", assignedUser=" + assignedUser + "]";
	}
	
}
