package it.uniroma3.siw.taskmanager.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

@Entity
public class Project {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false, length=200)
	private String name;
	
	@Column(updatable = false, nullable = false)
	private LocalDateTime creationTimeStamp;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private User owner;
	
	@ManyToMany(fetch = FetchType.LAZY)
	private List<User> members;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "project_id")
	private List<Task> tasks;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "project_id")
	private List<Tag> tags;
	
	public Project() { 
		this.members = new ArrayList<>();
		this.tasks = new ArrayList<>();
		this.tags = new ArrayList<>();
	}
	
	public Project(String name) {
		this();
		this.name = name;
	}
	
	@PrePersist
	protected void onPersist() {
		this.creationTimeStamp = LocalDateTime.now();
	}
	
	public void addTag(Tag t) {
		this.tags.add(t);
	}
	
	public void addMember(User u) {
		this.members.add(u);
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

	public LocalDateTime getCreationTimeStamp() {
		return creationTimeStamp;
	}

	public void setCreationTimeStamp(LocalDateTime creationTimeStamp) {
		this.creationTimeStamp = creationTimeStamp;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public List<User> getMembers() {
		return members;
	}

	public void setMembers(List<User> members) {
		this.members = members;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	
	public void addTask(Task task) {
		this.tasks.add(task);
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creationTimeStamp == null) ? 0 : creationTimeStamp.hashCode());
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
		Project other = (Project) obj;
		if (creationTimeStamp == null) {
			if (other.creationTimeStamp != null)
				return false;
		} else if (!creationTimeStamp.equals(other.creationTimeStamp))
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
		return "Project [name=" + name + ", creationTimeStamp=" + creationTimeStamp + ", owner=" + owner + ", members="
				+ members + ", tasks=" + tasks + ", tags=" + tags + "]";
	}
}
