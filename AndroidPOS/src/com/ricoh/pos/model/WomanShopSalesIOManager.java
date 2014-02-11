package com.ricoh.pos.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.ContentValues;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ricoh.pos.data.WomanShopSalesDef;

public class WomanShopSalesIOManager implements IOManager {

	private static String DATABASE_NAME = "sales_dummy";

	public WomanShopSalesIOManager() {
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
				String[] fieldValues = record.split(",");
				Log.d("debug", "Product Code" + fieldValues[0]);

				int i = 0;
				for (WomanShopSalesDef field : WomanShopSalesDef.values()) {
					contentValue.put(field.name(), fieldValues[i++]);
				}

				database.insertWithOnConflict(DATABASE_NAME, null, contentValue,
						SQLiteDatabase.CONFLICT_REPLACE);
			}
		} catch (IOException e) {
			Log.d("debug", "" + e + "");
		}
	}
	
	@Override
	public void insertSingleRecord(SQLiteDatabase database, String record) {
		
		ContentValues contentValue = new ContentValues();

		String[] fieldValues = record.split(",");
		Log.d("debug", "Product Code" + fieldValues[0]);

		int i = 0;
		for (WomanShopSalesDef field : WomanShopSalesDef.values()) {
			contentValue.put(field.name(), fieldValues[i++]);
		}

		database.insertWithOnConflict(DATABASE_NAME, null, contentValue,
				SQLiteDatabase.CONFLICT_REPLACE);

	}

	@Override
	public String[] searchAlldata(SQLiteDatabase database) {
		Cursor cursor = null;

		try {
			cursor = database.query(
					DATABASE_NAME,
					new String[] { WomanShopSalesDef.PRODUCT_CODE.name(),
							WomanShopSalesDef.PRODUCT_CATEGORY.name(),
							WomanShopSalesDef.ITEM_CATEGORY.name(),
							WomanShopSalesDef.QTY.name(),
							WomanShopSalesDef.SALE_PRICE.name(),
							WomanShopSalesDef.TOTAL_SALE_PRICE.name(),
							WomanShopSalesDef.DISCOUNT.name(),
							WomanShopSalesDef.DATE.name() }, 
							null, null, null, null, null);
			String[] results = new String[cursor.getCount()];
			Log.d("debug", "count:" + cursor.getCount());
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
	public String searchByCode(SQLiteDatabase database, String code) {
		Cursor cursor = null;
		try {
			cursor = database.query(
					DATABASE_NAME, 
					new String[] { WomanShopSalesDef.PRODUCT_CODE.name(),
							WomanShopSalesDef.PRODUCT_CATEGORY.name(),
							WomanShopSalesDef.ITEM_CATEGORY.name(),
							WomanShopSalesDef.QTY.name(),
							WomanShopSalesDef.SALE_PRICE.name(),
							WomanShopSalesDef.TOTAL_SALE_PRICE.name(),
							WomanShopSalesDef.DISCOUNT.name(),
							WomanShopSalesDef.DATE.name() }, 
							WomanShopSalesDef.PRODUCT_CODE.name() + " = ?", 
							new String[] { code }, null, null, null);
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

		int indexProductCode = cursor.getColumnIndex(WomanShopSalesDef.PRODUCT_CODE.name());
		int indexProductCategory = cursor.getColumnIndex(WomanShopSalesDef.PRODUCT_CATEGORY.name());
		int indexItemCategory = cursor.getColumnIndex(WomanShopSalesDef.ITEM_CATEGORY.name());
		int indexQTY = cursor.getColumnIndex(WomanShopSalesDef.QTY.name());
		int indexSalePrice = cursor.getColumnIndex(WomanShopSalesDef.SALE_PRICE.name());
		int indexTotalSalePrice = cursor.getColumnIndex(WomanShopSalesDef.TOTAL_SALE_PRICE.name());
		int indexDiscount = cursor.getColumnIndex(WomanShopSalesDef.DISCOUNT.name());
		int indexDate = cursor.getColumnIndex(WomanShopSalesDef.DATE.name());

		if (cursor.moveToNext()) {
			String productCode = cursor.getString(indexProductCode);
			String productCategory = cursor.getString(indexProductCategory);
			String itemCategory = cursor.getString(indexItemCategory);
			int qty = cursor.getInt(indexQTY);
			double salePrice = cursor.getDouble(indexSalePrice);
			double totalSalePrice = cursor.getDouble(indexTotalSalePrice);
			double discount = cursor.getDouble(indexDiscount);
			String date = cursor.getString(indexDate);

			result += productCode + ":" + productCategory + ":" + itemCategory + ":" + qty + ":"
					+ salePrice + ":" + totalSalePrice + ":" + discount + ":" + date + "\n";
		}
		return result;
	}
}

