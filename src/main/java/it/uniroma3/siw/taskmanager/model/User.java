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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name = "Users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false, length=200)
	private String firstName;
	
	@Column(nullable = false, length=200)
	private String lastName;
	
	@Column(updatable = false, nullable = false)
	private LocalDateTime creationTimeStamp;

	@OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<Project> ownedProjects;
	
	@ManyToMany(mappedBy = "members", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<Project> visibleProjects;
	
	public User() { 
		this.ownedProjects = new ArrayList<>();
		this.visibleProjects = new ArrayList<>();
	}
	
	public User(String firstName, String lastName) {
		this();
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	@PrePersist
	public void onPersist() {
		this.creationTimeStamp = LocalDateTime.now();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDateTime getCreationTimeStamp() {
		return creationTimeStamp;
	}

	public void setCreationTimeStamp(LocalDateTime creationTimeStamp) {
		this.creationTimeStamp = creationTimeStamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creationTimeStamp == null) ? 0 : creationTimeStamp.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
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
		User other = (User) obj;
		if (creationTimeStamp == null) {
			if (other.creationTimeStamp != null)
				return false;
		} else if (!creationTimeStamp.equals(other.creationTimeStamp))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [firstName=" + firstName + ", lastName=" + lastName + ", creationTimeStamp=" + creationTimeStamp
				+ ", ownedProjects=" + ownedProjects + ", visibleProject=" + visibleProjects + "]";
	}
}
