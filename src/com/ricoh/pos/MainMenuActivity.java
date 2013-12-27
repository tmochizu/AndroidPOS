package com.ricoh.pos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class MainMenuActivity extends Activity {

	private DatabaseHelper databaseHelper;
	public static SQLiteDatabase database;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);

		databaseHelper = new DatabaseHelper(this);
		database = databaseHelper.getWritableDatabase();

		findViewById(R.id.RegisterButton).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent().setClass(
								MainMenuActivity.this,
								CategoryListActivity.class);
						startActivity(intent);
					}
				});

		findViewById(R.id.SalesButton).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent().setClass(
								MainMenuActivity.this,
								SalesCalenderActivity.class);
						startActivity(intent);
					}
				});

		findViewById(R.id.SyncButton).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("debug", "SyncButton click");
				insertRecords();

				// TODO: Read test
				Log.d("debug", searchByID(20));
			}

			private void insertRecords() {
				// TODO: This assetManager is Temporary
				AssetManager assetManager = getResources().getAssets();

				ContentValues contentValue = new ContentValues();
				try {
					// TODO: Should import & export data
					BufferedReader bufferReader = new BufferedReader(
							new InputStreamReader(assetManager
									.open("products.csv")));

					String record = bufferReader.readLine();
					String[] fieldNames = record.split(",");
					for (String fieldName : fieldNames) {
						Log.d("debug", fieldName);
					}

					while ((record = bufferReader.readLine()) != null) {
						String[] fields = record.split(",");
						Log.d("debug", "S No." + fields[0]);

						contentValue.put("SNo", fields[0]);
						contentValue.put("ProductID", fields[1]);
						contentValue.put("Category", fields[2]);
						contentValue.put("OfficeName", fields[3]);
						contentValue.put("ProductName", fields[4]);
						contentValue.put("PricePiece", fields[5]);
						contentValue.put("NoOfPieces", fields[6]);
						contentValue.put("PriceBox", fields[7]);
						contentValue.put("TaxType", fields[8]);
						contentValue.put("TaxPercentage", fields[9]);

						database.insert("Products", null, contentValue);
					}
				} catch (IOException e) {
					Log.d("debug", "" + e + "");
				}
			}

			// TODO: temporary function
			private String searchByID(int id) {
				Cursor cursor = null;
				try {
					cursor = database.query("Products", new String[] { "SNo",
							"ProductID", "Category", "OfficeName",
							"ProductName", "PricePiece", "NoOfPieces",
							"PriceBox", "TaxType", "TaxPercentage" },
							"ProductID = ?", new String[] { "" + id }, null,
							null, null);
					return readCursor(cursor);
				} finally {
					if (cursor != null) {
						cursor.close();
					}
				}
			}

			// TODO: temporary function
			private String readCursor(Cursor cursor) {
				String result = "";

				int indexId = cursor.getColumnIndex("ProductID");
				int indexProductName = cursor.getColumnIndex("ProductName");

				while (cursor.moveToNext()) {
					int id = cursor.getInt(indexId);
					String productName = cursor.getString(indexProductName);
					result += id + ":" + productName + "\n";
				}
				return result;
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		database.close();
		databaseHelper.close();
		Log.d("debug", "Exit onDestroy");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

}
