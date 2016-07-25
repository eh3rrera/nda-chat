package com.example.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Object that represents a User entity
 */
@Entity
public class User implements Serializable {

	/**
	 * UID for serialization
	 */
	private static final long serialVersionUID = 3868269731826822792L;

	/** ID of the user */
	@Id
	@Column(name="user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/** Email of the user */
	@Column(name="user_email", nullable = false)
	private String email;
	
	/** Name of the user */
	@Column(name="user_name", nullable = false)
	private String name;
	
	/** Indicates if the user is active */
	@Column(name="user_active", nullable = false)
	private Boolean active;
	
	/** Indicates if the user has signed the NDA */
	@Column(name="nda_signed", nullable = false)
	private Boolean ndaSigned;
	
	/** Indicates if the user is the owner of the chat */
	@Column(name="owns_chat", nullable = false)
	private Boolean ownsChat;
	
	/** ID of the HelloSign request to sign the NDA */
	@Column(name="sign_id", nullable = true)
	private String signId;
	
	/** Date and time of the creation of the user */
	@Column(name="created_at", nullable = false)
	private Date createdAt;
	
	/** Object that represents the chat the user belongs to */
	@ManyToOne
    @JoinColumn(name = "chat_id", 
                nullable = false, updatable = false)
	private Chat chat;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getNdaSigned() {
		return ndaSigned;
	}

	public void setNdaSigned(Boolean ndaSigned) {
		this.ndaSigned = ndaSigned;
	}

	public Boolean getOwnsChat() {
		return ownsChat;
	}

	public void setOwnsChat(Boolean ownsChat) {
		this.ownsChat = ownsChat;
	}

	public String getSignId() {
		return signId;
	}

	public void setSignId(String signId) {
		this.signId = signId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Chat getChat() {
		return chat;
	}

	public void setChat(Chat chat) {
		this.chat = chat;
	}

	@Override
	public String toString(){
		return new ToStringBuilder(this).
			       append("id", id).
			       append("email", email).
			       append("name", name).
			       append("active", active).
			       append("ndaSigned", ndaSigned).
			       append("ownsChat", ownsChat).
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
	   User that = (User) obj;
	   return new EqualsBuilder()
	                 .appendSuper(super.equals(obj))
	                 .append(id, that.id)
	                 .isEquals();
	  }
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(3, 11).
		   append(id).
		   toHashCode();
	}
}
