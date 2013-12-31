package com.ricoh.pos.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.ContentValues;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ricoh.pos.data.WomanShopDataDef;

public class WomanShopIOManager implements IOManager {

	private static String DATABASE_NAME = "products";

	public WomanShopIOManager() {
		// Nothing to do
	}

	@Override
	public BufferedReader importCSVfromAssets(AssetManager assetManager) {
		BufferedReader bufferReader = null;
		try {
			// TODO: Should import & export data
			bufferReader = new BufferedReader(new InputStreamReader(assetManager.open(DATABASE_NAME
					+ ".csv")));
		} catch (IOException e) {
			Log.d("debug", "" + e + "");
		}
		return bufferReader;
	}

	@Override
	public void insertRecords(SQLiteDatabase database, BufferedReader bufferReader) {

		ContentValues contentValue = new ContentValues();
		try {
			readFieldName(bufferReader);

			String record;
			while ((record = bufferReader.readLine()) != null) {
				String[] fields = record.split(",");
				Log.d("debug", "S No." + fields[0]);

				contentValue.put(WomanShopDataDef.S_NO.name(), fields[0]);
				contentValue.put(WomanShopDataDef.PRODUCT_ID.name(), fields[1]);
				contentValue.put(WomanShopDataDef.CATEGORY.name(), fields[2]);
				contentValue.put(WomanShopDataDef.OFFICE_NAME.name(), fields[3]);
				contentValue.put(WomanShopDataDef.PRODUCT_NAME.name(), fields[4]);
				contentValue.put(WomanShopDataDef.PRICE_PIECE.name(), fields[5]);
				contentValue.put(WomanShopDataDef.NO_OF_PIECES.name(), fields[6]);
				contentValue.put(WomanShopDataDef.PRICE_BOX.name(), fields[7]);
				contentValue.put(WomanShopDataDef.TAX_TYPE.name(), fields[8]);
				contentValue.put(WomanShopDataDef.TAX_PERCENTAGE.name(), fields[9]);

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
			cursor = database.query(DATABASE_NAME, new String[] { WomanShopDataDef.S_NO.name(),
					WomanShopDataDef.PRODUCT_ID.name(), WomanShopDataDef.CATEGORY.name(),
					WomanShopDataDef.OFFICE_NAME.name(), WomanShopDataDef.PRODUCT_NAME.name(),
					WomanShopDataDef.PRICE_PIECE.name(), WomanShopDataDef.NO_OF_PIECES.name(),
					WomanShopDataDef.PRICE_BOX.name(), WomanShopDataDef.TAX_TYPE.name(),
					WomanShopDataDef.TAX_PERCENTAGE.name() }, null, null, null, null, null);
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
			cursor = database.query(DATABASE_NAME, new String[] { WomanShopDataDef.S_NO.name(),
					WomanShopDataDef.PRODUCT_ID.name(), WomanShopDataDef.CATEGORY.name(),
					WomanShopDataDef.OFFICE_NAME.name(), WomanShopDataDef.PRODUCT_NAME.name(),
					WomanShopDataDef.PRICE_PIECE.name(), WomanShopDataDef.NO_OF_PIECES.name(),
					WomanShopDataDef.PRICE_BOX.name(), WomanShopDataDef.TAX_TYPE.name(),
					WomanShopDataDef.TAX_PERCENTAGE.name() }, WomanShopDataDef.PRODUCT_ID.name()
					+ " = ?", new String[] { "" + id }, null, null, null);
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

		int indexSNo = cursor.getColumnIndex(WomanShopDataDef.S_NO.name());
		int indexProductId = cursor.getColumnIndex(WomanShopDataDef.PRODUCT_ID.name());
		int indexCategory = cursor.getColumnIndex(WomanShopDataDef.CATEGORY.name());
		int indexOfficeName = cursor.getColumnIndex(WomanShopDataDef.OFFICE_NAME.name());
		int indexProductName = cursor.getColumnIndex(WomanShopDataDef.PRODUCT_NAME.name());
		int indexPricePiece = cursor.getColumnIndex(WomanShopDataDef.PRICE_PIECE.name());
		int indexNoOfPieces = cursor.getColumnIndex(WomanShopDataDef.NO_OF_PIECES.name());
		int indexPriceBox = cursor.getColumnIndex(WomanShopDataDef.PRICE_BOX.name());
		int indexTaxType = cursor.getColumnIndex(WomanShopDataDef.TAX_TYPE.name());
		int indexTaxPercentage = cursor.getColumnIndex(WomanShopDataDef.TAX_PERCENTAGE.name());

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

			result += sNo + ":" + productId + ":" + category + ":" + officeName + ":" + productName
					+ ":" + pricePiece + ":" + noOfPieces + ":" + priceBox + ":" + taxType + ":"
					+ taxPercentage + "\n";
		}
		return result;
	}
}
