package com.ricoh.pos;

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
		
		SalesRecordDetailFragment fragment = new SalesRecordDetailFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.salesrecord_detail_container, fragment).commit();
				
	}

	@Override
	public void onItemLongSelected(String id) {
		showDeleteDialog(id);
	}
	
	private void showDeleteDialog(String date) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);  
        alert.setTitle("Delete");  
        alert.setMessage("Do you delete the date " + date + " ?");  
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener(){  
            public void onClick(DialogInterface dialog, int which) {  
                Toast.makeText(SalesRecordListActivity.this, "Deleted!", Toast.LENGTH_LONG).show();  
            }});  
        alert.setNegativeButton("No", new DialogInterface.OnClickListener(){  
            public void onClick(DialogInterface dialog, int which) {  
            }});  
        alert.show(); 
	}
}
