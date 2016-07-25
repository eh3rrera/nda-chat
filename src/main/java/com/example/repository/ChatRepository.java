package com.example.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Chat;

/**
 * Repository that contains operations to work with a Chat entities
 */
@Repository
public interface ChatRepository extends CrudRepository<Chat, Long> {

	/**
	 * Finds all active chats
	 * @return a List of Chat objects ordered by their ID
	 */
	List<Chat> findByActiveTrueOrderById();

	/**
	 * Finds an active chat by its name
	 * @param chatName Name of the chat
	 * @return List of Chat objects that meet the search criteria
	 */
	List<Chat> findByNameAndActiveTrue(String chatName);
}
