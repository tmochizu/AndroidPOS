package com.ricoh.pos;

import java.text.NumberFormat;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ricoh.pos.model.RegisterManager;
import com.ricoh.pos.model.UpdateOrderListener;

public class RegisterConfirmFragment extends Fragment implements UpdateOrderListener{
	// This is the maximum fraction digits for total payment to display.
	private static final int MAXIMUM_FRACTION_DIGITS = 2;
	private OnButtonClickListener buttonClickListener; 

	@Override  
	public void onAttach(Activity activity) {  
		super.onAttach(activity); 
		if (activity instanceof OnButtonClickListener == false) {  
			throw new ClassCastException("okButtonClickListener isn't implemented");  
		}  
		buttonClickListener = (OnButtonClickListener) activity;  
	}
	
	@Override  
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RegisterManager.getInstance().setUpdateOrderListener(this);
	}

	@Override  
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_register_confirm, container, false);
		
		double totalPayment = RegisterManager.getInstance().getTotalAmount();
		setTotalPayment(v, totalPayment);
		
		Button price_down_button = (Button) v.findViewById(R.id.price_down_button);
		price_down_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (buttonClickListener != null) {  
					buttonClickListener.onPriceDownClicked();  
				}
			}
		});

		Button ok_button = (Button) v.findViewById(R.id.ok_button);
		ok_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (buttonClickListener != null) {  
					buttonClickListener.onOkClicked();  
				}
			}
		});
		
		Button cancel_button = (Button) v.findViewById(R.id.cancel_button);
		cancel_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (buttonClickListener != null) {  
					buttonClickListener.onCancelClicked();  
				}
			}
		});
		return v;
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		//TODO: should not reset listeners. Remove only this.
		RegisterManager.getInstance().clearUpdateOrderListener();
	}

	@Override
	public void notifyUpdateOrder(double totalPayment) {
		setTotalPayment(getView(), totalPayment);
	}
	
	private void setTotalPayment(View view, double totalPayment)
	{
		TextView totalPaymentView = (TextView) view.findViewById(R.id.totalPaymentView);

		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(MAXIMUM_FRACTION_DIGITS);
		totalPaymentView.setText(format.format(totalPayment) + " Rp");
	}

	public interface OnButtonClickListener { 
		public void onPriceDownClicked();
		public void onOkClicked(); 
		public void onCancelClicked(); 
	}
}
