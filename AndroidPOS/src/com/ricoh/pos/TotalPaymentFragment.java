package com.ricoh.pos;

import java.text.NumberFormat;

import com.ricoh.pos.model.RegisterManager;
import com.ricoh.pos.model.UpdateOrderListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TotalPaymentFragment extends Fragment implements UpdateOrderListener{
	
	// This is the maximum fraction digits for total payment to display.
	private static final int MAXIMUM_FRACTION_DIGITS = 2;
	
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_total_payment, container, false);
        return v;
    }
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		RegisterManager.getInstance().setUpdateOrderListener(this);
	}
    
	@Override
	public void notifyUpdateOrder(double totalPayment) {
		TextView totalPaymentView = (TextView) getView().findViewById(R.id.totalPaymentView);
		
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(MAXIMUM_FRACTION_DIGITS);
		totalPaymentView.setText(format.format(totalPayment) + " Rp");
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		
		RegisterManager.getInstance().clearUpdateOrderListener();
	}

}
