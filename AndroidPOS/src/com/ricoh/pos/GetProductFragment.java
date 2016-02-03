package com.ricoh.pos;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.WindowManager;

import com.ricoh.pos.data.Product;
import com.ricoh.pos.model.ProductsManager;
import com.ricoh.pos.model.RegisterManager;

import java.util.ArrayList;

public class GetProductFragment extends ListFragment {

    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_SEARCH_WORD = "serch_word";
    private String category;
    private ArrayList<Product> productList;
    protected ArrayList<Product> searchProductList;
    // This is the maximum fraction digits for total payment to display.
    protected static final int MAXIMUM_FRACTION_DIGITS = 2;
    protected final int IMAGE_VIEW_SIZE = 120;
    protected RegisterManager registerManager;

    public GetProductFragment() {
        this.registerManager = RegisterManager.getInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        this.category = bundle.getString(this.ARG_ITEM_ID);
        String searchWord = bundle.getString(this.ARG_SEARCH_WORD);

        if (category.equals(getString(R.string.category_title_default))) {
            productList = ProductsManager.getInstance().getAllProducts();
        } else {
            productList = ProductsManager.getInstance().getProductsInCategory(category);
        }

        if (null != searchWord && !searchWord.isEmpty()) {
            searchProductList = new ArrayList<Product>();
            for (Product product : productList) {
                //TODO ここがうまくいったら、containsを使う。ほかのクラスでindexOfを使っている箇所がある
                if (product.getName().toUpperCase().contains(searchWord.toUpperCase())) {
                    searchProductList.add(product);
                }
            }
        } else {
            //FIXME 変数名を直す。
            this.searchProductList = productList;
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }
}
