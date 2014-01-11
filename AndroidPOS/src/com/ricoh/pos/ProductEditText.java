package com.ricoh.pos;

import com.ricoh.pos.data.Product;
import com.ricoh.pos.model.UpdateOrderListener;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class ProductEditText extends EditText{

	private String category;
	private int productId;
	private Product product;
	
	public ProductEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public void setCategory(String category){
		
		if (category == null || category.length() == 0) {
			throw new IllegalArgumentException("Illegal category");
		}
		
		this.category = category;
	}
	
	public void setProduct(Product product) {
		
		if (product == null) {
			throw new IllegalArgumentException("Passing product is null");
		}
		
		this.product = product;
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
	
	public String getCategory(){
		return this.category;
	}
	
	public Product getProduct(){
		return this.product;
	}

}
