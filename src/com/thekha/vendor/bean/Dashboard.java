package com.thekha.vendor.bean;

public class Dashboard {
	
	int status;
	String businessName;
	int profileCompleteness;
	int profileViews;
	int noOfDealsActive;
	int noOfDeals;
	int creditsBalance;
	int advertising;
	int banner;
	int clicks;
	
	public Dashboard(int status, String businessName, int profileCompleteness,
			int profileViews, int noOfDealsActive, int noOfDeals,
			int creditsBalance, int advertising, int banner, int clicks) {
		super();
		this.status = status;
		this.businessName = businessName;
		this.profileCompleteness = profileCompleteness;
		this.profileViews = profileViews;
		this.noOfDealsActive = noOfDealsActive;
		this.noOfDeals = noOfDeals;
		this.creditsBalance = creditsBalance;
		this.advertising = advertising;
		this.banner = banner;
		this.clicks = clicks;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public int getProfileCompleteness() {
		return profileCompleteness;
	}

	public void setProfileCompleteness(int profileCompleteness) {
		this.profileCompleteness = profileCompleteness;
	}

	public int getProfileViews() {
		return profileViews;
	}

	public void setProfileViews(int profileViews) {
		this.profileViews = profileViews;
	}

	public int getNoOfDealsActive() {
		return noOfDealsActive;
	}

	public void setNoOfDealsActive(int noOfDealsActive) {
		this.noOfDealsActive = noOfDealsActive;
	}

	public int getNoOfDeals() {
		return noOfDeals;
	}

	public void setNoOfDeals(int noOfDeals) {
		this.noOfDeals = noOfDeals;
	}

	public int getCreditsBalance() {
		return creditsBalance;
	}

	public void setCreditsBalance(int creditsBalance) {
		this.creditsBalance = creditsBalance;
	}

	public int getAdvertising() {
		return advertising;
	}

	public void setAdvertising(int advertising) {
		this.advertising = advertising;
	}

	public int getBanner() {
		return banner;
	}

	public void setBanner(int banner) {
		this.banner = banner;
	}

	public int getClicks() {
		return clicks;
	}

	public void setClicks(int clicks) {
		this.clicks = clicks;
	}
}
