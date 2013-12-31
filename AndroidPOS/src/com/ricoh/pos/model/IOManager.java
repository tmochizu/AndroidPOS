package com.ricoh.pos.model;

import java.io.BufferedReader;

import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;

public interface IOManager {

	public abstract BufferedReader importCSVfromAssets(AssetManager assetManager);

	public abstract void insertRecords(SQLiteDatabase database,
			BufferedReader bufferReader);

	public String[] searchAlldata(SQLiteDatabase database);
	
	public abstract String searchByID(SQLiteDatabase database, int id);
}