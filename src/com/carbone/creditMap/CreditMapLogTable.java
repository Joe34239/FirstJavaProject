package com.carbone.creditMap;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import com.carbone.dataset.CSVentryType;
import com.carbone.dataset.DataSet;
import com.carbone.dataset.LogAction;
import com.carbone.main.Global;
import com.carbone.utils.CustomException;
import com.carbone.utils.TimeStamp;

public class CreditMapLogTable extends DataSet<CreditMapLog>  implements Iterable<CreditMapLog>{
	
	final static String [] colNames = {
		"TimeStamp",
		"Action",
		"Tag",
		"Major",
		"Minor"
	};
	
	final static CSVentryType [] colTypes = {
		CSVentryType.TIMESTAMP,
		CSVentryType.STRING,
		CSVentryType.STRING,
		CSVentryType.STRING,
		CSVentryType.STRING
	};
	
	public CreditMapLogTable (String tableName){
		super(colNames, colTypes, tableName+ "<Log>");
	}	
	
	@Override
	public String makeRow(CreditMapLog row){
		return CSVoutTimeStamp(row.getTimeStamp()) + "," + row.getAction().toString() +
				"," + row.getTag() + "," + row.getMajor() + "," + row.getMinor() + "\n" ;
	}
	
	@Override
	public CreditMapLog parseRow(String [] tokens) throws CustomException{
		if (tokens.length != colNames.length) throw new CustomException("CSV File does not match template");
		long timeStamp = TimeStamp.fromString(tokens[0], Global.TIME_STAMP_CSV_FORMAT);
		LogAction action = LogAction.valueOf(tokens[1]);
		return new CreditMapLog(timeStamp,action,tokens[2],tokens[3],tokens[4]);
	}
	
	@Override
	public Comparator<CreditMapLog> getComparator(String name) throws CustomException{
		int idx = Arrays.asList(colNames).indexOf(name);
		switch(idx){
		case 0:		//TimeStamp
			return new TimeStampComparator();
		case 1:		//Action
			return new ActionComparator();
		case 2:		//Tag
			return new TagComparator();
		default:
			throw new CustomException("Illegal Compare Key");
		}
	}
	class TagComparator implements Comparator<CreditMapLog> {
	    public int compare(CreditMapLog rec1, CreditMapLog rec2) {
	        return rec1.getTag().compareTo(rec2.getTag());
	    }
	}
	class ActionComparator implements Comparator<CreditMapLog> {
	    public int compare(CreditMapLog rec1, CreditMapLog rec2) {
	        return rec1.getAction().compareTo(rec2.getAction());
	    }
	}
	class TimeStampComparator implements Comparator<CreditMapLog> {
	    public int compare(CreditMapLog rec1, CreditMapLog rec2) {
	        return (int)(rec1.getTimeStamp() - rec2.getTimeStamp());
	    }
	}
	
	@Override
	public boolean isMatch(CreditMapLog item, CreditMapLog match) {
		if (match.getTag() != null){
			if (!match.getTag().equals(item.getTag())) return false;
		}
		if (match.getMajor() != null){
			if (!match.getMajor().equals(item.getMajor())) return false;
		}
		if (match.getMinor() != null){
			if (!match.getMinor().equals(item.getMinor())) return false;
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
	public CreditMapLog copy(CreditMapLog item){
		return new CreditMapLog(item.getTimeStamp(),item.getAction(),item.getTag(),item.getMajor(),item.getMinor());
	}

	@Override
	public void replace(CreditMapLog item, CreditMapLog value) {
		if (value.getTag() != null){
			item.setTag(value.getTag());
		}
		if (value.getMajor() != null){
			item.setMajor(value.getMajor());
		}
		if (value.getMinor() != null){
			item.setMinor(value.getMinor());
		}
		if (value.getAction() != null){
			item.setAction(value.getAction());
		}
		if (value.getTimeStamp() != null){
			item.setTimeStamp(value.getTimeStamp());
		}
		
	}

	@Override
	public Iterator<CreditMapLog> iterator() {
		return getIterator();
	}
}
