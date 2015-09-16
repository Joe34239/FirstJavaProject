package com.carbone.creditMap;

import com.carbone.dataset.LogAction;
import com.carbone.main.Global;
import com.carbone.utils.TimeStamp;

public class CreditMapLog extends CreditMap  {
	protected Long mTimeStamp = null;
	protected LogAction mAction = null;
	
	public CreditMapLog(){
		super();
	}
	
	public CreditMapLog(Long timeStamp, LogAction action, String tag, String major, String minor){
		super(tag, major, minor);
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
		String s = "Tag:[" + super.getTag() + "] Major:[" + super.getMajor() + "] + Minor:[" + super.getMinor()+
				"] Action:[" + mAction.toString() + 
				"] TimeStamp:[" + TimeStamp.toString(mTimeStamp, Global.TIME_STAMP_CSV_FORMAT) + "]";
		return s.toString();
	}
}
