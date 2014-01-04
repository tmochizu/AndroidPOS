package com.ricoh.pos;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class RegisterConfirmActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm);
		if (findViewById(R.id.category_detail_container) != null) {
			Bundle arguments = new Bundle();
			arguments.putString(CategoryDetailFragment.ARG_ITEM_ID, getString(R.string.category_title_default));
			CategoryDetailFragment fragment = new CategoryDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.category_detail_container, fragment).commit();

			// add RegisterConfirmFragment
			RegisterConfirmFragment confirmFragment = new RegisterConfirmFragment();
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.confirm_container, confirmFragment).commit();
		}
	}
}
