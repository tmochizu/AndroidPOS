package com.ricoh.pos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class TotalPaymentActivity extends FragmentActivity implements TotalPaymentFragment.OnOkButtonClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_total_payment);

		if (savedInstanceState == null) {

			TotalPaymentFragment fragment = new TotalPaymentFragment();
			getSupportFragmentManager().beginTransaction()
			.add(R.id.total_payment_container, fragment).commit();
		}
		// TODO: If exposing deep links into your app, handle intents here.
	}

	@Override
	public void onOkClicked() {
		startActivity(new Intent(this, RegisterConfirmActivity.class));
	}
}
