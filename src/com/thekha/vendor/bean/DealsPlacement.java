package com.thekha.vendor.bean;

public class DealsPlacement {

	private Boolean regular;
	private Boolean special;
	private Boolean topListing;
	private Boolean homePageBanner;
	private Boolean categoryBanner;

	public DealsPlacement() {
		// TODO Auto-generated constructor stub
		
	}
	
	public DealsPlacement(Boolean regular, Boolean special, Boolean topListing, Boolean homePageBanner, Boolean categoryBanner) {
		// TODO Auto-generated constructor stub
		
		this.regular = regular;
		this.special = special;
		this.topListing = topListing;
		this.homePageBanner = homePageBanner;
		this.categoryBanner = categoryBanner;	
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
