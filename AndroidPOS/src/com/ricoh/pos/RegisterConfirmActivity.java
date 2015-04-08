package com.ricoh.pos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.ricoh.pos.data.SingleSalesRecord;
import com.ricoh.pos.model.RegisterManager;
import com.ricoh.pos.model.SalesRecordManager;

public class RegisterConfirmActivity extends FragmentActivity
implements RegisterConfirmFragment.OnButtonClickListener,OrderListFragment.OnOrderClickListener{
	
	private SalesDatabaseHelper salesDatabaseHelper;
	private static SQLiteDatabase salesDatabase;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_confirm);
        Log.d("RegisterConfirmActivity","onCreate");
		if (findViewById(R.id.order_list_container) != null) {
			// add OrderListFragment
            // 合計画面の上の段
			OrderListFragment fragment = new OrderListFragment();
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.order_list_container, fragment).commit();

			// add RegisterConfirmFragment
			RegisterConfirmFragment registerConfirmFragment = new RegisterConfirmFragment();
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.register_confirm_container, registerConfirmFragment).commit();
		}
		
		salesDatabaseHelper = new SalesDatabaseHelper(this);
		salesDatabase = salesDatabaseHelper.getWritableDatabase();
	}

	@Override
	public void onOkClicked() {
		Log.d("RegisterComfurmActivity","onOkClicked");
		// Save this sales record
		SingleSalesRecord record = RegisterManager.getInstance().getSingleSalesRecord();
		SalesRecordManager.getInstance().storeSingleSalesRecord(salesDatabase, record);
		
		// Clear this record
		RegisterManager.getInstance().clearAllOrders();
		
		// Go to the CategoryListActivity
		Intent intent = new Intent(this, CategoryListActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@Override
	public void onCancelClicked() {
		RegisterManager.getInstance().updateDiscountValue(0);
		finish();
	}

	@Override
	public void onPriceDownClicked() {
		showPriceDownDialog();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		salesDatabase.close();
		salesDatabaseHelper.close();
		Log.d("debug", "Exit RegisterConfirmActivity onDestroy");
	}

	private void showPriceDownDialog()
	{
		PriceDownDialog dialog = new PriceDownDialog();
		dialog.show(this);
	}

	@Override
	public void onOrderClicked() {
		Log.d("RegisterCom","onOrderClicked");
	}

	public static class ChangeSalesDialog extends DialogFragment {
		private String messageId;

		public void setMessageId(String messageId) {
			this.messageId = messageId;
		}

		public static ChangeSalesDialog newInstance(String messageId) {
			ChangeSalesDialog frag = new ChangeSalesDialog();
			frag.setMessageId(messageId);
			return frag;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

			builder.setTitle("Change");
			builder.setMessage(messageId);
			builder.setCancelable(false);
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					getActivity().finish();
				}
			});

			AlertDialog dialog = builder.create();
			dialog.setCanceledOnTouchOutside(false);
			setCancelable(false);

			return dialog;
		}
	}
}
