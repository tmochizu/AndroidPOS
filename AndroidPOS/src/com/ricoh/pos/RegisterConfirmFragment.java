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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_register_confirm, container, false);
		
		TextView totalPaymentView = (TextView) v.findViewById(R.id.totalPaymentView);
		RegisterManager registerManager = RegisterManager.getInstance();
		double totalPayment = registerManager.getTotalAmount();
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(MAXIMUM_FRACTION_DIGITS);
		totalPaymentView.setText(format.format(totalPayment) + " Rp");

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
		RegisterManager.getInstance().clearUpdateOrderListener();
	}

	public interface OnButtonClickListener {  
		public void onOkClicked(); 
		public void onCancelClicked(); 
	}

	@Override
	public void notifyUpdateOrder(double totalPayment) {
		TextView totalPaymentView = (TextView) getView().findViewById(R.id.totalPaymentView);

		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(MAXIMUM_FRACTION_DIGITS);
		totalPaymentView.setText(format.format(totalPayment) + " Rp");
	}  
}
