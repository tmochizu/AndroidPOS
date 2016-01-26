
package com.ricoh.pos;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;


public class EditActivity extends FragmentActivity implements
        CategoryListFragment.Callbacks {

    private String latestItemId;
    private String latestSearchWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        latestItemId = getString(R.string.category_title_default);

        ((CategoryListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.category_list_2))
                .setActivateOnItemClick(true);

        Bundle arguments = new Bundle();
        arguments.putString(ProductFragment.ARG_ITEM_ID, getString(R.string.category_title_default));
        final ProductFragment fragment = new ProductFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.product_list, fragment).commit();

        Log.d("CategoryDetailActivity", "createEditText");
        final EditText editText = (EditText) findViewById(R.id.id_search_text_box_2);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("beforeTextChanged", "beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                latestSearchWord = s.toString();
                Bundle arguments = new Bundle();
                arguments.putString(ProductFragment.ARG_ITEM_ID, latestItemId);
                arguments.putString(ProductFragment.ARG_SEARCH_WORD, latestSearchWord);
                ProductFragment fragment = new ProductFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.product_list, fragment).commit();
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("afterTextChanged", "afterTextChanged");
            }
        });
    }

    @Override
    public void onItemSelected(String id) {
        latestItemId = id;
        Bundle arguments = new Bundle();
        arguments.putString(ProductFragment.ARG_ITEM_ID, id);
        arguments.putString(ProductFragment.ARG_SEARCH_WORD, latestSearchWord);
        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.product_list, fragment).commit();
    }
}
