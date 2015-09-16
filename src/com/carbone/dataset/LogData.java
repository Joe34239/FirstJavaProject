package com.carbone.dataset;

public interface LogData {
	public Long getTimeStamp();	
	public void setTimeStamp(Long timeStamp);
	public LogAction getAction();	
	public void setAction(LogAction action);

}
