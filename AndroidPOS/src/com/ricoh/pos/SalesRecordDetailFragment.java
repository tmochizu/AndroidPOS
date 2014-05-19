package com.ricoh.pos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ricoh.pos.data.Order;
import com.ricoh.pos.data.Product;
import com.ricoh.pos.data.SingleSalesRecord;
import com.ricoh.pos.model.SalesCalenderManager;
import com.ricoh.pos.model.SalesRecordManager;

public class SalesRecordDetailFragment extends ListFragment {
	// This is the maximum fraction digits for total payment to display.
	private static final int MAXIMUM_FRACTION_DIGITS = 2;
	private ArrayList<Order> orders;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Date date = SalesCalenderManager.getInstance().getSelectedSalesDate();
		SingleSalesRecord salesRecord;
		if (date == null) {
			date = SalesCalenderManager.getInstance().getSelectedDate();
			salesRecord = SalesRecordManager.getInstance().restoreSingleSalesRecordsOfTheDay(date).get(0);
		} else {
			salesRecord = SalesRecordManager.getInstance().getSingleSalesRecord(date);
		}

		orders = salesRecord.getAllOrders();
		
		setListAdapter(new ListAdapter(getActivity()));
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	public class ListAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public ListAdapter(Context context) {
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return orders.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.order_row, null);
			}
			
			Order order = orders.get(position);
			Product product = order.getProduct();

			ImageView imageView = (ImageView) convertView.findViewById(R.id.photo);
			imageView.setLayoutParams(new LinearLayout.LayoutParams(120, 120));
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			setImageView(product, imageView);

			TextView textView = (TextView) convertView.findViewById(R.id.filename);
			textView.setPadding(10, 0, 0, 0);
			String productName = product.getName();
			if (productName == null || productName.length() == 0) {
				throw new NullPointerException("Product name is not valid");
			}
			textView.setText(productName);

			TextView priceView = (TextView) convertView.findViewById(R.id.price);
			priceView.setPadding(10, 0, 0, 0);
			NumberFormat.getInstance().setMaximumFractionDigits(MAXIMUM_FRACTION_DIGITS);
			priceView.setText(NumberFormat.getInstance().format(product.getPrice()));

			TextView numberOfSalseView = (TextView) convertView.findViewById(R.id.numberOfSales);
			numberOfSalseView.setPadding(10, 0, 0, 0);

			if (order == null || order.getNumberOfOrder() == 0) {
				throw new AssertionError("Product which isn't ordered is shown");
			} else {
				int numberOfSales = order.getNumberOfOrder();
				numberOfSalseView.setText(NumberFormat.getInstance().format(numberOfSales));
			}

			return convertView;
		}
		
		private void setImageView(Product product, ImageView imageView) {
			File imageFile = new File(product.getProductImagePath());
			try {
				InputStream inputStream = new FileInputStream(imageFile);
				Bitmap tmpImage = BitmapFactory.decodeStream(inputStream);
				imageView.setImageBitmap(tmpImage);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

}
