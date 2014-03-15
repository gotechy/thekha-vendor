package com.thekha.vendor.dao;

import hirondelle.date4j.DateTime;

import java.util.Random;
import java.util.TimeZone;

import com.thekha.vendor.bean.Transaction;

public class TransactionDAO {
	// TODO - CRUD for TransactionDAO
		public static Transaction read(){
			int amount = (new Random().nextInt()) * 1000;
			Transaction t = new Transaction("MyTransaction", "This is the discription for transaction.", amount, DateTime.now(TimeZone.getDefault()));
			return t;
		}
}
