package com.ricoh.pos;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A fragment representing a single Category detail screen. This fragment is
 * either contained in a {@link CategoryListActivity} in two-pane mode (on
 * tablets) or a {@link CategoryDetailActivity} on handsets.
 */
public class CategoryDetailFragment extends ListFragment {
	
	
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
	
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public CategoryDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
	            return itemPhotoList.length;
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
	            
	            ImageView imageView = (ImageView) convertView.findViewById(R.id.photo);
	            imageView.setLayoutParams(new LinearLayout.LayoutParams(120, 120));
	            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
	            imageView.setImageResource(getResourceID(itemPhotoList[position]));
	            
	            TextView textView = (TextView) convertView.findViewById(R.id.filename);
	            textView.setPadding(10, 0, 0, 0);
	            textView.setText(itemList[position]);
	            
	            return convertView; 
	        }
	    }
	 
	    private int getResourceID(String fileName) {
	        int resID = getResources().getIdentifier(fileName
	            , "drawable", "com.ricoh.pos");
	        return resID;
	    }


}
