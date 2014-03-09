package com.thekha.vendor.bean;

import java.io.Serializable;
import java.util.GregorianCalendar;

import android.content.Intent;

public class Deals implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5913018284176073902L;

	
	public static final String DEALS_KEY = "deals";
	
	private String title ="";
	private String description = "";
	private String code = "";
	private String imageURL ="";
	private GregorianCalendar from;
	private GregorianCalendar to;
	private DealsPlacement placement;
	private int SMSCount;
	private int emailCount;
	
	
	public Deals() {
		
	}
	
	public Deals(String title, String description, String code, 
			String imageURL, GregorianCalendar from, GregorianCalendar to, 
			DealsPlacement placement, int SMSCount, int emailCount) {
		
		super();
		
		this.title = title;
		this.description = description;
		this.code = code;
		this.imageURL = imageURL;
		this.from = from;
		this.to = to;
		this.placement = placement;
		this.SMSCount = SMSCount;
		this.emailCount = emailCount;
	}

	public Deals(Intent intent) {
		
		super();
		
		Deals dealTemp = (Deals) intent.getExtras().getSerializable(DEALS_KEY);
		
		this.title = dealTemp.getTitle();
		this.description = dealTemp.getDescription();
		this.code = dealTemp.getCode();
		this.imageURL = dealTemp.getImageURL();
		this.from = dealTemp.getFrom();
		this.to = dealTemp.getTo();
		this.placement = dealTemp.getPlacement();
		this.SMSCount = dealTemp.getSMSCount();
		this.emailCount = dealTemp.getEmailCount();
	}

	public static void packageIntent(Intent intent, Deals src) {
		
		intent.putExtra(DEALS_KEY, src);
		
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
	public GregorianCalendar getFrom() {
		return from;
	}
	public void setFrom(GregorianCalendar from) {
		this.from = from;
	}
	public GregorianCalendar getTo() {
		return to;
	}
	public void setTo(GregorianCalendar to) {
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
