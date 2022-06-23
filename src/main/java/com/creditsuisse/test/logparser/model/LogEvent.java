package com.creditsuisse.test.logparser.model;

import javax.persistence.Entity;

@Entity
public class LogEvent extends Event {
	private String eventState;
	private long timestamp;

	public String getEventState() {
		return eventState;
	}

	public void setEventState(String eventState) {
		this.eventState = eventState;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
