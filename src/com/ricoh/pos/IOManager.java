package com.ricoh.pos;

import java.io.BufferedReader;

import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;

public interface IOManager {

	public abstract BufferedReader importCSVfromAssets(AssetManager assetManager);

	public abstract void insertRecords(SQLiteDatabase database,
			BufferedReader bufferReader);

	public abstract String searchByID(SQLiteDatabase database, int id);
}