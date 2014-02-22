package com.ricoh.pos.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ricoh.pos.data.Order;
import com.ricoh.pos.data.SingleSalesRecord;
import com.ricoh.pos.dummy.DummyDataBaseAccessor;

public class SalesRecordManager {
	
	private static SalesRecordManager instance;
	private WomanShopSalesIOManager womanShopSalesIOManager;
	
	public static final String SALES_DATE_KEY = "SalesDate";
	
	private SalesRecordManager(){
		womanShopSalesIOManager = new WomanShopSalesIOManager();
	}
	
	public static SalesRecordManager getInstance(){
		if (instance == null) {
			instance = new SalesRecordManager();
		}
		return instance;
	}
	
	public void storeSingleSalesRecord(SQLiteDatabase database, SingleSalesRecord record){
		if (record == null) {
			throw new IllegalArgumentException("The passing record is null");
		}
		
		womanShopSalesIOManager.saveSalesRecord(database, record);
		
		//TODO: for debug
		String[] results = womanShopSalesIOManager.searchAlldata(database);
		for (String result : results) {
			Log.d("debug", "Sales:" + result);
		}
	}
	
	public ArrayList<SingleSalesRecord> restoreSingleSalesRecordsOfTheDay(Date date){
		ArrayList<SingleSalesRecord> allSalesRecords = womanShopSalesIOManager.getSalesRecords();
		ArrayList<SingleSalesRecord> salesRecordsOfTheDay = new ArrayList<SingleSalesRecord>();
		
		for (SingleSalesRecord record : allSalesRecords) {
			if ( areSameDay(date,record.getSalesDate()) ) {
				salesRecordsOfTheDay.add(record);
			}
		}
		return salesRecordsOfTheDay;
	}
	
	public SingleSalesRecord getSingleSalesRecord(Date date){
		ArrayList<SingleSalesRecord> allSalesRecords = womanShopSalesIOManager.getSalesRecords();
		
		for (SingleSalesRecord record : allSalesRecords) {
			if (areSameMinute(record.getSalesDate(), date)) {
				return record;
			}
		}
		
		throw new IllegalStateException("Single sales record of the date is not found");
	}
	
	private boolean areSameDay(Date date1, Date date2){
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		
		if ( cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
			 cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
			 cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
			) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean areSameMinute(Date date1, Date date2){
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		
		if ( cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
			 cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
			 cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH) &&
			 cal1.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY) &&
			 cal1.get(Calendar.MINUTE) == cal2.get(Calendar.MINUTE)
			) {
			return true;
		} else {
			return false;
		}
	}
	
}
