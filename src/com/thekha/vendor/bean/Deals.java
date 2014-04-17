package com.thekha.vendor.bean;

import hirondelle.date4j.DateTime;

import java.io.Serializable;

public class Deals implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5913018284176073902L;

	public static final String STATUS_ACTIVE = "ACTIVE";
	public static final String STATUS_PENDING = "PENDING";
	public static final String STATUS_COMPLETED = "COMPLETED";
	public static final String DEALS_KEY = "deals";
	
	private int id;
	private String title ="";
	private String description = "";
	private String code = "";
	private String imageURL ="";
	private DateTime from;
	private DateTime to;
	private String status;
	private DealsPlacement placement;
	private int SMSCount;
	private int emailCount;
	
	
	public Deals() {
		super();
		placement = new DealsPlacement();
	}
	
	public Deals(String title, String description, String code,
			String imageURL, DateTime from, DateTime to, String status,
			DealsPlacement placement, int sMSCount, int emailCount) {
		super();
		this.title = title;
		this.description = description;
		this.code = code;
		this.imageURL = imageURL;
		this.from = from;
		this.to = to;
		this.status = status;
		this.placement = placement;
		SMSCount = sMSCount;
		this.emailCount = emailCount;
	}
	
	public Deals(int id, String title, String description, String code,
			String imageURL, DateTime from, DateTime to, String status,
			DealsPlacement placement, int sMSCount, int emailCount) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.code = code;
		this.imageURL = imageURL;
		this.from = from;
		this.to = to;
		this.status = status;
		this.placement = placement;
		SMSCount = sMSCount;
		this.emailCount = emailCount;
	}
	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public DateTime getFrom() {
		return from;
	}
	public void setFrom(DateTime from) {
		this.from = from;
	}
	public DateTime getTo() {
		return to;
	}
	public void setTo(DateTime to) {
		this.to = to;
	}
	public DealsPlacement getPlacement() {
		return placement;
	}
	public void setPlacement(DealsPlacement placement) {
		this.placement = placement;
	}
	public int getSMSCount() {
		return SMSCount;
	}
	public void setSMSCount(int sMSCount) {
		SMSCount = sMSCount;
	}
	public int getEmailCount() {
		return emailCount;
	}
	public void setEmailCount(int emailCount) {
		this.emailCount = emailCount;
	}

}
