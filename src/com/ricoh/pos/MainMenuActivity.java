package com.ricoh.pos;

import java.io.BufferedReader;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import com.ricoh.pos.model.IOManager;
import com.ricoh.pos.model.ProductsManager;
import com.ricoh.pos.model.WSIOManager;

public class MainMenuActivity extends Activity {

	private ProductsManager productsManager;
	private DatabaseHelper databaseHelper;
	private IOManager wsIOManager;
	public static SQLiteDatabase database;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);

		productsManager = ProductsManager.getInstance();
		databaseHelper = new DatabaseHelper(this);
		database = databaseHelper.getWritableDatabase();
		wsIOManager = new WSIOManager();

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

				AssetManager assetManager = getResources().getAssets();
				BufferedReader bufferReader = wsIOManager
						.importCSVfromAssets(assetManager);
				if (bufferReader == null) {
					Log.d("debug", "File not found");
					return;
				}
				wsIOManager.insertRecords(database, bufferReader);

				// TODO: Read test
				Log.d("debug", wsIOManager.searchByID(database, 20));

				String results[] = wsIOManager.searchAlldata(database);
				for (String result : results) {
					Log.d("debug", result);
				}
				productsManager.createProducts(results);

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
