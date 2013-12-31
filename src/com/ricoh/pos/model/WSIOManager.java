package com.ricoh.pos.model;

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

	@Override
	public String[] searchAlldata(SQLiteDatabase database) {
		Cursor cursor = null;

		try {
			cursor = database.query(DATABASE_NAME, new String[] { S_NO,
					PRODUCT_ID, CATEGORY, OFFICE_NAME, PRODUCT_NAME,
					PRICE_PIECE, NO_OF_PIECES, PRICE_BOX, TAX_TYPE,
					TAX_PERCENTAGE }, null, null, null, null, null);
			String[] results = new String[cursor.getCount()];
			for (int i = 0; i < cursor.getCount(); i++) {
				results[i] = readCursor(cursor);
			}
			return results;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
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

	private String readCursor(Cursor cursor) {
		String result = "";

		int indexSNo = cursor.getColumnIndex(S_NO);
		int indexProductId = cursor.getColumnIndex(PRODUCT_ID);
		int indexCategory = cursor.getColumnIndex(CATEGORY);
		int indexOfficeName = cursor.getColumnIndex(OFFICE_NAME);
		int indexProductName = cursor.getColumnIndex(PRODUCT_NAME);
		int indexPricePiece = cursor.getColumnIndex(PRICE_PIECE);
		int indexNoOfPieces = cursor.getColumnIndex(NO_OF_PIECES);
		int indexPriceBox = cursor.getColumnIndex(PRICE_BOX);
		int indexTaxType = cursor.getColumnIndex(TAX_TYPE);
		int indexTaxPercentage = cursor.getColumnIndex(TAX_PERCENTAGE);

		if (cursor.moveToNext()) {
			int sNo = cursor.getInt(indexSNo);
			int productId = cursor.getInt(indexProductId);
			String category = cursor.getString(indexCategory);
			String officeName = cursor.getString(indexOfficeName);
			String productName = cursor.getString(indexProductName);
			double pricePiece = cursor.getDouble(indexPricePiece);
			int noOfPieces = cursor.getInt(indexNoOfPieces);
			double priceBox = cursor.getDouble(indexPriceBox);
			String taxType = cursor.getString(indexTaxType);
			int taxPercentage = cursor.getInt(indexTaxPercentage);

			result += sNo + ":" + productId + ":" + category + ":" + officeName
					+ ":" + productName + ":" + pricePiece + ":" + noOfPieces
					+ ":" + priceBox + ":" + taxType + ":" + taxPercentage
					+ "\n";
		}
		return result;
	}
}
