package com.carbone.credit;

import com.carbone.dataset.LogAction;
import com.carbone.main.Global;
import com.carbone.utils.Currency;
import com.carbone.utils.TimeStamp;

public class CreditLog extends Credit  {
	protected Long mTimeStamp = null;
	protected LogAction mAction = null;
	
	public CreditLog(){
		super();
	}
	
	public CreditLog(Long timeStamp, 
			LogAction action, 
			String store,
			Long date,
			Currency amount,
			String location,
			String major, 
			String minor,
			Boolean ignore){
		super(store,date,amount,location, major, minor,ignore);
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
		String s = "Store:[" + super.getStore() + 
				"] Date:[" + TimeStamp.toString(super.getDate(), Global.TIME_STAMP_CREDIT_FORMAT) + 
				"] Amount:[" + super.getAmount().toString() +
				"] Action:[" + mAction.toString() + 
				"] TimeStamp:[" + TimeStamp.toString(mTimeStamp, Global.TIME_STAMP_CSV_FORMAT) + "]";
		return s.toString();
	}
}
