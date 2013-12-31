package com.ricoh.pos;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class TotalPaymentActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_total_payment);

		/*
		if (findViewById(R.id.total_payment_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((CategoryListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.total_payment_container))
					.setActivateOnItemClick(true);
		}*/

		if (savedInstanceState == null) {

			TotalPaymentFragment fragment = new TotalPaymentFragment();
			getSupportFragmentManager().beginTransaction()
					.add(R.id.total_payment_container, fragment).commit();
		}
		
		// TODO: If exposing deep links into your app, handle intents here.
	}
	
}
