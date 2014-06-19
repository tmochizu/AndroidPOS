package com.ricoh.pos.data;

/*
public enum WomanShopDataDef {
	PRODUCT_CODE,
	PRODUCT_CATEGORY,
	ITEM_CATEGORY,
	QTY,
	SALE_PRICE,
	TOTAL_SALE_PRICE,
	COST_TO_ENTREPRENEUR,
	TOTAL_COST_TO_ENTREP,
	TOTAL_PROFIT_TO_ENTREP,
}
*/

public enum WomanShopDataDef {
	PRODUCT_CODE("product_id"),
	ITEM_CATEGORY("product_name"),
	PRODUCT_CATEGORY("category_name"),
	COST_TO_ENTREPRENEUR("unit_selling_price"),
	SALE_PRICE("MRP");
	/*
	ITEM_CATEGORY(" "),
	QTY(" "),
	TOTAL_SALE_PRICE(" "),
	TOTAL_COST_TO_ENTREP(" "),
	TOTAL_PROFIT_TO_ENTREP(" ");
	*/
	private String name;
	WomanShopDataDef(String name){ this.name = name;}
	public String getName() {return name;}
}
