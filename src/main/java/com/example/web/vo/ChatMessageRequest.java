package com.example.web.vo;

import java.io.Serializable;

/**
 * Value Object that represents a request to register a chat message
 */
public class ChatMessageRequest implements Serializable {

	/**
	 * UID for serialization
	 */
	private static final long serialVersionUID = -2249158513686494489L;

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
