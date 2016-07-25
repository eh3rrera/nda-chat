package com.example.web.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Value Object that represents a webhook request from Pusher
 */
public class PusherWebhookRequest implements Serializable {

	/**
	 * UID for serialization
	 */
	private static final long serialVersionUID = -221474257382441989L;

	@JsonProperty("time_ms")
	private Long time;
	
	private List<Event> events;
	
	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Event implements Serializable {
		/**
		 * UID for serialization
		 */
		private static final long serialVersionUID = -741584257382441247L;

		private String name;
		
		private String channel;
		
		@JsonProperty("user_id")
		private Long userId;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getChannel() {
			return channel;
		}

		public void setChannel(String channel) {
			this.channel = channel;
		}

		public Long getUserId() {
			return userId;
		}

		public void setUserId(Long userId) {
			this.userId = userId;
		}
	}
}
