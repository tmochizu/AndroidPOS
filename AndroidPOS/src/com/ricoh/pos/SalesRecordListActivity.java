package com.ricoh.pos;

import com.ricoh.pos.model.WomanShopSalesIOManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class SalesRecordListActivity extends FragmentActivity implements
SalesRecordListFragment.Callbacks{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_salesrecord_list);

		if (findViewById(R.id.salesrecord_list) != null) {
			
			((SalesRecordListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.salesrecord_list))
					.setActivateOnItemClick(true);
		}
		
		SalesRecordDetailFragment fragment = new SalesRecordDetailFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.salesrecord_detail_container, fragment).commit();
		
		// add OneDeySalesFragment
		OneDaySalesFragment oneDaySalesFragment = new OneDaySalesFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.oneday_sales_container, oneDaySalesFragment).commit();

		// TODO: If exposing deep links into your app, handle intents here.
	}

	/**
	 * Callback method from {@link CategoryListFragment.Callbacks} indicating
	 * that the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		replaceFragment();
	}

	/**
	 * Callback method from {@link CategoryListFragment.Callbacks} indicating
	 * that the item with the given ID was long selected.
	 */
	@Override
	public void onItemLongSelected(String id) {
		showDeleteDialog(id);
		replaceFragment();
	}
	
	private void replaceFragment() {
		SalesRecordDetailFragment fragment = new SalesRecordDetailFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.salesrecord_detail_container, fragment).commit();
	}
	
	private void showDeleteDialog(final String date) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);  
        alert.setTitle(R.string.title_delete);  
        alert.setMessage("Do you delete the following date?\n" + date);  
        alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener(){  
            public void onClick(DialogInterface dialog, int which) {
            	WomanShopSalesIOManager.getInstance().deleteSingleSalesRecordRelatedTo(date);
                Toast.makeText(SalesRecordListActivity.this, R.string.success_deleted_message, Toast.LENGTH_LONG).show(); 
            }});  
        alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){  
            public void onClick(DialogInterface dialog, int which) {  
            }});  
        alert.show(); 
	}
}
