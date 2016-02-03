package com.ricoh.pos;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

public class SearchBoxWatcher implements TextWatcher {

    private String latestItemId;
    private String latestSearchWord;
    private ActivityName activityName;
    private FragmentManager fragmentManager;

    SearchBoxWatcher(String latestItemId, String latestSearchWord, ActivityName activityName, FragmentManager fragmentManager) {
        this.latestItemId = latestItemId;
        this.latestSearchWord = latestSearchWord;
        this.fragmentManager = fragmentManager;
        this.activityName = activityName;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.d("beforeTextChanged", "beforeTextChanged");
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        latestSearchWord = s.toString();
        Bundle arguments = new Bundle();
        arguments.putString(GetProductFragment.ARG_ITEM_ID, latestItemId);
        arguments.putString(GetProductFragment.ARG_SEARCH_WORD, latestSearchWord);

        if (ActivityName.EDIT_ACTIVITY == activityName) {

            ProductFragment fragment = new ProductFragment();
            fragment.setArguments(arguments);
            fragmentManager.beginTransaction()
                    .replace(R.id.product_list, fragment).commit();
        } else if (ActivityName.CATEGORY_LIST_ACTIVITY == activityName) {
            CategoryDetailFragment fragment = new CategoryDetailFragment();
            fragment.setArguments(arguments);
            fragmentManager.beginTransaction()
                    .replace(R.id.category_detail_container, fragment).commit();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.d("afterTextChanged", "afterTextChanged");
    }
}
