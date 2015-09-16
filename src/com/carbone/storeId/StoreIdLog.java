package com.carbone.storeId;

import com.carbone.dataset.LogAction;
import com.carbone.main.Global;
import com.carbone.utils.TimeStamp;

public class StoreIdLog extends StoreId  {
	protected Long mTimeStamp = null;
	protected LogAction mAction = null;
	
	public StoreIdLog(){
		super();
	}
	
	public StoreIdLog(Long timeStamp, LogAction action, String oldName, String newName
			){
		super(oldName, newName);
		mTimeStamp = timeStamp;
		mAction = action;
	}

	public Long getTimeStamp() {
		return mTimeStamp;
	}

	public void setTimeStamp(Long mTimeStamp) {
		this.mTimeStamp = mTimeStamp;
	}

	public LogAction getAction() {
		return mAction;
	}

	public void setAction(LogAction mAction) {
		this.mAction = mAction;
	}
	
	public String toString(){
		String s = "OldName:[" + super.getOldName() + "] NewName:[" + super.getNewName() + "]" +
				"] Action:[" + mAction.toString() + 
				"] TimeStamp:[" + TimeStamp.toString(mTimeStamp, Global.TIME_STAMP_CSV_FORMAT) + "]";
		return s.toString();
	}
}
