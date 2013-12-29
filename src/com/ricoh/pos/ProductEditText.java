package com.ricoh.pos;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class ProductEditText extends EditText {

	private int productId;
	
	public ProductEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public void setProductId(int id){
		
		if (id < 0) {
			throw new IllegalArgumentException("Product ID should be positive");
		}
		
		this.productId = id;
	}

	public int getProductId(){
		return this.productId;
	}
}
