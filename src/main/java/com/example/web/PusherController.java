package com.example.web;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.example.config.PusherSettings;
import com.example.constants.GeneralConstants;
import com.example.model.Message;
import com.example.service.ChatService;
import com.example.service.UserService;
import com.example.web.vo.ChatForm;
import com.example.web.vo.ChatMessageRequest;
import com.example.web.vo.ChatMessageResponse;
import com.example.web.vo.PusherWebhookRequest;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pusher.rest.Pusher;
import com.pusher.rest.data.PresenceUser;
import com.pusher.rest.data.Validity;

/**
 * Controller for the REST API related to the functionality provided by Pusher
 */
@RestController
public class PusherController {
	
	private Logger logger = LoggerFactory.getLogger(PusherController.class);
	
	@Autowired
	private ChatService chatService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PusherSettings pusherSettings;
	
	private Pusher pusher;
	
	/**
	 * Method executed after the object is created
	 * that creates an instance of the Pusher object
	 */
	@PostConstruct
	public void createPusherObject() {
		pusher = pusherSettings.newInstance();
	}

	/**
	 * Endpoint that authenticates a subscription to a Pusher presence channel
	 * @param socketId ID of the opened socket
	 * @param channel Channel to subscribe
	 * @param chatInfo Session object with information about the chat 
	 * @return A status string
	 */
	@RequestMapping(method = RequestMethod.POST, value= "/chat/auth")
	public String auth(
			@RequestParam(value="socket_id") String socketId, 
	        @RequestParam(value="channel_name") String channel,
	        @SessionAttribute(GeneralConstants.ID_SESSION_CHAT_INFO) ChatForm chatInfo){
		
		Long userId = chatInfo.getIdUser();
		Map<String, String> userInfo = new HashMap<>();
		userInfo.put("name", chatInfo.getUserName());
		userInfo.put("email", chatInfo.getUserEmail());

		String res = pusher.authenticate(socketId, channel, new PresenceUser(userId, userInfo));

	    return res;
	}
	
	/**
	 * Endpoint to register a chat message
	 * @param request Object with information about the message
	 * @param chatInfo Session object with information about the chat
	 * @return An object with information about the message
	 */
	@RequestMapping(value = "/chat/message", 
					method = RequestMethod.POST, 
					consumes = "application/json", 
					produces = "application/json")
	public ChatMessageResponse messsage(
			@RequestBody ChatMessageRequest request,
			@SessionAttribute(GeneralConstants.ID_SESSION_CHAT_INFO) ChatForm chatInfo) {
		
		Message msg = new Message();
		msg.setCreatedAt(new Date());
		msg.setIdChat(chatInfo.getIdChat());
		msg.setMessage(request.getMessage());
		
		chatService.saveMessage(msg, chatInfo.getIdUser());
		
		ChatMessageResponse response = new ChatMessageResponse();
		response.setMessage(msg.getMessage());
		response.setTime(msg.getCreatedAtString());
		response.setUserId(msg.getUser().getId());
		response.setUserName(msg.getUser().getName());
		
		pusher.trigger(chatInfo.getPresenceChatName(), "new_message", response);
		
		return response;
	}
	
	/**
	 * Endpoint called from Pusher when an event happens
	 * @param key Pusher key
	 * @param signature Signature of the request
	 * @param json JSON with the information about the event
	 * @return A status string
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@RequestMapping(value = "/pusher/webhook", 
			method = RequestMethod.POST, 
			consumes = "application/json")
	public String webhook(
			@RequestHeader(value="X-Pusher-Key") String key,
			@RequestHeader(value="X-Pusher-Signature") String signature,
			@RequestBody String json) throws JsonParseException, JsonMappingException, IOException {
		Validity valid = pusher.validateWebhookSignature(key, signature, json);
		
		if(Validity.VALID.equals(valid)) {
			ObjectMapper mapper = new ObjectMapper();
			PusherWebhookRequest request = mapper.readValue(json, PusherWebhookRequest.class);
			
			if(request.getEvents() != null) {
				for(PusherWebhookRequest.Event event : request.getEvents()) {
					switch(event.getName()) {
						case "channel_occupied":
							logger.info("channel_occupied: " + event.getChannel());
							break;
						case "channel_vacated":
							logger.info("channel_vacated: " + event.getChannel());
							chatService.markChatAsInactive(event.getChannel().replace(GeneralConstants.CHANNEL_PREFIX, ""));
							break;
						case "member_added":
							logger.info("member_added: " + event.getUserId());
							break;
						case "member_removed":
							logger.info("member_removed: " + event.getUserId());
							userService.markUserAsInactive(event.getUserId());
							break;
					}
				}
			}
		}
		
		return "OK";
	}
}
