package com.ricoh.pos.data;

public class Product {
	
	private String category;
	private String name;
	private int originalCost;
	private int price;
	private int stock;
	private String imagePath;
	
	public Product(String category,String name){
		this.category = category;
		this.name = name;
	}
	
	///////////////////////////
	// Setter
	///////////////////////////
	public void setOriginalCost(int cost){
		this.originalCost = cost;
	}
	
	public void setPrice(int price){
		this.price = price;
	}
	
	public void setStock(int stock){
		this.stock = stock;
	}
	
	public void setProductImagePath(String imagePath){
		this.imagePath = imagePath;
	}
	

	///////////////////////////
	// Getter
	///////////////////////////

	public String getCategory(){
		return this.category;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getOriginalCost(){
		return this.originalCost;
	}
	
	public int getPrice(){
		return this.price;
	}
	
	public int getStock(){
		return this.stock;
	}
	
	public String getProductImagePath(){
		return imagePath;
	}
	
	
	public Product clone(){
		return this.clone();
	}
}
