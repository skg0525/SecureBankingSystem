package edu.softwaresecurity.group5.model;

import java.util.Date;

public class AccountAttempts {
	private int id;
	private String username;
	private int tries;
	private Date lastAttempt;
	private int isLocked;
	public int isLocked() {
		return isLocked;
	}
	public void setLocked(int isLocked) {
		this.isLocked = isLocked;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getTries() {
		return tries;
	}
	public void setTries(int tries) {
		this.tries = tries;
	}
	public Date getLastAttempt() {
		return lastAttempt;
	}
	public void setLastAttempt(Date lastAttempt) {
		this.lastAttempt = lastAttempt;
	}
	
	
	
}
