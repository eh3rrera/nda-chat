package com.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.pusher.rest.Pusher;

/**
 * Class that contains the information to use the Pusher API
 */
@Component
public class PusherSettings {
	
	/** Pusher App ID */
	@Value("${pusher.appId}")
    private String appId;
	
	/** Pusher Key */
	@Value("${pusher.key}")
    private String key;
	
	/** Pusher Secret */
	@Value("${pusher.secret}")
    private String secret;
	
	/**
	 * Creates a new instance of the Pusher object to use its API
	 * 
	 * @return An instance of the Pusher object
	 */
	public Pusher newInstance() {
		return new Pusher(appId, key, secret);
	}
	
	public String getPusherKey() {
		return key;
	}

}
