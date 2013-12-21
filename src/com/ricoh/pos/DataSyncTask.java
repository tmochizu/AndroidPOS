package com.ricoh.pos;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class DataSyncTask extends AsyncTask<String, Void, AsyncTaskResult<String>> {
	final String TAG = "DataSyncTask";
	DataSyncTaskCallback callback;
	ProgressDialog progressDialog;

	public DataSyncTask(Context context, DataSyncTaskCallback callback) {
		this.callback = callback;
		
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Synchronizing, please wait...");
			progressDialog.show();
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
		} 
	}

	@Override
	protected AsyncTaskResult<String> doInBackground(String... params) {
		Log.d(TAG, "doInBackground");
		try {
			//TODO: Should import & export data
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			//TODO: Should separate exception(Import, Export, at least)
			return AsyncTaskResult.createErrorResult(R.string.sd_import_error);
		}
		return AsyncTaskResult.createNormalResult(null);
	}      

	@Override
	protected void onPostExecute(AsyncTaskResult<String> result) {
		Log.d(TAG, "onPostExecute");
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();

			if (result.isError()) {
				callback.onFailedSyncData(result.getResourceId());
			}
		}            
	}
}