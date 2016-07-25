package com.example.web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.example.config.PusherSettings;
import com.example.constants.GeneralConstants;
import com.example.constants.HelloSignConstants;
import com.example.model.User;
import com.example.service.UserService;
import com.example.web.vo.ChatForm;
import com.example.web.vo.ChatMessageResponse;
import com.hellosign.sdk.HelloSignClient;
import com.hellosign.sdk.HelloSignException;
import com.hellosign.sdk.resource.Event;
import com.hellosign.sdk.resource.SignatureRequest;
import com.hellosign.sdk.resource.TemplateSignatureRequest;
import com.pusher.rest.Pusher;

/**
 * Controller for the REST API related to the functionality provided by HelloSign
 */
@RestController
public class HelloSignController {
	
	private Logger logger = LoggerFactory.getLogger(HelloSignController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PusherSettings pusherSettings;
	
	@Value("${hellosign.apikey}")
	private String helloSignApiKey;
	
	@Value("${hellosign.templateId}")
	private String helloSignTemplateId;
	
	@Value("${hellosign.testMode}")
	private Boolean testMode;
	
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
	 * Endpoint that request a signature to HelloSign
	 * @param chatInfo Object with the chat information
	 * @return A status string
	 * @throws HelloSignException
	 */
	@RequestMapping(value = "/chat/request/nda", 
			method = RequestMethod.POST, 
			produces = "application/json")
	public String requestNda(
		@SessionAttribute(GeneralConstants.ID_SESSION_CHAT_INFO) ChatForm chatInfo) 
				throws HelloSignException {
	
		List<User> users = userService.getChatMembersToSignNda(chatInfo.getIdChat());
		
		if(users != null && !users.isEmpty()) {
			HelloSignClient client = new HelloSignClient(helloSignApiKey);
			
			for(User user : users) {
				TemplateSignatureRequest request = new TemplateSignatureRequest();
				request.setSubject(HelloSignConstants.EMAIL_SUBJECT + chatInfo.getChatName());
				request.setSigner(HelloSignConstants.SIGNING_ROLE, user.getEmail(), user.getName());
				request.setCustomFieldValue(HelloSignConstants.NAME_TEMPLATE_FIELD, user.getName());
				request.setTemplateId(helloSignTemplateId);
				request.setTestMode(testMode);
				
				SignatureRequest newRequest = client.sendTemplateSignatureRequest(request);
				
				user.setSignId(newRequest.getId());
				userService.saveUser(user);
			}
			
		}
		
		return "OK";
	}
	
	/**
	 * Endpoint called from HelloSign when an event happens
	 * @param json JSON with the information about the event
	 * @return A status string
	 * @throws HelloSignException
	 */
	@RequestMapping(value = "/hellosign/webhook", 
			method = RequestMethod.POST)
	public String webhook(@RequestParam String json) throws HelloSignException {
		JSONObject jsonObject = new JSONObject(json);
		Event event = new Event(jsonObject);
		
		boolean validRequest = event.isValid(helloSignApiKey);
		
		if(validRequest) {
			SignatureRequest signatureRequest = event.getSignatureRequest();
			User user = null;
			ChatMessageResponse response = new ChatMessageResponse();
			
			logger.info(event.getTypeString());
			switch(event.getTypeString()) {
				case HelloSignConstants.REQUEST_SIGNED_EVENT:
					user = userService.getUserBySignId(signatureRequest.getId());
					if(user != null) {
						response.setMessage(user.getName() + " has signed the NDA agreement. Download the file <a href=\"/download/" + signatureRequest.getId() + "\" target=\"_blank\">here</a>");
						pusher.trigger(GeneralConstants.CHANNEL_PREFIX + user.getChat().getName(), "system_message", response);
					}
					break;
				case HelloSignConstants.REQUEST_SENT_EVENT:
					user = userService.getUserBySignId(signatureRequest.getId());
					if(user != null) {
						response.setMessage("The signature request has been sent to " + user.getName());
						pusher.trigger(GeneralConstants.CHANNEL_PREFIX + user.getChat().getName(), "system_message", response);
					}
					break;
			}
		}
		
		return HelloSignConstants.WEBHOOK_RESPONSE;
	}
	
	/**
	 * Endpoint to download the signed NDA
	 * @param response Object to send a response
	 * @param id ID of the request signature
	 * @throws IOException
	 * @throws HelloSignException
	 */
	@RequestMapping(value="/download/{id}", method = RequestMethod.GET)
    public void downloadFile(HttpServletResponse response, @PathVariable("id") String id) throws IOException, HelloSignException {
		HelloSignClient client = new HelloSignClient(helloSignApiKey);
        File file = client.getFiles(id);
         
        if(!file.exists()){
            String errorMessage = HelloSignConstants.FILE_DOWNLOAD_ERROR_MSG;
            System.out.println(errorMessage);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            outputStream.close();
            
            return;
        }
         
        response.setContentType(HelloSignConstants.FILE_CONTENT_TYPE);
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() +"\""));
        response.setContentLength((int)file.length());
 
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }

}
