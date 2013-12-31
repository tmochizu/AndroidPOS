package com.ricoh.pos;

import java.io.BufferedReader;

import com.ricoh.pos.model.IOManager;
import com.ricoh.pos.model.ProductsManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;
import android.database.sqlite.SQLiteDatabase;

public class DataSyncTask extends AsyncTask<String, Void, AsyncTaskResult<String>> {
	final String TAG = "DataSyncTask";
	DataSyncTaskCallback callback;
	Context context;
	ProgressDialog progressDialog;
	IOManager wsIOManager;
	SQLiteDatabase database;
	ProductsManager productsManager;

	public DataSyncTask(Context context,
			DataSyncTaskCallback callback,
			IOManager wsIOManager,
			SQLiteDatabase database) {
		this.callback = callback;
		this.context = context;
		this.wsIOManager = wsIOManager;
		this.database = database;
		this.productsManager = ProductsManager.getInstance();
	}
	
	@Override
	protected void onPreExecute()
	{
		Log.d(TAG, "onPreExecute");
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage(context.getString(R.string.dialog_data_syncro_message));
			progressDialog.show();
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
		} 
	}

	@Override
	protected AsyncTaskResult<String> doInBackground(String... params) {
		Log.d(TAG, "doInBackground");
		try {
			Log.d("debug", "SyncButton click");

			AssetManager assetManager = context.getResources().getAssets();
			BufferedReader bufferReader = wsIOManager
					.importCSVfromAssets(assetManager);
			if (bufferReader == null) {
				Log.d("debug", "File not found");
				return AsyncTaskResult.createErrorResult(R.string.sd_import_error);
			}
			wsIOManager.insertRecords(database, bufferReader);

			// TODO: Read test
			Log.d("debug", wsIOManager.searchByID(database, 20));
			
			String[] results = wsIOManager.searchAlldata(database);
			for (String result : results) {
				Log.d("debug", result);
			}
			productsManager.createProducts(results);
		} catch (Exception e) {
			//TODO: Should separate exception(Import, Export, at least)
			return AsyncTaskResult.createErrorResult(R.string.sd_import_error);
		}
		//The argument is null because nothing to notify on success
		return AsyncTaskResult.createNormalResult(null);
	}      

	@Override
	protected void onPostExecute(AsyncTaskResult<String> result) {
		Log.d(TAG, "onPostExecute");
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();

			if (result.isError()) {
				callback.onFailedSyncData(result.getResourceId());
			} else {
				callback.onSuccessSyncData();
			}
		}            
	}
}