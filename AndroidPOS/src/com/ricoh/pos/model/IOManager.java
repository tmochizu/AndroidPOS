package com.ricoh.pos.model;

import java.io.BufferedReader;

import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;

/**
 * This class manage I/O related database
 * @author Takuya Mizuhara
 *
 */
public interface IOManager {
	/**
	 * Import csv file from assets folder.
	 * @param assetManager asset file accessor
	 * @return buffered records imported from csv file 
	 */
	public abstract BufferedReader importCSVfromAssets(AssetManager assetManager);

	/**
	 * Insert all record into database.
	 * @param database database created in app
	 * @param bufferReader buffered records imported from csv file
	 */
	public abstract void insertRecords(SQLiteDatabase database,
			BufferedReader bufferReader);
	
	/**
	 * Insert all record into database.
	 * @param database database created in app
	 * @param record single record
	 */
	public abstract void insertSingleRecord(SQLiteDatabase database, String record);

	/**
	 * Search all record from database.
	 * @param database database created in app
	 * @return search results
	 */
	public String[] searchAlldata(SQLiteDatabase database);
	
	/**
	 * Search a record from database by product code
	 * @param database database created in app
	 * @param product code connected with a record
	 * @return search result
	 */
	public abstract String searchByCode(SQLiteDatabase database, String code);
}