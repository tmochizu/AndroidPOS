package com.ricoh.pos;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class DataSyncTask extends AsyncTask<String, Void, AsyncTaskResult<String>> {
	final String TAG = "DataSyncTask";
	DataSyncTaskCallback callback;
	Context context;
	ProgressDialog progressDialog;

	public DataSyncTask(Context context, DataSyncTaskCallback callback) {
		this.callback = callback;
		this.context = context;
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
			//TODO: Should import & export data
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
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
			}
		}            
	}
}