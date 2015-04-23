package com.ricoh.pos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.ricoh.pos.data.Order;
import com.ricoh.pos.data.Product;
import com.ricoh.pos.data.SingleSalesRecord;
import com.ricoh.pos.model.ProductsManager;
import com.ricoh.pos.model.RegisterManager;
import com.ricoh.pos.model.SalesRecordManager;

import java.io.FileNotFoundException;
import java.text.NumberFormat;

public class RegisterConfirmActivity extends FragmentActivity
implements RegisterConfirmFragment.OnButtonClickListener,OrderListFragment.OnOrderClickListener{
	
	private SalesDatabaseHelper salesDatabaseHelper;
	private static SQLiteDatabase salesDatabase;
	private RegisterManager registerManager;

	public RegisterConfirmActivity() {
		this.registerManager = RegisterManager.getInstance();
	}

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

		salesDatabaseHelper = new SalesDatabaseHelper(this);
		salesDatabase = salesDatabaseHelper.getWritableDatabase();
	}

	@Override
	public void onOkClicked() {
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
	public void onOrderClicked(Product product) {
		Order order = registerManager.findOrderOfTheProduct(product);
		EditSalesDialog dialog = EditSalesDialog.newInstance(product,order);
		dialog.show(getFragmentManager(), null);


	}

	public static class EditSalesDialog extends DialogFragment {
		private Product product;
		private Order order;
		private int orderNum = 0;

		public void setProduct(Product product) {
			this.product = product;
		}
		public void setOrder(Order order){this.order = order;}

		public static EditSalesDialog newInstance(Product product,Order order) {
			EditSalesDialog frag = new EditSalesDialog();
			frag.setProduct(product);
			frag.setOrder(order);
			return frag;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			Log.d("RegisterConfirmActivity","orderNum="+orderNum);

			final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

			LayoutInflater inflater = getActivity().getLayoutInflater();
			final View view = inflater.inflate(R.layout.change_order_num_dialog, null);
			builder.setView(view);

			ImageView imageView = (ImageView) view.findViewById(R.id.photo);
			imageView.setLayoutParams(new LinearLayout.LayoutParams(240, 240));
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			setImageView(product, imageView);

			TextView textView = (TextView) view.findViewById(R.id.filename);
			textView.setPadding(10, 0, 0, 0);
			String productName = product.getName();
			if (productName == null || productName.length() == 0) {
				throw new NullPointerException("Product name is not valid");
			}
			textView.setText(productName);

			TextView priceView = (TextView) view.findViewById(R.id.price);
			priceView.setPadding(10, 0, 0, 0);
			NumberFormat.getInstance().setMaximumFractionDigits(2);
			priceView.setText(NumberFormat.getInstance().format(product.getPrice()));

			builder.setTitle("Edit");
			builder.setCancelable(false);
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(orderNum > 0){
						new RegisterConfirmActivity().addProduct(orderNum,product);
					} else if(orderNum < 0){
						new RegisterConfirmActivity().removeProduct(orderNum,product);
					}
				}
			});


			final EditText numberOfSalesText = (EditText) view
					.findViewById(R.id.numberOfSales);
			numberOfSalesText.setInputType(InputType.TYPE_CLASS_NUMBER);
			numberOfSalesText.setImeOptions(EditorInfo.IME_ACTION_DONE);

			ProductButton plusBtn = (ProductButton) view.findViewById(R.id.plusButton);
			plusBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					NumberFormat.getInstance().setMaximumFractionDigits(2);
					numberOfSalesText.setText(String.valueOf(Integer.parseInt(numberOfSalesText.getText().toString()) + 1));
					orderNum++;
				}
			});

			ProductButton minusBtn = (ProductButton) view.findViewById(R.id.minusButton);
			minusBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					NumberFormat.getInstance().setMaximumFractionDigits(2);
					int i = Integer.parseInt(numberOfSalesText.getText().toString());
					if (i != 0) {
						numberOfSalesText.setText(String.valueOf(i - 1));
						orderNum--;
					}
				}
			});

			if (order == null || order.getNumberOfOrder() == 0) {
				numberOfSalesText.getEditableText().clear();
			} else {

				int numberOfSales = order.getNumberOfOrder();
				NumberFormat.getInstance().setMaximumFractionDigits(2);
				numberOfSalesText.setText(NumberFormat.getInstance().format(numberOfSales));
			}



			AlertDialog dialog = builder.create();

			return dialog;
		}

		private void setImageView(Product product, ImageView imageView) {

			try {
				Bitmap image = ProductsManager.getInstance().decodeProductImage(product, 240, 240);
				imageView.setImageBitmap(image);
				imageView.setVisibility(View.VISIBLE);
			} catch (FileNotFoundException e) {
				// プロダクトIDに対応する写真がない。ないことは許容されるので問題はない
				// ただしViewが再利用されるため、関係ない写真がViewに表示される可能性がある。
				// そのため写真がない場合はinvisibleにしている。
				// TODO: 裏ではメモリを余計に消費している可能性があるのでそれをクリアした方が本当は良いはず）
				imageView.setVisibility(View.INVISIBLE);
				//imageView.setImageBitmap(null);
			}
		}
	}

	private void addProduct(int num,Product product){
		for(int i=0; i<num;i++){
			registerManager.plusNumberOfOrder(product);
		}
		registerManager.notifyUpdateOrderList();

	}
	private void removeProduct(int num,Product product){
		for(int i=0; i>num;i--){
			registerManager.minusNumberOfOrder(product);
		}
		registerManager.notifyUpdateOrderList();
	}

}
