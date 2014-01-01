package com.ricoh.pos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ricoh.pos.data.WomanShopDataDef;

public class DatabaseHelper extends SQLiteOpenHelper {

	public DatabaseHelper(Context context) {
		super(context, "products.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("Create Table Products (" + "_id Integer Primary Key, " + WomanShopDataDef.S_NO.name()
				+ " Integer UNIQUE, " + WomanShopDataDef.PRODUCT_ID.name() + " Integer, "
				+ WomanShopDataDef.CATEGORY.name() + " Text, " + WomanShopDataDef.OFFICE_NAME.name() + " Text, "
				+ WomanShopDataDef.PRODUCT_NAME.name() + " Text, " + WomanShopDataDef.PRICE_PIECE.name()
				+ " Real, " + WomanShopDataDef.NO_OF_PIECES.name() + " Integer, "
				+ WomanShopDataDef.PRICE_BOX.name() + " Real, " + WomanShopDataDef.TAX_TYPE.name() + " Text, "
				+ WomanShopDataDef.TAX_PERCENTAGE.name() + " Integer)");
		Log.d("debug", "Database onCreate");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
	}

}