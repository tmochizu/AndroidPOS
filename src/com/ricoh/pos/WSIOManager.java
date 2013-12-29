package com.ricoh.pos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.ContentValues;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class WSIOManager implements IOManager {

	private static String DATABASE_NAME = "products";
	private static String S_NO = "SNo";
	private static String PRODUCT_ID = "ProductID";
	private static String CATEGORY = "Category";
	private static String OFFICE_NAME = "OfficeName";
	private static String PRODUCT_NAME = "ProductName";
	private static String PRICE_PIECE = "PricePiece";
	private static String NO_OF_PIECES = "NoOfPieces";
	private static String PRICE_BOX = "PriceBox";
	private static String TAX_TYPE = "TaxType";
	private static String TAX_PERCENTAGE = "TaxPercentage";

	public WSIOManager() {
		// Nothing to do
	}

	@Override
	public BufferedReader importCSVfromAssets(AssetManager assetManager) {
		BufferedReader bufferReader = null;
		try {
			// TODO: Should import & export data
			bufferReader = new BufferedReader(new InputStreamReader(
					assetManager.open(DATABASE_NAME + ".csv")));
		} catch (IOException e) {
			Log.d("debug", "" + e + "");
		}
		return bufferReader;
	}

	@Override
	public void insertRecords(SQLiteDatabase database,
			BufferedReader bufferReader) {

		ContentValues contentValue = new ContentValues();
		try {
			readFieldName(bufferReader);

			String record;
			while ((record = bufferReader.readLine()) != null) {
				String[] fields = record.split(",");
				Log.d("debug", "S No." + fields[0]);

				contentValue.put(S_NO, fields[0]);
				contentValue.put(PRODUCT_ID, fields[1]);
				contentValue.put(CATEGORY, fields[2]);
				contentValue.put(OFFICE_NAME, fields[3]);
				contentValue.put(PRODUCT_NAME, fields[4]);
				contentValue.put(PRICE_PIECE, fields[5]);
				contentValue.put(NO_OF_PIECES, fields[6]);
				contentValue.put(PRICE_BOX, fields[7]);
				contentValue.put(TAX_TYPE, fields[8]);
				contentValue.put(TAX_PERCENTAGE, fields[9]);

				database.insert(DATABASE_NAME, null, contentValue);
			}
		} catch (IOException e) {
			Log.d("debug", "" + e + "");
		}
	}

	// TODO: Temporary function
	@Override
	public String searchByID(SQLiteDatabase database, int id) {
		Cursor cursor = null;
		try {
			cursor = database.query(DATABASE_NAME, new String[] { S_NO,
					PRODUCT_ID, CATEGORY, OFFICE_NAME, PRODUCT_NAME,
					PRICE_PIECE, NO_OF_PIECES, PRICE_BOX, TAX_TYPE,
					TAX_PERCENTAGE }, "ProductID = ?",
					new String[] { "" + id }, null, null, null);
			return readCursor(cursor);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
	
	private void readFieldName(BufferedReader bufferReader) throws IOException {
		String record = bufferReader.readLine();

		// TODO: Show field name for debug
		String[] fieldNames = record.split(",");
		for (String fieldName : fieldNames) {
			Log.d("debug", fieldName);
		}
	}

	// TODO: Temporary function
	private String readCursor(Cursor cursor) {
		String result = "";

		int indexId = cursor.getColumnIndex(PRODUCT_ID);
		int indexProductName = cursor.getColumnIndex(PRODUCT_NAME);

		while (cursor.moveToNext()) {
			int id = cursor.getInt(indexId);
			String productName = cursor.getString(indexProductName);
			result += id + ":" + productName + "\n";
		}
		return result;
	}
}
