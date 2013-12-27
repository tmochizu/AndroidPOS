package com.ricoh.pos.model;

import java.util.ArrayList;
import com.ricoh.pos.data.Product;

public class ProductsManager {
	
	private ArrayList<Product> productList;
	private static ProductsManager instance;
	
	private ProductsManager(){
		this.productList = new ArrayList<Product>();
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
	
	public ArrayList<Product> getAllProducts(){
		// Return deep copy of productList
		ArrayList<Product> productListClone = new ArrayList<Product>();
		
		for (Product product : productList) {
			productListClone.add( product.clone());
		}
		
		return productListClone;
	}
	
	public ArrayList<Product> getProductsInCategory(String category) {
		
		if(category == null || category.length() == 0){
			throw new IllegalArgumentException("Invalid category name");
		}
		
		ArrayList<Product> productsInCategory = new ArrayList<Product>();
		
		for (Product product : productList) {
			if (product.getCategory().equals(category)){
				productsInCategory.add(product.clone());
			}
		}
		
		if (productsInCategory.size() == 0) {
			throw new IllegalArgumentException("No such category");
		}
		
		return productsInCategory;
	}

}
