package com.thekha.vendor.bean;

import java.io.Serializable;

public class Prices implements Serializable{
	
	private static final long serialVersionUID = 3200583165664527833L;

	public static final String PRICES_KEY = "prices";
	
	private String regular;
	private String special;
	private String topListing;
	private String homePageBanner;
	private String categoryBanner;
	private String pushSMS;
	private String pushEMail;
	
	public Prices(){
		
	}
	
	public Prices(String regular, String special, String topListing,
			String homePageBanner, String categoryBanner, String pushSMS,
			String pushEMail) {
		super();
		this.regular = regular;
		this.special = special;
		this.topListing = topListing;
		this.homePageBanner = homePageBanner;
		this.categoryBanner = categoryBanner;
		this.pushSMS = pushSMS;
		this.pushEMail = pushEMail;
	}
	public String getRegular() {
		return regular;
	}
	public void setRegular(String regular) {
		this.regular = regular;
	}
	public String getSpecial() {
		return special;
	}
	public void setSpecial(String special) {
		this.special = special;
	}
	public String getTopListing() {
		return topListing;
	}
	public void setTopListing(String topListing) {
		this.topListing = topListing;
	}
	public String getHomePageBanner() {
		return homePageBanner;
	}
	public void setHomePageBanner(String homePageBanner) {
		this.homePageBanner = homePageBanner;
	}
	public String getCategoryBanner() {
		return categoryBanner;
	}
	public void setCategoryBanner(String categoryBanner) {
		this.categoryBanner = categoryBanner;
	}
	public String getPushSMS() {
		return pushSMS;
	}
	public void setPushSMS(String pushSMS) {
		this.pushSMS = pushSMS;
	}
	public String getPushEMail() {
		return pushEMail;
	}
	public void setPushEMail(String pushEMail) {
		this.pushEMail = pushEMail;
	}
	
}
