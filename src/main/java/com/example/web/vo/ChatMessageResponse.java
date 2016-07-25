package com.example.web.vo;

import java.io.Serializable;

/**
 * Value Object that contains the information of a chat message
 */
public class ChatMessageResponse implements Serializable {
	
	/**
	 * UID for serialization
	 */
	private static final long serialVersionUID = -2974835383997850724L;
	
	private Long userId;

	private String userName;
	
	private String message;
	
	private String time;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
