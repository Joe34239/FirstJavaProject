package com.carbone.category;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import com.carbone.dataset.CSVentryType;
import com.carbone.dataset.DataSet;
import com.carbone.dataset.LogAction;
import com.carbone.main.Global;
import com.carbone.utils.CustomException;
import com.carbone.utils.TimeStamp;
import com.carbone.category.CategoryLog;

public class CategoryLogTable extends DataSet<CategoryLog>  implements Iterable<CategoryLog>{
	
	final static String [] colNames = {
		"TimeStamp",
		"Action",
		"Category"
	};
	
	final static CSVentryType [] colTypes = {
		CSVentryType.TIMESTAMP,
		CSVentryType.STRING,
		CSVentryType.STRING
	};
	
	public CategoryLogTable (String tableName){
		super(colNames, colTypes, tableName+ "<Log>");
	}	
	
	@Override
	public String makeRow(CategoryLog row){
		return CSVoutTimeStamp(row.getTimeStamp()) + "," + row.getAction().toString() + "," + row.getName() + "\n" ;
	}
	
	@Override
	public CategoryLog parseRow(String [] tokens) throws CustomException{
		if (tokens.length != colNames.length) throw new CustomException("CSV File does not match template");
		long timeStamp = TimeStamp.fromString(tokens[0], Global.TIME_STAMP_CSV_FORMAT);
		LogAction action = LogAction.valueOf(tokens[1]);
		return new CategoryLog(timeStamp,action,tokens[2]);
	}
	
	@Override
	public Comparator<CategoryLog> getComparator(String name) throws CustomException{
		int idx = Arrays.asList(colNames).indexOf(name);
		switch(idx){
		case 0:		//TimeStamp
			return new TimeStampComparator();
		case 1:		//Action
			return new ActionComparator();
		case 2:		//Category
			return new CategoryComparator();
		default:
			throw new CustomException("Illegal Compare Key");
		}
	}
	class CategoryComparator implements Comparator<CategoryLog> {
	    public int compare(CategoryLog rec1, CategoryLog rec2) {
	        return rec1.getName().compareTo(rec2.getName());
	    }
	}
	class ActionComparator implements Comparator<CategoryLog> {
	    public int compare(CategoryLog rec1, CategoryLog rec2) {
	        return rec1.getAction().compareTo(rec2.getAction());
	    }
	}
	class TimeStampComparator implements Comparator<CategoryLog> {
	    public int compare(CategoryLog rec1, CategoryLog rec2) {
	        return (int)(rec1.getTimeStamp() - rec2.getTimeStamp());
	    }
	}
	@Override
	public boolean isMatch(CategoryLog item, CategoryLog match) {;
		if (match.getName() != null){
			if (!match.getName().equals(item.getName())) return false;
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
	public CategoryLog copy(CategoryLog item){
		return new CategoryLog(item.getTimeStamp(),item.getAction(),item.getName());
	}

	@Override
	public void replace(CategoryLog item, CategoryLog value) {
		if (value.getName() != null){
			item.setName(value.getName());
		}
		if (value.getAction() != null){
			item.setAction(value.getAction());
		}
		if (value.getTimeStamp() != null){
			item.setTimeStamp(value.getTimeStamp());
		}
		
	}

	@Override
	public Iterator<CategoryLog> iterator() {
		return getIterator();
	}
}
