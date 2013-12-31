package com.ricoh.pos;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainMenuActivity extends Activity implements DataSyncTaskCallback{

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
				DataSyncTask syncTask = new DataSyncTask(MainMenuActivity.this,
						MainMenuActivity.this,
						new WSIOManager(),
						database);
				syncTask.execute();
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

	@Override
	public void onSuccessSyncData() {
		Toast.makeText(this, R.string.sync_success, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onFailedSyncData(int resId) {
		Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
	}
}
