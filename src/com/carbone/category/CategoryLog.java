package com.carbone.category;

import com.carbone.dataset.LogAction;
import com.carbone.main.Global;
import com.carbone.utils.TimeStamp;

public class CategoryLog extends Category  {
	protected Long mTimeStamp = null;
	protected LogAction mAction = null;
	
	public CategoryLog(){
		super();
	}
	
	public CategoryLog(Long timeStamp, LogAction action, String name){
		super(name);
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
		String s = "Name:[" + super.getName() + 
				"] Action:[" + mAction.toString() + 
				"] TimeStamp:[" + TimeStamp.toString(mTimeStamp, Global.TIME_STAMP_CSV_FORMAT) + "]";
		return s.toString();
	}
}
