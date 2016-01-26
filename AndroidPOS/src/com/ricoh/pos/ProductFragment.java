package com.ricoh.pos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ricoh.pos.data.Product;
import com.ricoh.pos.data.WomanShopDataDef;
import com.ricoh.pos.model.ProductsManager;
import com.ricoh.pos.model.RegisterManager;
import com.ricoh.pos.model.WomanShopIOManager;

import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.util.ArrayList;


public class ProductFragment extends ListFragment {

    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_SEARCH_WORD = "serch_word";
    private static final int MAXIMUM_FRACTION_DIGITS = 2;
    private final int IMAGE_VIEW_SIZE = 120;
    private RegisterManager registerManager;
    private String category;
    private ArrayList<Product> productList;
    private ArrayList<Product> searchProductList;

    public ProductFragment() {
        this.registerManager = RegisterManager.getInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Log.d("ProductFragment","onCreate is called");

        Bundle bundle = getArguments();
        this.category = bundle.getString(ProductFragment.ARG_ITEM_ID);
        String searchWord = bundle.getString(ProductFragment.ARG_SEARCH_WORD);

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
            searchProductList = productList;
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    public void refresh() {
        setListAdapter(new ListAdapter(getActivity()));
    }


    protected void showDialogFragment(Product product) {
        ChangeStockDialogFragment fragment = new ChangeStockDialogFragment();
        fragment.giveProductData(product);
        fragment.show(getActivity().getFragmentManager(), "aaaa");
    }

    public static class ChangeStockDialogFragment extends DialogFragment {

        private final int IMAGE_VIEW_SIZE = 120;
        private Product product;

        public void giveProductData(Product product) {
            this.product = product;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View content = inflater.inflate(R.layout.change_stock_num_dialog, null);
            builder.setView(content);

            setImageViewForDialog(content, product);
            setProductInformationView(content, product);

            final EditText changingEditText = (EditText) content.findViewById(R.id.changing_stock_num);
            changingEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            changingEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
            changingEditText.setText(String.valueOf(product.getStock()));
            changingEditText.setSelection(String.valueOf(product.getStock()).length());

            ProductButton plusBtn = (ProductButton) content.findViewById(R.id.plusButton);
            plusBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str = changingEditText.getText().toString();
                    int num = 0;
                    try {
                        num = Integer.parseInt(str);
                        num++;
                    } catch (NumberFormatException e) {
                        Toast toast = Toast.makeText(ChangeStockDialogFragment.this.getActivity(), "set zero because the input number is not available", Toast.LENGTH_SHORT);
                        toast.show();
                    } finally {

                        if (num < 0 || num > Integer.MAX_VALUE) {
                            num = 0;
                        }
                        str = String.valueOf(num);
                        changingEditText.setText(str);
                    }
                }
            });

            ProductButton minusBtn = (ProductButton) content.findViewById(R.id.minusButton);
            minusBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str = changingEditText.getText().toString();
                    int num = 0;
                    try {
                        num = Integer.parseInt(str);
                        num = num - 1;

                    } catch (NumberFormatException e) {
                        Toast toast = Toast.makeText(ChangeStockDialogFragment.this.getActivity(), "set zero because the input number is not available", Toast.LENGTH_SHORT);
                        toast.show();
                    } finally {
                        if (num < 0) {
                            num = 0;
                        }
                        str = String.valueOf(num);
                        changingEditText.setText(str);
                    }
                }
            });

            //FIXME 画面に表示する文言は xml にまとめる

            builder.setPositiveButton("Edit Stock", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        if (Integer.parseInt(changingEditText.getText().toString()) < 0) {
                            changingEditText.setText("0");
                        }
                        product.setStock(Integer.parseInt(changingEditText.getText().toString()));
                        confirmEdit(product.getCode(), changingEditText.getText().toString());

                        FragmentActivity activity =(FragmentActivity) getActivity();
                        ProductFragment fragment =(ProductFragment) activity.getSupportFragmentManager().findFragmentById(R.id.product_list);
                        fragment.refresh();

                    } catch (NumberFormatException e) {
                        Toast toast = Toast.makeText(ChangeStockDialogFragment.this.getActivity(), "the input number is not available", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });
            builder.setNegativeButton("Cancel", null);
            AlertDialog dialog = builder.create();

            return dialog;
        }

        private void confirmEdit(String productCode, String productStock) {

            if (Integer.parseInt(productStock) < 0 || Integer.parseInt(productStock) > Integer.MAX_VALUE) {
                productStock = "0";
            }

            WomanShopIOManager womanShopIOManager = new WomanShopIOManager();
            DatabaseHelper databaseHelper = new DatabaseHelper(this.getActivity());
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            womanShopIOManager.setDatabase(db);
            womanShopIOManager.updateStock(productCode, productStock);
            womanShopIOManager.closeDatabase();
        }

        private void setImageViewForDialog(View convertView, final Product product) {
            //TODO xmlにかけるかも
            ImageView imageView = (ImageView) convertView.findViewById(R.id.photo);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(IMAGE_VIEW_SIZE, IMAGE_VIEW_SIZE));

            try {
                Bitmap image = product.decodeProductImage(IMAGE_VIEW_SIZE, IMAGE_VIEW_SIZE);
                imageView.setImageBitmap(image);
                imageView.setVisibility(View.VISIBLE);
            } catch (FileNotFoundException e) {
                imageView.setVisibility(View.INVISIBLE);
            }

            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        ProductDetailDialogFragment dialogFragment = new ProductDetailDialogFragment();
                        Bundle arguments = new Bundle();
                        DisplayMetrics metrics = getResources().getDisplayMetrics();
                        arguments.putParcelable(ProductDetailDialogFragment.ARG_KEY_IMAGE_BITMAP, product.decodeProductImage(metrics.widthPixels, metrics.heightPixels));
                        dialogFragment.setArguments(arguments);
                        dialogFragment.show(getActivity().getFragmentManager(), ProductDetailDialogFragment.DIALOG_TAG);
                    } catch (FileNotFoundException e) {
                        Log.e("ProductFragment", "Product image file is not found.", e);
                        Toast.makeText(getActivity(), R.string.error_image_file_not_found, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        private void setProductInformationView(View convertView, Product product) {

            TextView textView = (TextView) convertView.findViewById(R.id.filename);
            String productName = product.getName();
            if (productName == null || productName.length() == 0) {
                throw new NullPointerException("Product name is not valid");
            }
            textView.setText(productName);

            NumberFormat.getInstance().setMaximumFractionDigits(MAXIMUM_FRACTION_DIGITS);

            TextView stockView = (TextView) convertView.findViewById(R.id.stock);
            int stock = product.getStock();
            stockView.setText(NumberFormat.getInstance().format(stock));

            if (stock <= 0) {
                stockView.setTextColor(getResources().getColor(R.color.warn));
            } else {
                stockView.setTextColor(getResources().getColor(android.R.color.black));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("ProductFragment:","onResume is called");

        setListAdapter(new ListAdapter(getActivity()));
    }

    public class ListAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public ListAdapter(Context context) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return searchProductList.size();
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

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.row_product_for_edit, null);
            }

            final Product product = searchProductList.get(position);
            setImageViewForProductFragment(convertView, product);
            setProductInformationView(convertView, product);
            convertView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    showDialogFragment(product);
                }
            });
            return convertView;
        }

        private void setImageViewForProductFragment(View convertView, final Product product) {
            ImageView imageView = (ImageView) convertView.findViewById(R.id.photo);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(IMAGE_VIEW_SIZE, IMAGE_VIEW_SIZE));

            try {
                Bitmap image = product.decodeProductImage(IMAGE_VIEW_SIZE, IMAGE_VIEW_SIZE);
                imageView.setImageBitmap(image);
                imageView.setVisibility(View.VISIBLE);
            } catch (FileNotFoundException e) {
                imageView.setVisibility(View.INVISIBLE);
            }

            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        ProductDetailDialogFragment dialogFragment = new ProductDetailDialogFragment();
                        Bundle arguments = new Bundle();
                        DisplayMetrics metrics = getResources().getDisplayMetrics();
                        arguments.putParcelable(ProductDetailDialogFragment.ARG_KEY_IMAGE_BITMAP, product.decodeProductImage(metrics.widthPixels, metrics.heightPixels));
                        dialogFragment.setArguments(arguments);
                        dialogFragment.show(getActivity().getFragmentManager(), ProductDetailDialogFragment.DIALOG_TAG);
                    } catch (FileNotFoundException e) {
                        Log.e("ProductFragment", "Product image file is not found.", e);
                        Toast.makeText(getActivity(), R.string.error_image_file_not_found, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        private void setProductInformationView(View convertView, Product product) {

            TextView textView = (TextView) convertView.findViewById(R.id.filename);
            String productName = product.getName();
            if (productName == null || productName.length() == 0) {
                throw new NullPointerException("Product name is not valid");
            }
            textView.setText(productName);

            NumberFormat.getInstance().setMaximumFractionDigits(MAXIMUM_FRACTION_DIGITS);

            TextView priceView = (TextView) convertView.findViewById(R.id.price);
            priceView.setText(NumberFormat.getInstance().format(product.getPrice()));

            TextView costView = (TextView) convertView.findViewById(R.id.cost);
            double cost = product.getOriginalCost();
            costView.setText(NumberFormat.getInstance().format(cost));

            TextView stockView = (TextView) convertView.findViewById(R.id.stock);
            int stock = product.getStock();
            stockView.setText(NumberFormat.getInstance().format(stock));

            if (stock <= 0) {
                stockView.setTextColor(getResources().getColor(R.color.warn));
            } else {
                stockView.setTextColor(getResources().getColor(android.R.color.black));
            }


        }


    }


}