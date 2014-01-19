package com.ricoh.pos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.ricoh.pos.data.SingleSalesRecord;
import com.ricoh.pos.model.RegisterManager;
import com.ricoh.pos.model.SalesRecordManager;

public class RegisterConfirmActivity extends FragmentActivity
implements RegisterConfirmFragment.OnButtonClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_confirm);
		if (findViewById(R.id.order_list_container) != null) {
			// add OrderListFragment
			OrderListFragment fragment = new OrderListFragment();
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.order_list_container, fragment).commit();

			// add RegisterConfirmFragment
			RegisterConfirmFragment registerConfirmFragment = new RegisterConfirmFragment();
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.register_confirm_container, registerConfirmFragment).commit();
		}
	}

	@Override
	public void onOkClicked() {
		Intent intent = new Intent(this, CategoryListActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		
		// Save this sales record
		SingleSalesRecord record = RegisterManager.getInstance().getSingleSalesRecord();
		SalesRecordManager.getInstance().storeSingleSalesRecord(record);
		
		// Clear this record
		RegisterManager.getInstance().clearAllOrders();
		
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

	private void showPriceDownDialog()
	{
		PriceDownDialog dialog = new PriceDownDialog();
		dialog.show(this);
	}
}
