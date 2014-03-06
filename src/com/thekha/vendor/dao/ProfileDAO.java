package com.thekha.vendor.dao;

import java.util.ArrayList;

import com.thekha.vendor.bean.Address;
import com.thekha.vendor.bean.Business;
import com.thekha.vendor.bean.Facilities;
import com.thekha.vendor.bean.Phone;

public class ProfileDAO {
	// TODO - CRUD for ProfileDAO
	public Business read(){
		Business b = new Business(1, 
				"Blue Moon",
				Business.HOTEL,
				new Address(1,"E-2a","Jwahar Park","Saket","Delhi","Bihar","India","201005"),
				new ArrayList<Phone>(),
				"bluemoon@gmail.com",
				"www.bluemoon.in",
				"www.facebook.com/me.gaurav.rana",
				new Facilities(1, true, true, false, true, true, true)
		);
		b.getPhones().add(new Phone(1, "9944445555", Phone.RECEPTION));
		b.getPhones().add(new Phone(1, "011-4445555", Phone.BOOKINGS));
		
		return b;
	}
}
