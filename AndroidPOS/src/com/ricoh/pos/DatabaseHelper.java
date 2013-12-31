package com.ricoh.pos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	public DatabaseHelper(Context context) {
		super(context, "products.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("Create Table Products (" + "_id Integer Primary Key, "
				+ "SNo Integer, " + "ProductID Integer, " + "Category Text, "
				+ "OfficeName Text, " + "ProductName Text, "
				+ "PricePiece Real, " + "NoOfPieces Integer, "
				+ "PriceBox Real, " + "TaxType Text, "
				+ "TaxPercentage Integer)");
		Log.d("debug", "Database onCreate");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
	}

}