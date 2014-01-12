package com.ricoh.pos.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.ricoh.pos.data.SingleSalesRecord;
import com.ricoh.pos.dummy.DummyDataBaseAccessor;

public class SalesRecordManager {
	
	private static SalesRecordManager instance;
	private DummyDataBaseAccessor dummyDataBaseAccessor;
	
	private SalesRecordManager(){
		// TODO: change DummDataBase to actual one.
		dummyDataBaseAccessor = new DummyDataBaseAccessor();
	}
	
	public static SalesRecordManager getInstance(){
		if (instance == null) {
			instance = new SalesRecordManager();
		}
		return instance;
	}
	
	public void storeSingleSalesRecord(SingleSalesRecord record){
		if (record == null) {
			throw new IllegalArgumentException("The passing record is null");
		}
		//TODO: save record to DataBase
		dummyDataBaseAccessor.saveSalesRecord(record);
	}
	
	public ArrayList<SingleSalesRecord> restoreSingleSalesRecordsOfTheDay(Date date){
		ArrayList<SingleSalesRecord> allSalesRecords = dummyDataBaseAccessor.getSalesRecords();
		ArrayList<SingleSalesRecord> salesRecordsOfTheDay = new ArrayList<SingleSalesRecord>();
		
		for (SingleSalesRecord record : allSalesRecords) {
			if ( isSameDay(date,record.getSalesDate()) ) {
				salesRecordsOfTheDay.add(record);
			}
		}
		return salesRecordsOfTheDay;
	}
	
	private boolean isSameDay(Date date1, Date date2){
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
	
}
