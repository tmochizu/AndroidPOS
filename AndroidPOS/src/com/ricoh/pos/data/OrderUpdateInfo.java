package com.ricoh.pos.data;

public class OrderUpdateInfo {
	private double totalAmountBeforeDiscount;
	private double discountAmount;
	private double totalAmountAfterDiscount;
	
	public OrderUpdateInfo(double totalAmountBeforeDiscount, double discountAmount, double totalAmountAfterDiscount) {
		this.totalAmountBeforeDiscount = totalAmountBeforeDiscount;
		this.discountAmount = discountAmount;
		this.totalAmountAfterDiscount = totalAmountAfterDiscount;
	}
	
	public double GetTotalAmountBeforeDiscount() {
		return totalAmountBeforeDiscount;
	}
	
	public double GetDiscountAmount() {
		return discountAmount;
	}
	
	public double GetTotalAmountAfterDiscount() {
		return totalAmountAfterDiscount;
	}
}
