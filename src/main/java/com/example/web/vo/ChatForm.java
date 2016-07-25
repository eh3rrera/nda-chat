package com.example.web.vo;

import java.io.Serializable;

/**
 * Value Object that represents the form to create or join a chat
 */
public class ChatForm implements Serializable {
	
	/**
	 * UID for serialization
	 */
	private static final long serialVersionUID = -1007310999180241229L;
	
	private Long idChat;
	
	private Long idUser;
	
	private String chatName;
	
	private String presenceChatName;
	
	private String chatDescription;
	
	private String userEmail;
	
	private String userName;
	
	private Boolean isUserChatOwner;

	public Long getIdChat() {
		return idChat;
	}

	public void setIdChat(Long idChat) {
		this.idChat = idChat;
	}

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public String getChatName() {
		return chatName;
	}

	public void setChatName(String chatName) {
		this.chatName = chatName;
	}

	public String getPresenceChatName() {
		return presenceChatName;
	}

	public void setPresenceChatName(String presenceChatName) {
		this.presenceChatName = presenceChatName;
	}

	public String getChatDescription() {
		return chatDescription;
	}

	public void setChatDescription(String chatDescription) {
		this.chatDescription = chatDescription;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Boolean getIsUserChatOwner() {
		return isUserChatOwner;
	}

	public void setIsUserChatOwner(Boolean isUserChatOwner) {
		this.isUserChatOwner = isUserChatOwner;
	}
}
