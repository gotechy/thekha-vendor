package com.thekha.vendor.bean;

import java.io.Serializable;

public class Address implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5075882765578088886L;
	private int id;
	private String line1;
	private String line2;
	private String locality;
	private String city;
	private String state;
	private String country;
	private String pin;
	
	public Address() {
		
	}
	
	public Address(int id, String line1, String line2, String locality,
			String city, String state, String country, String pin) {
		super();
		this.id = id;
		this.line1 = line1;
		this.line2 = line2;
		this.locality = locality;
		this.city = city;
		this.state = state;
		this.country = country;
		this.pin = pin;
	}

	@Override
	public String toString() {
		return line1 + ", " + line2 + ", "
				+ locality + ", " + city + ", " + state
				+ ", " + country + ". " + pin;
	}

	public String getLine1() {
		return line1;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setLine1(String line1) {
		this.line1 = line1;
	}
	public String getLine2() {
		return line2;
	}
	public void setLine2(String line2) {
		this.line2 = line2;
	}
	public String getLocality() {
		return locality;
	}
	public void setLocality(String locality) {
		this.locality = locality;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	
}
