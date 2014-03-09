package com.thekha.vendor.dao;

import java.util.GregorianCalendar;

import com.thekha.vendor.bean.Deals;
import com.thekha.vendor.bean.DealsPlacement;

public class DealsDAO {

		public Deals read(){
			Deals b = new Deals ( "Free Chicken", "There would be a free chicken treat only for today!!!", "FBP11101", 
					"", new GregorianCalendar(), new GregorianCalendar(),
					new DealsPlacement( Boolean.FALSE , Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE), 500, 100);
			return b;
		}
		

}
