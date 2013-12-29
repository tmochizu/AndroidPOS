package com.ricoh.pos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainMenuActivity extends Activity implements DataSyncTaskCallback{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);

		findViewById(R.id.RegisterButton).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent().setClass(MainMenuActivity.this, CategoryListActivity.class);
				startActivity(intent);
			}
		});

		findViewById(R.id.SyncButton).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				DataSyncTask syncTask = new DataSyncTask(MainMenuActivity.this, MainMenuActivity.this);
				syncTask.execute();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public void onSuccessSyncData() {
		//TODO: Do nothing
	}

	@Override
	public void onFailedSyncData(int resId) {
		Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
	}
}
