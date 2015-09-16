package com.carbone.category;

public class Category {
	/*
	 * Key
	 * 	Name - Always Unique
	 */
	protected String mName = null;
	
	public Category(){
		mName  = null;
	}
	
	public Category(String s){
		mName  = s;
	}

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}
	
	public String toString(){
		String s = "Name:[" + mName + "]";
		return s.toString();
	}
}
