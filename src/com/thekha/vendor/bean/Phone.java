package com.thekha.vendor.bean;

public class Phone {
	
	private int id;
	private String number;
	private String type;
	
	public static final String MANAGER = "Manager";
	public static final String RECEPTION = "Reception";
	public static final String BOOKINGS = "Bookings";
	public static final String SUPPORT = "Support";
	
	public Phone() {
		
	}
	
	public Phone(int id, String number, String type) {
		super();
		this.id = id;
		this.number = number;
		this.type = type;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
