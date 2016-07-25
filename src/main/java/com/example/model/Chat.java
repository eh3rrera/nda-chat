package com.example.model;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Object that represents a Chat entity
 */
@Entity
public class Chat  implements Serializable {

	/**
	 * UID for serialization
	 */
	private static final long serialVersionUID = 1598739731826887591L;

	/** ID of the chat */
	@Id
	@Column(name="chat_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/** Name of the chat */
	@Column(name="chat_name", nullable = false)
	private String name;
	
	/** Description of the chat */
	@Column(name="chat_description", nullable = false)
	private String description;
	
	/** Indicates if the chat is active */
	@Column(name="chat_active", nullable = false)
	private Boolean active;
	
	/** The date and time when the chat was created */
	@Column(name="created_at", nullable = false)
	private Date createdAt;
	
	/** Members of the chat */
	@OneToMany(fetch = FetchType.LAZY, 
            cascade = CascadeType.ALL, 
            mappedBy = "chat", 
            orphanRemoval = true)
	private Set<User> members = new LinkedHashSet<User>();
	
	/**
	 * Helper method to add a member
	 * @param user Member to add to the chat
	 */
	public void addMember(User user) {
        this.members.add(user);
        user.setChat(this);
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Set<User> getMembers() {
		return members;
	}

	public void setMembers(Set<User> members) {
		this.members = members;
	}

	@Override
	public String toString(){
		return new ToStringBuilder(this).
			       append("id", id).
			       append("name", name).
			       append("description", description).
			       append("active", active).
			       append("createdAt", createdAt).
			       toString();
	}
	
	@Override
	public boolean equals(Object obj) {
	   if (obj == null) { return false; }
	   if (obj == this) { return true; }
	   if (obj.getClass() != getClass()) {
	     return false;
	   }
	   Chat that = (Chat) obj;
	   return new EqualsBuilder()
	                 .appendSuper(super.equals(obj))
	                 .append(id, that.id)
	                 .isEquals();
	  }
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(5, 13).
		   append(id).
		   toHashCode();
	}
}
