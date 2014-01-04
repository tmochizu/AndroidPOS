package com.ricoh.pos;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class RegisterConfirmActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm);
		if (findViewById(R.id.discount_list_container) != null) {
			// add CategoryDetailFragment
			Bundle arguments = new Bundle();
			arguments.putString(DiscountListFragment.ARG_ITEM_ID, getString(R.string.category_title_default));
			DiscountListFragment fragment = new DiscountListFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.discount_list_container, fragment).commit();

			// add RegisterConfirmFragment
			RegisterConfirmFragment confirmFragment = new RegisterConfirmFragment();
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.confirm_container, confirmFragment).commit();
		}
	}
}
