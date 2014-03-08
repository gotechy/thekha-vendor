package com.thekha.vendor.bean;

import java.io.Serializable;

public class Business implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1535829335411586863L;
	public static final String HOTEL = "Hotel";
	public static final String BAR = "Bar";
	public static final String RESORT = "Resort";
	public static final String BANQUET = "Banquet";
	
	public static final String BUSINESS_KEY = "business";
	
	private int id;
	private String name = "";
	private String  type = "";
	private String imageURL = "";
	private Address address =  new Address();
	private String phone1 = "";
	private String phone2 = "";
	private String email = "";
	private String website = "";
	private String facebook = "";
	private Facilities facilities = new Facilities();
	
	public Business() {
		
	}
	
	public Business(int id, String name, String type, String imageURL,
			Address address, String phone1, String phone2, String email,
			String website, String facebook, Facilities facilities) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.imageURL = imageURL;
		this.address = address;
		this.phone1 = phone1;
		this.phone2 = phone2;
		this.email = email;
		this.website = website;
		this.facebook = facebook;
		this.facilities = facilities;
	}



	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public String getPhone1() {
		return phone1;
	}
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}
	public String getPhone2() {
		return phone2;
	}
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getFacebook() {
		return facebook;
	}
	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}
	public Facilities getFacilities() {
		return facilities;
	}
	public void setFacilities(Facilities facilities) {
		this.facilities = facilities;
	}
	
}
