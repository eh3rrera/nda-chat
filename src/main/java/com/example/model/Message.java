package com.example.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Object that represents a Message entity
 */
@Entity
public class Message implements Serializable {

	/**
	 * UID for serialization
	 */
	private static final long serialVersionUID = 7024644638730524732L;

	/** ID of the message */
	@Id
	@Column(name="message_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/** ID of the chat that the message belongs to */
	@Column(name="chat_id", nullable = false)
	private Long idChat;
	
	/** Text of the message */
	@Column(name="message_text", nullable = false)
	private String message;
	
	/** Date and time when the message was created */
	@Column(name="created_at", nullable = false)
	private Date createdAt;
	
	/** Object that represents the user that post the message */
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", 
                nullable = false, updatable = false)
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdChat() {
		return idChat;
	}

	public void setIdChat(Long idChat) {
		this.idChat = idChat;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String getCreatedAtString() {
		String time = "";
		if(this.createdAt != null) {
			DateFormat df = new SimpleDateFormat("hh:mm a");
			time = df.format(this.createdAt);
		}
		
		return time;
	}
	
	public String getMessageFormatted() {
		String msg = "";
		
		if(this.message != null) {
			msg = this.message.replaceAll("(\r?\n)", "<br />");
		}
		
		return msg;
	}

	@Override
	public String toString(){
		return new ToStringBuilder(this).
			       append("id", id).
			       append("idChat", idChat).
			       append("message", message).
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
	   Message that = (Message) obj;
	   return new EqualsBuilder()
	                 .appendSuper(super.equals(obj))
	                 .append(id, that.id)
	                 .isEquals();
	  }
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(7, 15).
		   append(id).
		   toHashCode();
	}
}
