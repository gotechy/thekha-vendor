package com.thekha.vendor.bean;

import java.io.Serializable;

public class Facilities implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8746020501895895647L;
	private int id;
	private boolean ac = false;
	private boolean sa = false;
	private boolean vp = false;
	private boolean cc = false;
	private boolean veg = false;
	private boolean nonVeg = false;
	
	public static String AC = "Air Conditioned";
	public static String SA = "Smoking Area";
	public static String VP = "Valet Parking";
	public static String CC = "Credit Card Accepted";
	public static String VEG = "Vegetarian";
	public static String NON_VEG = "Non-Vegetarian";
	
	public Facilities() {
		
	}

	public Facilities(int id, boolean ac, boolean sa, boolean vp, boolean cc,
			boolean veg, boolean nonVeg) {
		super();
		this.id = id;
		this.ac = ac;
		this.sa = sa;
		this.vp = vp;
		this.cc = cc;
		this.veg = veg;
		this.nonVeg = nonVeg;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isAc() {
		return ac;
	}
	public void setAc(boolean ac) {
		this.ac = ac;
	}
	public boolean isSa() {
		return sa;
	}
	public void setSa(boolean sa) {
		this.sa = sa;
	}
	public boolean isVp() {
		return vp;
	}
	public void setVp(boolean vp) {
		this.vp = vp;
	}
	public boolean isCc() {
		return cc;
	}
	public void setCc(boolean cc) {
		this.cc = cc;
	}
	public boolean isVeg() {
		return veg;
	}
	public void setVeg(boolean veg) {
		this.veg = veg;
	}
	public boolean isNonVeg() {
		return nonVeg;
	}
	public void setNonVeg(boolean nonVeg) {
		this.nonVeg = nonVeg;
	}
	
}
