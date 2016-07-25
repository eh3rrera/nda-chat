/*
 * H2 script.
 * Create the database schema for the application.
 */
CREATE TABLE chat(
	chat_id 			BIGINT IDENTITY PRIMARY KEY,
	chat_name 			VARCHAR(50) NOT NULL,
	chat_description 	VARCHAR(150) NOT NULL,
	chat_active			BOOLEAN NOT NULL,
	created_at			TIMESTAMP NOT NULL
);

CREATE TABLE user(
	user_id 			BIGINT IDENTITY PRIMARY KEY,
	user_email 			VARCHAR(50) NOT NULL,
	user_name			VARCHAR(50) NOT NULL,
	user_active			BOOLEAN NOT NULL,
	nda_signed			BOOLEAN NOT NULL,
	owns_chat			BOOLEAN NOT NULL,
	chat_id				BIGINT NOT NULL,
	sign_id 			VARCHAR(255),
	created_at			TIMESTAMP NOT NULL,
	CONSTRAINT FK_user_chat FOREIGN KEY (chat_id) REFERENCES chat(chat_id)
);

CREATE TABLE message(
	message_id			BIGINT IDENTITY PRIMARY KEY,
	chat_id				BIGINT NOT NULL,
	user_id				BIGINT NOT NULL,
	message_text	 	VARCHAR(1000) NOT NULL,
	created_at			TIMESTAMP NOT NULL,
	CONSTRAINT FK_message_chat FOREIGN KEY (chat_id) REFERENCES chat(chat_id),
	CONSTRAINT FK_message_user FOREIGN KEY (user_id) REFERENCES user(user_id)
);