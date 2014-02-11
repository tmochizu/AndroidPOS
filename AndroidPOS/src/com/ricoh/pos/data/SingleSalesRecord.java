package com.ricoh.pos.data;

import java.util.ArrayList;
import java.util.Date;

public class SingleSalesRecord {
	
	private Date salesDate;
	private double discountValue;
	private ArrayList<Order> orders;
	
	public SingleSalesRecord(Date date){
		orders = new ArrayList<Order>();
		this.salesDate = date;
		this.discountValue = 0;
	}
	
	public void setOrders(ArrayList<Order> orderList){
		this.orders = orderList;
	}
	
	public void addOrder(Order order){
		if (order == null) {
			throw new IllegalArgumentException("Order is null");
		}
		orders.add(order);
	}
	
	public void setDiscountValue(double discountValue) {
		this.discountValue = discountValue;
	}
	
	public ArrayList<Order> getAllOrders(){
		return orders;
	}
	
	public Date getSalesDate(){
		return salesDate;
	}
	
	public double getDiscountValue() {
		return discountValue;
	}
	
}
