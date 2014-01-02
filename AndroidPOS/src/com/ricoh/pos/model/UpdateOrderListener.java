package com.ricoh.pos.model;

import java.util.EventListener;

public interface UpdateOrderListener extends EventListener {

	public void notifyUpdateOrder(double totalPayment);
	
}
