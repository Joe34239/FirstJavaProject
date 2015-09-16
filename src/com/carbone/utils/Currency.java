package com.carbone.utils;

public class Currency {
	public static Currency create() {
		return new Currency();
	}
	public static Currency createCents(long amount) {
		return new Currency(amount);
	}
	public static Currency create(String amount) {
		return new Currency(amount);
	}
	// Supports currency as a Long representing cents
	//  Illegal String Input results in Long.MAX_VALUE
	
	private Long mCents;
	
	private Currency(){mCents = 0L;}
	private Currency(long amount){mCents = amount;}
	private Currency(String amount){
		try {
			Double d = Double.parseDouble(amount);
			mCents = Math.round(d*100.0);
		} catch (NumberFormatException e){
			mCents = Long.MAX_VALUE;
		}

	}
	
	public Long getCents(){ return new Long(mCents);}
	public void setCents(long cents){ mCents = cents;}
	
	public String toString(){
		String s =  String.format("%d.%02d",mCents/100,Math.abs(mCents%100));
		if ((mCents/100 == 0) & (mCents <0)) s = "-"+ s;
		return s;
	}
	
}
