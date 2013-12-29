package com.ricoh.pos.model;

import java.util.ArrayList;
import com.ricoh.pos.data.Product;

public class ProductsManager {
	
	// TODO: Change these lists to imported products data.
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
	
	private ArrayList<Product> productList;
	private static ProductsManager instance;
	
	private ProductsManager(){
		this.productList = new ArrayList<Product>();
		
		for (int i=0 ; i < itemList.length ; i++) { 
			Product product = new Product("Test", itemList[i]);
			product.setProductImagePath(itemPhotoList[i]);
			addNewProduct(product);
		}
		
	}

	public static ProductsManager getInstance() {
		
		if (instance == null){
			instance = new ProductsManager();
		}
		
		return instance;
	}
	
	public void addNewProduct(Product product) {
		
		if (product == null) {
			throw new IllegalArgumentException("Passing product is null");
		} 
		
		for (Product registeredProduct : productList) {
			if(registeredProduct.getName().equals(product.getName())){
				throw new IllegalArgumentException("The passing product has already been registered");
			}
		}
		
		productList.add(product);
		
	}
	
	public Product getProductByName(String productName){
		
		if (productName == null || productName.length() == 0) {
			throw new IllegalArgumentException("Invalid product name");
		}
		
		for (Product product : productList){
			if (product.getName().equals(productName)) {
				// Successfully found
				return product;
			}
		}
		
		// Not Found
		return null;
	}
	
	public Product getProductFromId(int productId){
		
		if (productId < 0 || productId >= productList.size()) {
			throw new IllegalArgumentException("productId is out of range!");
		}
		
		return productList.get(productId);
	}
	
	public ArrayList<Product> getAllProducts(){
		return productList;
	}
	
	public ArrayList<Product> getProductsInCategory(String category) {
		
		if(category == null || category.length() == 0){
			throw new IllegalArgumentException("Invalid category name");
		}
		
		ArrayList<Product> productsInCategory = new ArrayList<Product>();
		
		for (Product product : productList) {
			if (product.getCategory().equals(category)){
				productsInCategory.add(product);
			}
		}
		
		if (productsInCategory.size() == 0) {
			throw new IllegalArgumentException("No such category");
		}
		
		return productsInCategory;
	}
	
	public int getNumberOfProductsInCategory(String category){
		return getProductsInCategory(category).size();
	}

}
