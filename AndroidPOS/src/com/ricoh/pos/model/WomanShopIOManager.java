package com.ricoh.pos.model;

import android.content.ContentValues;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.ricoh.pos.data.WomanShopDataDef;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class WomanShopIOManager {

	private SQLiteDatabase database;
	private static String DATABASE_NAME = "products_dummy";
	private static String csvStorageFolder = "/Ricoh";
	
	private final String[] WomanShopDataStructure = new String[]{
			WomanShopDataDef.PRODUCT_CODE.name(),
			WomanShopDataDef.ITEM_CATEGORY.name(),
			WomanShopDataDef.PRODUCT_CATEGORY.name(),
			WomanShopDataDef.SALE_PRICE.name(),
			WomanShopDataDef.COST_TO_ENTREPRENEUR.name() };

	public WomanShopIOManager() {
		// Nothing to do
	}

	public String[] searchAlldata() {
		Cursor cursor = null;

		try {
			cursor = database.query(
					DATABASE_NAME,
					WomanShopDataStructure,
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
	public String searchByCode(String code) {
		Cursor cursor = null;
		try {
			cursor = database.query(
					DATABASE_NAME,
					WomanShopDataStructure,
					WomanShopDataDef.PRODUCT_CODE.name() + " = ?",
					new String[] { code },
					null,
					null,
					null);
			return readCursor(cursor);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	/**
	 * Assertから在庫DBにデータをimportする。使ってない。
	 * @param assetManager
	 * @throws IOException
	 */
	public void importCSVfromAssets(AssetManager assetManager) throws IOException {
		try {
			BufferedReader bufferReader = new BufferedReader(new InputStreamReader(assetManager.open(DATABASE_NAME + ".csv")));
			this.insertRecords(bufferReader);
			bufferReader.close();
		} catch (IOException e) {
			Log.d("debug", "" + e + "");
			throw e;
		}
	}

	/**
	 * 固定パスのCSVから、内容を在庫DBにinsertする。
	 * @throws IOException
	 */
	public void importCSVfromSD() throws IOException {
		BufferedReader bufferReader = null;
		try {
			String csvStoragePath = getCSVStoragePath();
			File productDataCSV = new File(csvStoragePath + "/Product.csv");
			bufferReader = new BufferedReader(new FileReader(productDataCSV));
			this.insertRecords(bufferReader);
		}
		catch (FileNotFoundException e) {
			Log.d("debug", "CSV File not found.", e);
			throw e;
		}
		catch (IOException ioe) {
			Log.d("debug", "insert Record fail", ioe);
			throw ioe;
		}
		finally {
			bufferReader.close();
		}
	}

	private void insertRecords(BufferedReader bufferReader) throws IOException {
		ContentValues contentValue = new ContentValues();
		// CSVの先頭にカラム名を書いた行があるので読み飛ばす
		String record = bufferReader.readLine();
		//this.writeFieldName(record); // debug用

		while ((record = bufferReader.readLine()) != null) {
			String[] fieldValues = record.split(",");
			Log.d("debug", "Product Code" + fieldValues[0]);

			int i = 0;
			for (WomanShopDataDef field : WomanShopDataDef.values()) {
				contentValue.put(field.name(), fieldValues[i++]);
			}
			database.insertWithOnConflict(DATABASE_NAME, null, contentValue, SQLiteDatabase.CONFLICT_REPLACE);
		}
	}

	/**
	 * debug用にカラム名を出力する
	 * @param record CSVの一行目の文字列
	 */
	private void writeFieldName(String record) {
		String[] fieldNames = record.split(",");
		for (String fieldName : fieldNames) {
			Log.d("debug", fieldName);
		}
	}

	// TODO: Add this function to interface
	public void setDatabase(SQLiteDatabase database) {
		if (this.database == null) {
			Log.d("debug", "Database set:" + DATABASE_NAME);
			this.database = database;
		}
	}
	
	// TODO: Add this function to interface
	public void closeDatabase() {
		if (database != null) {
			Log.d("debug", "Database closed:" + DATABASE_NAME);
			database.close();
		}
	}

	private String readCursor(Cursor cursor) {
		String result = "";

		int indexProductCode = cursor.getColumnIndex(WomanShopDataDef.PRODUCT_CODE.name());
		int indexProductCategory = cursor.getColumnIndex(WomanShopDataDef.PRODUCT_CATEGORY.name());
		int indexItemCategory = cursor.getColumnIndex(WomanShopDataDef.ITEM_CATEGORY.name());
		int indexCostToEntrepreneur = cursor.getColumnIndex(WomanShopDataDef.COST_TO_ENTREPRENEUR.name());
		int indexSalePrice = cursor.getColumnIndex(WomanShopDataDef.SALE_PRICE.name());

		if (cursor.moveToNext()) {
			String productCode = cursor.getString(indexProductCode);
			String productCategory = cursor.getString(indexProductCategory);
			String itemCategory = cursor.getString(indexItemCategory);
			double salePrice = cursor.getDouble(indexSalePrice);
			double costToEntrepreneur = cursor.getDouble(indexCostToEntrepreneur);

			result += productCode + ":" + itemCategory + ":" + productCategory + ":"
					+ costToEntrepreneur + ":" + salePrice + ":" + "\n";
		}
		return result;
	}
	
	public String getCSVStoragePath() {
		File exterlStorage = Environment.getExternalStorageDirectory();
		Log.d("debug", "Environment External:" + exterlStorage.getAbsolutePath());
		return exterlStorage.getAbsolutePath() + csvStorageFolder;
	}
}
