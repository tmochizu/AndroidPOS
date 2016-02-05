package com.ricoh.pos.data;

import java.util.Date;
import java.math.BigDecimal;

public class WomanShopFormatter {
	
	public static String formatDate(Date date) {
		String formatDate = "";
		String[] splitDate = null;

		splitDate = date.toString().split(" ");
		for (String element : splitDate) {
			if (element.indexOf(":") != -1) {
				element = element.replaceAll("[+:]", "");
			}
			formatDate += element;
		}

		return formatDate;
	}

	/**
	 * DBやクラスではパイサ単位で格納されている金額をルピー表記に変換する。
	 * @param paisa 金額。単位パイサ（＝整数）
	 * @return double 引数paisaのルピー表記。(rrrr.ppのdouble表記)
	 */
	public static double convertPaisaToRupee(long paisa)
	{
		BigDecimal result = new BigDecimal(paisa);
		return result.scaleByPowerOfTen(-2).doubleValue();
	}

	/**
	 * UIでルピー単位で入力された数字をパイサ単位に変換する。
	 * @param rupee 金額。単位ルピー。
	 * @return long 引数rupeeのパイサ表記。
	 */
	public static long convertRupeeToPaisa(double rupee)
	{
		BigDecimal result = new BigDecimal(rupee);
		return result.scaleByPowerOfTen(2).longValue();
	}
}
