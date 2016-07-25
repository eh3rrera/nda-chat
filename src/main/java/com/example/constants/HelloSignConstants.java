package com.example.constants;

/**
 * Constants used for the HelloSign API
 */
public interface HelloSignConstants {

	/** Subject for the email sent by HelloSign to request a sign */
	String EMAIL_SUBJECT = "The NDA for the chat ";
	
	/** ID of the signing role */
	String SIGNING_ROLE = "Consultant";
	
	/** ID of the template custom field */
	String NAME_TEMPLATE_FIELD = "name";
	
	/** Error message when the file of the signed document doesn't exist */
	String FILE_DOWNLOAD_ERROR_MSG = "Sorry. The file does not exist.";
	
	/** Content type of the file */
	String FILE_CONTENT_TYPE = "application/pdf";
	
	/** ID for the request signed event */
	String REQUEST_SIGNED_EVENT = "signature_request_signed";
	
	/** ID for the request sent event */
	String REQUEST_SENT_EVENT = "signature_request_sent";
	
	/** Response to the webhook request required by HelloSign */
	String WEBHOOK_RESPONSE = "Hello API Event Received";
}
