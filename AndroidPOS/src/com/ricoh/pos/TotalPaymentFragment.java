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
	
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_total_payment, container, false);
        return v;
    }

	@Override
	public void onStart() {
		super.onStart();

		RegisterManager.getInstance().setListener(this);
	}
    
	@Override
	public void notifyUpdateOrder(double totalPayment) {
		TextView totalPaymentView = (TextView) getView().findViewById(R.id.totalPaymentView);
		
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(2);
		totalPaymentView.setText(format.format(totalPayment) + " Rp");
	}

}
