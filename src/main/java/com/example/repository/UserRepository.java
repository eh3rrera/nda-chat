package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Chat;
import com.example.model.User;

/**
 * Repository that contains operations to work with User entities
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	/**
	 * Finds active members of a chat who haven't sign the NDA and are not the chat owner
	 * @param chat the Chat object that the users belongs to
	 * @return A List of User objects that represent the members of the chat
	 */
	List<User> findByChatAndNdaSignedFalseAndActiveTrueAndOwnsChatFalse(Chat chat);
	
	/**
	 * Finds users by their sign request ID
	 * @param signId HelloSign sign request ID
	 * @return A List of User objects that meet the search criteria
	 */
	@EntityGraph(attributePaths = { "chat" })
	List<User> findBySignId(String signId);
}
