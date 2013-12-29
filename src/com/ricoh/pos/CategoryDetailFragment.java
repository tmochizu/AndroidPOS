package com.ricoh.pos;

import java.util.ArrayList;

import com.ricoh.pos.data.Product;
import com.ricoh.pos.model.ProductsManager;
import com.ricoh.pos.model.RegisterManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A fragment representing a single Category detail screen. This fragment is
 * either contained in a {@link CategoryListActivity} in two-pane mode (on
 * tablets) or a {@link CategoryDetailActivity} on handsets.
 */
public class CategoryDetailFragment extends ListFragment {
	
	/*
    private final String[] itemList
    = { "sample021"
      , "sample022"
      , "sample023"
      , "sample024"
      , "sample025"
      , "sample026"
      , "sample027"
      , "sample028"
      , "sample029"
      , "sample030"
      , "sample031"
      , "sample032"
      , "sample033"
      , "sample034"
      , "sample035"
      , "sample036"
      , "sample037"};
    
    private final String[] itemPhotoList
    = { "sample021"
      , "sample022"
      , "sample023"
      , "sample024"
      , "sample025"
      , "sample026"
      , "sample027"
      , "sample028"
      , "sample029"
      , "sample030"
      , "sample031"
      , "sample032"
      , "sample033"
      , "sample034"
      , "sample035"
      , "sample036"
      , "sample037"};
      */
	
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";
	
	private RegisterManager registerManager;
	
	private ArrayList<Product> productList;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public CategoryDetailFragment() {
		this.registerManager = new RegisterManager();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		productList = ProductsManager.getInstance().getAllProducts();
		
		setListAdapter(new ListAdapter(getActivity()));

	}

	
	 public class ListAdapter extends BaseAdapter {
	        //private Context contextInAdapter;
	        private LayoutInflater inflater;
	        
	        public ListAdapter(Context context) {
	            //contextInAdapter = context;
	            inflater = (LayoutInflater) context.getSystemService(
	                Context.LAYOUT_INFLATER_SERVICE);
	        }
	        
	        @Override
	        public int getCount() {
	        	return productList.size();
	            //return itemPhotoList.length;
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
	           
	            if(convertView == null){  
	                convertView = inflater.inflate(R.layout.row, null);  
	            }
	            
	            Product product = productList.get(position);
	            
	            ImageView imageView = (ImageView) convertView.findViewById(R.id.photo);
	            imageView.setLayoutParams(new LinearLayout.LayoutParams(120, 120));
	            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
	            imageView.setImageResource(getResourceID(product.getProductImagePath()));
	            
	            TextView textView = (TextView) convertView.findViewById(R.id.filename);
	            textView.setPadding(10, 0, 0, 0);
	            textView.setText("Price");
	            //textView.setText(itemList[position]);
	            
	            TextView priceView = (TextView) convertView.findViewById(R.id.price);
	            priceView.setPadding(10, 0, 0, 0);
	            priceView.setText("Initial Cost");
	            
	            ProductEditText numberOfSalesText = (ProductEditText) convertView.findViewById(R.id.numberOfSales);
	            numberOfSalesText.setProductId(position);
	            numberOfSalesText.setInputType(InputType.TYPE_CLASS_NUMBER);
	            numberOfSalesText.addTextChangedListener(new NumberOfSalesWatcher(numberOfSalesText));

	            return convertView; 
	        }

		    private int getResourceID(String fileName) {
		        int resID = getResources().getIdentifier(fileName
		            , "drawable", "com.ricoh.pos");
		        return resID;
		    }
	 }

	 public class NumberOfSalesWatcher implements TextWatcher{
		 
		 private View textView;
		 
		 public NumberOfSalesWatcher(View view){
			 this.textView = view;
		 }

		@Override
		public void afterTextChanged(Editable s) {
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

			/*
			int productId = textView.getId();
			Product product = ProductsManager.getInstance().getProductFromId(productId);
			registerManager.updateOrder(product, Integer.parseInt(s.toString()));
			*/
		}
		 
	 }

}
