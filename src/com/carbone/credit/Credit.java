package com.carbone.credit;

import com.carbone.creditMap.CreditMap;
import com.carbone.utils.Currency;

public class Credit {
	
	/**
	 * Key
	 * 	Store + Date + Amount
	 * 	If a duplicate entry is to be created, a "-X" suffix will be appended to the end of Store
	 * 
	 * @return
	 */
	public static Credit createCredit() {
		return new Credit();
	}

	public static Credit createCredit(String store, Long date, 
			Currency amount, String location, String major, String minor, Boolean ignore) {
		return new Credit(store, date, amount, location, major, minor, ignore);
	}

	private String mStore = null;	//Key
	private Long mDate = null;
	private String mLocation = null;
	private Currency mAmount = null; 	// In cents
	private Boolean mIgnore = null;
	private String mMajor = null;
	private String mMinor = null;


	protected Credit(){}

	protected Credit(
			String store,
			Long date,
			Currency amount,
			String location,
			String major, 
			String minor,
			Boolean ignore
			){
		this.setStore(store);
		this.setDate(date);
		this.setAmount(amount);
		this.setLocation(location);
		this.setMajor(major);
		this.setMinor(minor);
		this.setIgnore(ignore);

	}


	public String toString(){
		String s = getStore() + ":[" + 
				getDate().toString() +"," + 
				getAmount() +"," + 
				getLocation() +"," + 
				getMajor() + "," + 
				getMinor() +"," +
				isIgnore() + "]";
		return s.toString();
	}

	public String getMinor() {
		return mMinor;
	}

	public void setMinor(String mMinor) {
		this.mMinor = mMinor;
	}

	public Long getDate() {
		return mDate;
	}

	public void setDate(Long mDate) {
		this.mDate = mDate;
	}

	public String getLocation() {
		return mLocation;
	}

	public void setLocation(String location) {
		this.mLocation = location;
	}

	public Currency getAmount() {
		return mAmount;
	}

	public void setAmount(Currency mAmount) {
		this.mAmount = mAmount;
	}

	public Boolean isIgnore() {
		return mIgnore;
	}

	public void setIgnore(Boolean mIgnore) {
		this.mIgnore = mIgnore;
	}

	public String getMajor() {
		return mMajor;
	}

	public void setMajor(String mMajor) {
		this.mMajor = mMajor;
	}

	public String getStore() {
		return mStore;
	}

	public void setStore(String mStore) {
		this.mStore = mStore;
	}
		
	public Credit makeKey(){
		Credit credit = new Credit();
		credit.setStore(mStore);
		credit.setDate(mDate);
		credit.setAmount(mAmount);
		return credit;
	}
}
