package com.ricoh.pos;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.ricoh.pos.model.ProductsManager;
import com.ricoh.pos.model.WomanShopIOManager;
import com.ricoh.pos.model.WomanShopSalesIOManager;

public class MainMenuActivity extends Activity implements DataSyncTaskCallback {

	private WomanShopIOManager womanShopIOManager;
	private DatabaseHelper databaseHelper;
	private SalesDatabaseHelper salesDatabaseHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);

		womanShopIOManager = new WomanShopIOManager();
		databaseHelper = new DatabaseHelper(this);
		womanShopIOManager.setDatabase(databaseHelper.getWritableDatabase());
		
		salesDatabaseHelper = new SalesDatabaseHelper(this);
		WomanShopSalesIOManager.getInstance().setDatabase(salesDatabaseHelper.getWritableDatabase());

		findViewById(R.id.RegisterButton).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent().setClass(MainMenuActivity.this,
						CategoryListActivity.class);
				startActivity(intent);
			}
		});
		setRegisterButtonEnabled();

		findViewById(R.id.SalesButton).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent().setClass(MainMenuActivity.this,
						SalesCalenderActivity.class);
				startActivity(intent);
			}
		});

		findViewById(R.id.SyncButton).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DataSyncTask syncTask = new DataSyncTask(MainMenuActivity.this,
						MainMenuActivity.this, womanShopIOManager);
				syncTask.execute();
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		womanShopIOManager.closeDatabase();
		WomanShopSalesIOManager.getInstance().closeDatabase();
		databaseHelper.close();
		salesDatabaseHelper.close();
		WomanShopSalesIOManager.resetInstance();
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
		setRegisterButtonEnabled();
		
		// TODO: For debug
		try {
			Log.d("debug", loadText("sales_dummy.csv"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void onFailedSyncData(int resId) {
		Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
	}

	private void setRegisterButtonEnabled() {
		if (ProductsManager.getInstance().getCategoryCount() > 0) {
			findViewById(R.id.RegisterButton).setEnabled(true);
		} else {
			findViewById(R.id.RegisterButton).setEnabled(false);
		}
	}
	
	// TODO For debug
	private String loadText(String fileName) throws IOException {
		FileInputStream input = this.openFileInput(fileName);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		StringBuffer strBuffer = new StringBuffer();
		String line;
		while ((line = reader.readLine()) != null) {
			strBuffer.append(line);
			strBuffer.append("\n");
		}
		reader.close();
		
		return strBuffer.toString();
	}
}
