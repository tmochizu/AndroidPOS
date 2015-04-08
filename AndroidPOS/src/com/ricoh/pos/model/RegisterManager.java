package com.ricoh.pos.model;

import java.util.ArrayList;
import java.util.Date;

import android.util.Log;

import com.ricoh.pos.data.Order;
import com.ricoh.pos.data.OrderUpdateInfo;
import com.ricoh.pos.data.Product;
import com.ricoh.pos.data.SingleSalesRecord;

/**
 * シングルトン
 */
public class RegisterManager {
	
	private static RegisterManager instance;

    // 注文したいリスト(+ボタンで数量を入れたアイテムのリスト)
	private ArrayList<Order> orderList;
	
	private ArrayList<UpdateOrderListener> listeners;
	
	private double discountValue;
	
	private String userAttribute;
	
	private RegisterManager(){
		orderList = new ArrayList<Order>();
		listeners = new ArrayList<UpdateOrderListener>();
	}
	
	public static RegisterManager getInstance(){
		if (instance == null) {
			instance = new RegisterManager();
		}
		
		return instance;
	}
	
	public void updateOrder(Product product, int num){
		Order orderOfTheProduct = findOrderOfTheProduct(product);
		
		if (num == 0) {
			if (orderOfTheProduct == null) {
				return;
			} else {
				orderList.remove(orderOfTheProduct);
			}
		} else if (orderOfTheProduct == null) {
			Order newOrder = new Order(product,num);
			orderList.add(newOrder);
		} else {
			orderOfTheProduct.setNumberOfOrder(num);
		}
		
		notifyUpdateOrder();
	}

    /* 注文数を1個増やす
     * 1つも注文されていなければorderListに商品を1つ追加する
     */
	public void plusNumberOfOrder(Product product){
		Order orderOfTheProduct = findOrderOfTheProduct(product);
		
		if (orderOfTheProduct == null) {
			int numberOfFirstOrder = 1;
			Order newOrder = new Order(product,numberOfFirstOrder);
			orderList.add(newOrder);
		} else {
			orderOfTheProduct.plusNumberOfOrder();
		}
		
		notifyUpdateOrder();
	}
    /* 注文数を1個減らす
     * 注文数が0の場合は注文を削除する
     */
    public void minusNumberOfOrder(Product product) {
		Order orderOfTheProduct = findOrderOfTheProduct(product);
		
		if (orderOfTheProduct == null) {
			Order newOrder = new Order(product,0);
			orderList.add(newOrder);
		}else{
			orderOfTheProduct.minusNumberOfOrder();
			
			// 注文が0になったら削除
			if (orderOfTheProduct.getNumberOfOrder() == 0) {
				orderList.remove(orderOfTheProduct);
			}
		}
		
		notifyUpdateOrder();
	}
	
	private void notifyUpdateOrder(){
		
		if (listeners == null || listeners.isEmpty()) {
			throw new IllegalStateException("UpdateOrderListener is not resgistered");
		}
		
		for (UpdateOrderListener listener : listeners) {
			if (listener == null) {
				throw new IllegalStateException("UpdateOrderListener to register is null");
			}
			listener.notifyUpdateOrder(new OrderUpdateInfo(getOriginalTotalAmount()
					, discountValue
					, getTotalAmountAfterDiscount()));
		}
	}
	
	public double getOriginalTotalAmount(){
		double totalAmount = 0;
		for (Order order: orderList) {
			totalAmount += order.getTotalAmount();
		}
		return totalAmount;
	}
	
	public double getTotalAmountAfterDiscount(){
		return getOriginalTotalAmount() - discountValue;
	}
	
	public int getTotalNumberOfOrder() {
		int totalNumber = 0;
		for (Order order: orderList) {
			totalNumber += order.getNumberOfOrder();
		}
		return totalNumber;
	}
	
	public void clearAllOrders(){
		orderList = new ArrayList<Order>();
		discountValue = 0;
		userAttribute = null;
	}
    /**
     * orderListの中からカテゴリーとプロダクトコードが一致するものを取得する
     */
	public Order findOrderOfTheProduct(Product product){
		for (Order order : orderList) {
			if ((order.getProductCategory().equals(product.getCategory()) && order.getProductCode().equals(product.getCode()))) {
				return order;
			}
		}
		// Not Found
		return null;
	}
	
	public int getNumberOfOrder(Product product) {
		Order order = findOrderOfTheProduct(product);
		if (order == null) {
			return 0;
		} else {
			return order.getNumberOfOrder();	
		}
	}
	
	public void setUpdateOrderListener(UpdateOrderListener listener){
		listeners.add(listener);
	}
	
	public void removeUpdateOrderListener(UpdateOrderListener listener){
		listeners.remove(listener);
	}
	
	public void clearUpdateOrderListener(){
		listeners.clear();
	}

    /*
    値引き額と、注文商品の合計金額の関係のチェック
       値引き額>=合計金額でエラー
     */
	public void updateDiscountValue(double discountValue){

        Log.d("updateDiscountValue","discountValue=" + discountValue);
        Log.d("updateDiscountValue","getOriginalTotalAmount=" + getOriginalTotalAmount());
        double totalAmount = getOriginalTotalAmount();

        if(totalAmount == 0){
            this.discountValue = 0;
        } else if (discountValue >= totalAmount) {
			throw new IllegalArgumentException("discountValues is larger than totalAmount");
		} else {
            this.discountValue = discountValue;
        }
		notifyUpdateOrder();
	}

	public void setUserAttribute(String attribute){
		if (attribute == null || attribute.length() == 0) {
			throw new IllegalArgumentException("User attribute is illegal");
		}
		this.userAttribute = attribute;
		Log.d("Attribute:", " " + userAttribute + " ");
	}
	
	public SingleSalesRecord getSingleSalesRecord(){
		Log.d("RegisterManager","getSingleSalesRecord");
		SingleSalesRecord record = new SingleSalesRecord(new Date());
		record.setOrders(orderList);
		record.setDiscountValue(discountValue);
		record.setUserAttribute(userAttribute);
		return record;
	}
	
}
