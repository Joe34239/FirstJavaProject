package com.carbone.creditMap;

public class CreditMap {
	private String mTag = null;
	private String mMajor = null;
	private String mMinor = null;

	public CreditMap(){}
	
	public CreditMap(String tag,String major, String minor){
		setTag(tag);
		setMajor(major);
		mMinor = minor;
	}
	
	public String getTag() {
		return mTag;
	}

	public void setTag(String mTag) {
		this.mTag = mTag;
	}

	public String getMajor() {
		return mMajor;
	}

	public void setMajor(String mMajor) {
		this.mMajor = mMajor;
	}
	
	public String getMinor() {
		return mMinor;
	}

	public void setMinor(String mMinor) {
		this.mMinor = mMinor;
	}

	public String toString(){
		String s = "Tag:[" + mTag + "] Major:[" + mMajor + "] Minor:[" + mMinor +"]";
		return s.toString();
	}
}
