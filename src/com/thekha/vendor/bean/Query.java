package com.thekha.vendor.bean;

import java.io.Serializable;

public class Query implements Serializable{

	private static final long serialVersionUID = -4247386163885395050L;

	public static final String PROBLEM = "problem";
	public static final String TRANSACTION = "transaction";
	
	private String name;
	private String type;
	private String phone;
	private String email;
	private String subject;
	private String message;
	
	public Query () {
		
	}
	
	public Query(String name, String type, String phone, String email,
			String subject, String message) {
		super();
		this.name = name;
		this.type = type;
		this.phone = phone;
		this.email = email;
		this.subject = subject;
		this.message = message;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
