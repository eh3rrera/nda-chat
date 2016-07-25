package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.model.Chat;
import com.example.model.User;
import com.example.repository.ChatRepository;
import com.example.repository.UserRepository;

/**
 * Service with the operations to work with a user
 */
@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ChatRepository chatRepository;
	
	/**
	 * Marks a user as inactive
	 * @param idUser ID of the user
	 */
	public void markUserAsInactive(Long idUser) {
		User user = userRepository.findOne(idUser);
		
		if(user != null) {
			user.setActive(Boolean.FALSE);
			userRepository.save(user);
		}
	}

	/**
	 * Find all the active members of a chat that haven't sign the NDA
	 * @param idChat ID of the chat
	 * @return a List of User objects
	 */
	public List<User> getChatMembersToSignNda(Long idChat) {
		Chat chat = chatRepository.findOne(idChat);
		
		return userRepository.findByChatAndNdaSignedFalseAndActiveTrueAndOwnsChatFalse(chat);
	}
	
	/**
	 * Get a user by the HelloSign ID of her signature request
	 * @param signId ID of the signature request
	 * @return a User object
	 */
	public User getUserBySignId(String signId) {
		List<User> users = userRepository.findBySignId(signId);
		User user = null;
		
		if(users != null && !users.isEmpty()) {
			user = users.get(0);
		}
		
		return user;
	}
	
	/**
	 * Saves(creates or modify) a user object
	 * @param user The object to save
	 */
	public void saveUser(User user) {
		userRepository.save(user);
	}
}
