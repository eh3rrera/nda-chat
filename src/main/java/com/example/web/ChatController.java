package com.example.web;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.example.config.PusherSettings;
import com.example.constants.GeneralConstants;
import com.example.model.Chat;
import com.example.model.Message;
import com.example.model.User;
import com.example.service.ChatService;
import com.example.web.vo.ChatForm;

/**
 * Controller that manages the routes related to the chat
 */
@Controller
@SessionAttributes(GeneralConstants.ID_SESSION_CHAT_INFO)
public class ChatController {
	
	private Logger logger = LoggerFactory.getLogger(ChatController.class);
	
	@Autowired
	private ChatService chatService;
	
	@Autowired
	private PusherSettings pusherSettings;
	
	/**
	 * Route for the main page
	 * @return Object with the view information
	 */
	@RequestMapping(method=RequestMethod.GET, value="/")
    public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView();
		
		List<Chat> list = chatService.getAllActiveChats();
		logger.debug("" + list.size());
		
		modelAndView.setViewName("index");
		modelAndView.addObject("chat", new ChatForm()); 
		modelAndView.addObject("chats", list);
        
        return modelAndView;
    }
	
	/**
	 * Route to validate that a chat name is unique
	 * @param chatName The name to validate
	 * @return true if the name is valid (doesn't exist) or false otherwise
	 */
	@RequestMapping(method=RequestMethod.GET, value="/chat/validate/name", produces = "application/json")
	@ResponseBody
    public String validateChatName(@RequestParam String chatName) {
		Chat chat = chatService.getActiveChatByName(chatName);
        
        return String.valueOf(chat == null);
    }
	
	/**
	 * Route to create a chat
	 * @param form Object with the chat information
	 * @param model Object to store information in the session
	 * @return the view to redirect the request
	 */
	@RequestMapping(method=RequestMethod.POST, value="/chat/create")
	public String createChat(ChatForm form, Model model) {
		
		User user = new User();
        user.setName(form.getUserName());
        user.setActive(Boolean.TRUE);
        user.setCreatedAt(new Date());
        user.setEmail(form.getUserEmail());
        user.setNdaSigned(Boolean.FALSE);
        user.setOwnsChat(Boolean.TRUE);
        
        Chat chat = new Chat();
        chat.setActive(Boolean.TRUE);
        chat.setCreatedAt(new Date());
        chat.setDescription(form.getChatDescription());
        
        form.setChatName(form.getChatName().toLowerCase().replaceAll("\\s+", "-")); // Replace blank spaces with a hyphen
		form.setPresenceChatName(GeneralConstants.CHANNEL_PREFIX + form.getChatName());
        chat.setName(form.getChatName());
        
        chat.addMember(user);
        
        chatService.saveChat(chat);
        
        form.setIsUserChatOwner(Boolean.TRUE);
        form.setIdChat(chat.getId());
        form.setIdUser(user.getId());
        
        model.addAttribute(GeneralConstants.ID_SESSION_CHAT_INFO, form);
		
		return "redirect:/chat";
	}
	
	/**
	 * Route to join an existing chat
	 * @param form Object with the chat information
	 * @param model Object to store information in the session
	 * @return the view to redirect the request
	 */
	@RequestMapping(method=RequestMethod.POST, value="/chat/join")
	public String joinChat(ChatForm form, Model model) {
		User user = new User();
        user.setName(form.getUserName());
        user.setActive(Boolean.TRUE);
        user.setCreatedAt(new Date());
        user.setEmail(form.getUserEmail());
        user.setNdaSigned(Boolean.FALSE);
        user.setOwnsChat(Boolean.FALSE);
        
        Chat chat = chatService.addNewUserToChat(form.getIdChat(), user);
        
        form.setIsUserChatOwner(Boolean.FALSE);
        form.setIdUser(user.getId());
        form.setChatName(chat.getName());
        form.setChatDescription(chat.getDescription());
        form.setPresenceChatName(GeneralConstants.CHANNEL_PREFIX + form.getChatName());
        
        model.addAttribute(GeneralConstants.ID_SESSION_CHAT_INFO, form);
		
		return "redirect:/chat";
	}
	
	/**
	 * Route for the chat page
	 * @param chatInfo Session Object that contains information about the chat
	 * @return Object with the view information
	 */
	@RequestMapping(method=RequestMethod.GET, value="/chat")
    public ModelAndView chat(
    		@SessionAttribute(GeneralConstants.ID_SESSION_CHAT_INFO) ChatForm chatInfo) {
		ModelAndView modelAndView = new ModelAndView();
		List<Message> list = chatService.getAllChatMessages(chatInfo.getIdChat());
		
		modelAndView.setViewName("chat");
		modelAndView.addObject("messages", list);
		modelAndView.addObject("key", pusherSettings.getPusherKey());
        
        return modelAndView;
    }

}
