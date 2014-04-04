package com.thekha.vendor.bean;

import java.io.Serializable;

public class DealsPlacement implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3146048448569853218L;
	
	private int id;
	private boolean regular;
	private boolean special;
	private boolean topListing;
	private boolean homePageBanner;
	private boolean categoryBanner;

	public DealsPlacement() {
		
	}
	
	public DealsPlacement(Boolean regular, Boolean special, Boolean topListing, Boolean homePageBanner, Boolean categoryBanner) {
		
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

	public boolean isRegular() {
		return regular;
	}

	public void setRegular(boolean regular) {
		this.regular = regular;
	}

	public boolean isSpecial() {
		return special;
	}

	public void setSpecial(boolean special) {
		this.special = special;
	}

	public boolean isTopListing() {
		return topListing;
	}

	public void setTopListing(boolean topListing) {
		this.topListing = topListing;
	}

	public boolean isHomePageBanner() {
		return homePageBanner;
	}

	public void setHomePageBanner(boolean homePageBanner) {
		this.homePageBanner = homePageBanner;
	}

	public boolean isCategoryBanner() {
		return categoryBanner;
	}

	public void setCategoryBanner(boolean categoryBanner) {
		this.categoryBanner = categoryBanner;
	}
	
}
