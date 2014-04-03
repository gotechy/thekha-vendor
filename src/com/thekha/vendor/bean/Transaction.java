package com.thekha.vendor.bean;

import java.io.Serializable;

public class Transaction implements Serializable{
	
	private static final long serialVersionUID = -597885129713459391L;
	
	public static final String TITLE_DEBIT = "Debited";
	
	private String title, description;
	private int amount;
	private static int balance = 0;
	private String timeStamp;
	
	public Transaction() {
		
	}
	
	public Transaction(String title, String description, int amount) {
		super();
		this.title = title;
		this.description = description;
		this.amount = amount;
	}

	public Transaction(String title, String description, int amount,
			String timeStamp) {
		super();
		this.title = title;
		this.description = description;
		this.amount = amount;
		this.timeStamp = timeStamp;
	}

	public static int getBalance() {
		return balance;
	}
	
	public static void setBalance(int balance) {
		Transaction.balance = balance;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
}
