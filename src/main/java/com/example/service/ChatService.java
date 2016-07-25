package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.model.Chat;
import com.example.model.Message;
import com.example.model.User;
import com.example.repository.ChatRepository;
import com.example.repository.MessageRepository;
import com.example.repository.UserRepository;

/**
 * Service with the operations to work with a chat
 */
@Service
@Transactional
public class ChatService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ChatRepository chatRepository;

	@Autowired
	private MessageRepository messageRepository;
	
	/**
	 * Saves(creates or modify) a chat object
	 * @param chat The object to save
	 */
	public void saveChat(Chat chat) {
		chatRepository.save(chat);
	}
	
	/**
	 * Add a new user to a chat
	 * @param id ID of the chat
	 * @param user Object that represents the user to add
	 * @return the Chat object with the new user added
	 */
	public Chat addNewUserToChat(Long id, User user) {
		Chat chat = getChat(id);
		chat.addMember(user);
		
		userRepository.save(user);
		
		return chat;
	}
	
	/**
	 * Saves a message and its relation to a user
	 * @param msg The message to save
	 * @param idUser The ID of the user that post the message
	 */ 
	public void saveMessage(Message msg, Long idUser) {
		User user = userRepository.findOne(idUser);
		msg.setUser(user);
		
		messageRepository.save(msg);
	}
	
	/**
	 * Marks an active chat as inactive
	 * @param chatName The name of the chat to mark
	 */
	public void markChatAsInactive(String chatName) {
		List<Chat> chats = chatRepository.findByNameAndActiveTrue(chatName);
		
		if(chats != null && !chats.isEmpty()) {
			Chat chat = chats.get(0);
			chat.setActive(Boolean.FALSE);
			chatRepository.save(chat);
		}
	}
	
	/**
	 * Finds a chat by its ID
	 * @param id ID of the chat to find
	 * @return a chat object
	 */
	public Chat getChat(Long id) {
		return chatRepository.findOne(id);
	}
	
	/**
	 * Get all the active chats
	 * @return a List of chat objects
	 */
	public List<Chat> getAllActiveChats() {
		return chatRepository.findByActiveTrueOrderById();
	}
	
	/**
	 * Find an active chat by name
	 * @param chatName Name of the chat
	 * @return a chat object
	 */
	public Chat getActiveChatByName(String chatName) {
		List<Chat> chats = chatRepository.findByNameAndActiveTrue(chatName);
		Chat chat = null;
		
		if(chats != null && !chats.isEmpty()) {
			chat = chats.get(0);
		}
		
		return chat;
	}
	
	/**
	 * Gets all the messages of a chat
	 * @param idChat ID of the chat
	 * @return a List of message objects
	 */
	public List<Message> getAllChatMessages(Long idChat) {
		return messageRepository.findByIdChatOrderByCreatedAt(idChat);
	}
}
