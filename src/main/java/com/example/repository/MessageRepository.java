package com.example.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Message;

/**
 * Repository that contains operations to work with a Message entities
 */
@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {

	/**
	 * Finds the messages of a chat
	 * @param idChat ID of the chat
	 * @return a List of Message objects that meet the search criteria
	 */
	List<Message> findByIdChatOrderByCreatedAt(Long idChat);
}
