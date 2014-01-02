package com.ricoh.pos.model;

import java.util.ArrayList;

import com.ricoh.pos.data.Product;
import com.ricoh.pos.data.Order;

public class RegisterManager {
	
	private static RegisterManager instance;
	
	private ArrayList<Order> orderList;
	
	private RegisterManager(){
		orderList = new ArrayList<Order>();
	}
	
	public static RegisterManager getInstance(){
		if (instance == null) {
			return new RegisterManager();
		} else {
			return instance;
		}
	}
	
	public void updateOrder(Product product, int num){
		Order orderOfTheProduct = findOrderOfTheProduct(product);
		
		if (orderOfTheProduct == null) {
			Order newOrder = new Order(product,num);
			orderList.add(newOrder);
		} else {
			orderOfTheProduct.setNumberOfOrder(num);
		}
	}
	
	public int getTotalAmount(){
		int totalAmount = 0;
		for (Order order: orderList) {
			totalAmount += order.getTotalAmount();
		}
		return totalAmount;
	}
	
	public void clearAllOrders(){
		orderList = new ArrayList<Order>();
	}
	
	public Order findOrderOfTheProduct(Product product){
		for (Order order : orderList) {
			if ((order.getProductCategory().equals(product.getCategory()) && order.getProductName().equals(product.getName()))) {
				return order;
			}
		}
		// Not Found
		return null;
	}
	


}
