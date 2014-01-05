package com.ricoh.pos;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

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
		//TODO: Not implemented
	}

	@Override
	public void onCancelClicked() {
		//TODO: Not implemented
	}
}
