package com.carbone.storeId;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import com.carbone.dataset.CSVentryType;
import com.carbone.dataset.DataSet;
import com.carbone.dataset.LogAction;
import com.carbone.main.Global;
import com.carbone.utils.CustomException;
import com.carbone.utils.TimeStamp;

public class StoreIdLogTable extends DataSet<StoreIdLog>  implements Iterable<StoreIdLog> {
	
	final static String [] colNames = {
		"TimeStamp",
		"Action",
		"OldName",
		"NewName"
	};
	
	final static CSVentryType [] colTypes = {
		CSVentryType.TIMESTAMP,
		CSVentryType.STRING,
		CSVentryType.STRING,
		CSVentryType.STRING
	};
	
	public StoreIdLogTable (String tableName){
		super(colNames, colTypes, tableName+ "<Log>");
	}	
	
	@Override
	public String makeRow(StoreIdLog row){
		return CSVoutTimeStamp(row.getTimeStamp()) + "," + row.getAction().toString() +
				"," + row.getOldName() + "," + row.getNewName() + "\n" ;
	}
	
	@Override
	public StoreIdLog parseRow(String [] tokens) throws CustomException{
		if (tokens.length != colNames.length) throw new CustomException("CSV File does not match template");
		long timeStamp = TimeStamp.fromString(tokens[0], Global.TIME_STAMP_CSV_FORMAT);
		LogAction action = LogAction.valueOf(tokens[1]);
		return new StoreIdLog(timeStamp,action,tokens[2],tokens[3]);
	}
	
	@Override
	public Comparator<StoreIdLog> getComparator(String name) throws CustomException{
		int idx = Arrays.asList(colNames).indexOf(name);
		switch(idx){
		case 0:		//TimeStamp
			return new TimeStampComparator();
		case 1:		//Action
			return new ActionComparator();
		case 2:		//OldName
			return new OldNameComparator();
		default:
			throw new CustomException("Illegal Compare Key");
		}
	}
	class OldNameComparator implements Comparator<StoreIdLog> {
	    public int compare(StoreIdLog rec1, StoreIdLog rec2) {
	        return rec1.getOldName().compareTo(rec2.getOldName());
	    }
	}
	class ActionComparator implements Comparator<StoreIdLog> {
	    public int compare(StoreIdLog rec1, StoreIdLog rec2) {
	        return rec1.getAction().compareTo(rec2.getAction());
	    }
	}
	class TimeStampComparator implements Comparator<StoreIdLog> {
	    public int compare(StoreIdLog rec1, StoreIdLog rec2) {
	        return (int)(rec1.getTimeStamp() - rec2.getTimeStamp());
	    }
	}
	
	@Override
	public boolean isMatch(StoreIdLog item, StoreIdLog match) {
		if (match.getOldName() != null){
			if (!match.getOldName().equals(item.getOldName())) return false;
		}
		if (match.getNewName() != null){
			if (!match.getNewName().equals(item.getNewName())) return false;
		}
		if (match.getAction() != null){
			if (!match.getAction().equals(item.getAction())) return false;
		}
		if (match.getTimeStamp() != null){
			if (!match.getTimeStamp().equals(item.getTimeStamp())) return false;
		}
		return true;
	}
	@Override
	public StoreIdLog copy(StoreIdLog item){
		return new StoreIdLog(item.getTimeStamp(),item.getAction(),item.getOldName(),item.getNewName());
	}

	@Override
	public void replace(StoreIdLog item, StoreIdLog value) {
		if (value.getOldName() != null){
			item.setOldName(value.getOldName());
		}
		if (value.getNewName() != null){
			item.setNewName(value.getNewName());
		}
		if (value.getAction() != null){
			item.setAction(value.getAction());
		}
		if (value.getTimeStamp() != null){
			item.setTimeStamp(value.getTimeStamp());
		}
		
	}

	@Override
	public Iterator<StoreIdLog> iterator() {
		return getIterator();
	}
}
