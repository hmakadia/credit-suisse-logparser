package com.creditsuisse.test.logparser.model;

import javax.persistence.Entity;

@Entity
public class AlertEvent extends Event {
	private long eventDuration;
	private boolean alert;

	public long getEventDuration() {
		return eventDuration;
	}

	public void setEventDuration(long eventDuration) {
		this.eventDuration = eventDuration;
	}

	public boolean isAlert() {
		return alert;
	}

	public void setAlert(boolean alert) {
		this.alert = alert;
	}
}
