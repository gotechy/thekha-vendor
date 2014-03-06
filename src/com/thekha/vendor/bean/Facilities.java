package com.thekha.vendor.bean;

public class Facilities {
	private int id;
	private boolean ac = false;
	private boolean sa = false;
	private boolean vp = false;
	private boolean cc = false;
	private boolean veg = false;
	private boolean nonVeg = false;
	
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
