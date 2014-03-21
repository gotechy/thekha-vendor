package com.thekha.vendor.bean;

import java.io.Serializable;

public class DealsPlacement implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3146048448569853218L;
	
	private int id;
	private Boolean regular;
	private Boolean special;
	private Boolean topListing;
	private Boolean homePageBanner;
	private Boolean categoryBanner;

	public DealsPlacement() {
		
	}
	
	public DealsPlacement(Boolean regular, Boolean special, Boolean topListing, Boolean homePageBanner, Boolean categoryBanner) {
		// TODO Auto-generated constructor stub
		
		this.regular = regular;
		this.special = special;
		this.topListing = topListing;
		this.homePageBanner = homePageBanner;
		this.categoryBanner = categoryBanner;	
	}
	
	public DealsPlacement(int id, Boolean regular, Boolean special,
			Boolean topListing, Boolean homePageBanner, Boolean categoryBanner) {
		super();
		this.id = id;
		this.regular = regular;
		this.special = special;
		this.topListing = topListing;
		this.homePageBanner = homePageBanner;
		this.categoryBanner = categoryBanner;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Boolean getRegular() {
		return regular;
	}

	public void setRegular(Boolean regular) {
		this.regular = regular;
	}

	public Boolean getSpecial() {
		return special;
	}

	public void setSpecial(Boolean special) {
		this.special = special;
	}

	public Boolean getTopListing() {
		return topListing;
	}

	public void setTopListing(Boolean topListing) {
		this.topListing = topListing;
	}

	public Boolean getHomePageBanner() {
		return homePageBanner;
	}

	public void setHomePageBanner(Boolean homePageBanner) {
		this.homePageBanner = homePageBanner;
	}

	public Boolean getCategoryBanner() {
		return categoryBanner;
	}

	public void setCategoryBanner(Boolean categoryBanner) {
		this.categoryBanner = categoryBanner;
	}
	
	
	

}
