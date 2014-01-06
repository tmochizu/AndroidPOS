package com.ricoh.pos.model;

import java.util.ArrayList;

import com.ricoh.pos.data.Product;
import com.ricoh.pos.data.Order;

public class RegisterManager {
	
	private static RegisterManager instance;
	
	private ArrayList<Order> orderList;
	
	private UpdateOrderListener listener;
	
	private double discountValue;
	
	private RegisterManager(){
		orderList = new ArrayList<Order>();
	}
	
	public static RegisterManager getInstance(){
		if (instance == null) {
			instance = new RegisterManager();
		}
		
		return instance;
	}
	
	public void updateOrder(Product product, int num){
		Order orderOfTheProduct = findOrderOfTheProduct(product);
		
		if (orderOfTheProduct == null) {
			Order newOrder = new Order(product,num);
			orderList.add(newOrder);
		} else {
			orderOfTheProduct.setNumberOfOrder(num);
		}
		
		notifyUpdateOrder();
	}
	
	private void notifyUpdateOrder(){
		
		if (this.listener == null) {
			throw new IllegalStateException("UpdateOrderListener is not resgistered");
		}
		
		listener.notifyUpdateOrder(getTotalAmount());
	}
	
	public double getTotalAmount(){
		double totalAmount = 0;
		for (Order order: orderList) {
			totalAmount += order.getTotalAmount();
		}
		totalAmount -= discountValue;
		return totalAmount;
	}
	
	public void clearAllOrders(){
		orderList = new ArrayList<Order>();
		discountValue = 0;
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
	
	public void setUpdateOrderListener(UpdateOrderListener listener){
		this.listener = listener;
	}
	
	public void clearUpdateOrderListener(){
		this.listener = null;
	}
	
	public void updateDiscountValue(double discountValue)
	{
		this.discountValue = discountValue;
		notifyUpdateOrder();
	}
}
