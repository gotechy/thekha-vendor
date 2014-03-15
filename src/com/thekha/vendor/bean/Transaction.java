package com.thekha.vendor.bean;

import hirondelle.date4j.DateTime;

import java.io.Serializable;
import java.util.Random;

public class Transaction implements Serializable{
	
	private static final long serialVersionUID = -597885129713459391L;
	
	private String title, description;
	private int amount;
	private static int balance;
	private DateTime dateTime;
	
	public Transaction() {
		
	}
	
	public Transaction(String title, String description, int amount,
			DateTime dateTime) {
		super();
		this.title = title;
		this.description = description;
		this.amount = amount;
		this.dateTime = dateTime;
		balance = (new Random().nextInt()) * 1000;
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

	public DateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(DateTime dateTime) {
		this.dateTime = dateTime;
	}
	
}
