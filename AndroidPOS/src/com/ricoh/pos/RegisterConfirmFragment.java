package com.ricoh.pos;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.ricoh.pos.model.RegisterManager;

public class RegisterConfirmFragment extends Fragment{
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
}
