package com.creditsuisse.test.logparser.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public abstract class Event {
	@Id
	private String id;
	private String type;
	private String host;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
}
