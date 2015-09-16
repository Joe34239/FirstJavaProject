package com.carbone.storeId;

public class StoreId {
	private String mOldName = null;
	private String mNewName = null;

	public StoreId(){}
	
	public StoreId(String oldName,String newName){
		setOldName(oldName);
		setNewName(newName);
	}

	public String toString(){
		String s = "OldName:[" + mOldName + "] NewName:[" + mNewName +"]";
		return s.toString();
	}

	public String getOldName() {
		return mOldName;
	}

	public void setOldName(String mOldName) {
		this.mOldName = mOldName;
	}

	public String getNewName() {
		return mNewName;
	}

	public void setNewName(String mNewName) {
		this.mNewName = mNewName;
	}
}
