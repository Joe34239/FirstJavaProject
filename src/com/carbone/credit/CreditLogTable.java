package com.carbone.credit;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import com.carbone.dataset.CSVentryType;
import com.carbone.dataset.DataSet;
import com.carbone.dataset.LogAction;
import com.carbone.main.Global;
import com.carbone.utils.Currency;
import com.carbone.utils.CustomException;
import com.carbone.utils.TimeStamp;

public class CreditLogTable extends DataSet<CreditLog>  implements Iterable<CreditLog>{
	
	final static String [] colNames = {
		"TimeStamp",
		"Action",
		"Store",
		"Date",
		"Amount",
		"Location",
		"Major",
		"Minor",
		"Ignore"
	};
	
	final static CSVentryType [] colTypes = {
		CSVentryType.TIMESTAMP,
		CSVentryType.STRING,
		CSVentryType.STRING,
		CSVentryType.TIMESTAMP,
		CSVentryType.CURRENCY,
		CSVentryType.STRING,
		CSVentryType.STRING,
		CSVentryType.STRING,
		CSVentryType.BOOLEAN
	};
	
	public CreditLogTable (String tableName){
		super(colNames, colTypes, tableName+ "<Log>");
	}	
	
	@Override
	public String makeRow(CreditLog row){
		return CSVoutTimeStamp(row.getTimeStamp()) + "," + 
				row.getAction().toString() + "," + 
				row.getStore() + "," + 
				TimeStamp.toString(row.getDate(), Global.TIME_STAMP_CREDIT_FORMAT) + "," + 
				row.getAmount().toString() + "," + 
				row.getLocation() + "," + 
				row.getMajor() + "," + 
				row.getMinor() + "," + 
				row.isIgnore() + "\n";
	}
	
	@Override
	public CreditLog parseRow(String [] tokens) throws CustomException{
		if (tokens.length != colNames.length) throw new CustomException("CSV File does not match template");
		long timeStamp = TimeStamp.fromString(tokens[0], Global.TIME_STAMP_CSV_FORMAT);
		LogAction action = LogAction.valueOf(tokens[1]);
		return new CreditLog(timeStamp,
				action,
				tokens[2],						//Store
				TimeStamp.fromString(tokens[3], Global.TIME_STAMP_CREDIT_FORMAT),	//Date
				Currency.create(tokens[4]),					//Amount
				tokens[5],									//Location
				tokens[6],									//Major
				tokens[7],									//Minor
				Boolean.parseBoolean(tokens[8]));			//Ignore
	}
	
	@Override
	public Comparator<CreditLog> getComparator(String name) throws CustomException{
		int idx = Arrays.asList(colNames).indexOf(name);
		switch(idx){
		case 0:		//TimeStamp
			return new TimeStampComparator();
		case 1:		//Action
			return new ActionComparator();
		case 2:		//Store
			return new StoreComparator();
		case 3:		//Date
			return new DateComparator();
		case 4:		//Amount
			return new AmountComparator();
		case 5:		//Location
			return new LocationComparator();
		case 6:		//Major
			return new MajorComparator();
		case 7:		//Minor
			return new MinorComparator();
		case 8:		//Ignore
			return new IgnoreComparator();
		default:
			throw new CustomException("Illegal Compare Key");
		}
	}
	class StoreComparator implements Comparator<CreditLog> {
	    public int compare(CreditLog rec1, CreditLog rec2) {
	        return rec1.getStore().compareTo(rec2.getStore());
	    }
	}
	class DateComparator implements Comparator<CreditLog> {
		public int compare(CreditLog rec1, CreditLog rec2) {
			return rec1.getDate().compareTo(rec2.getDate());
		}
	}
	class AmountComparator implements Comparator<CreditLog> {
		public int compare(CreditLog rec1, CreditLog rec2) {
			return rec1.getAmount().getCents().compareTo(rec2.getAmount().getCents());
		}
	}
	class LocationComparator implements Comparator<CreditLog> {
		public int compare(CreditLog rec1, CreditLog rec2) {
			return rec1.getLocation().compareTo(rec2.getLocation());
		}
	}
	class MajorComparator implements Comparator<CreditLog> {
		public int compare(CreditLog rec1, CreditLog rec2) {
			return rec1.getMajor().compareTo(rec2.getMajor());
		}
	}
	class MinorComparator implements Comparator<CreditLog> {
		public int compare(CreditLog rec1, CreditLog rec2) {
			return rec1.getMinor().compareTo(rec2.getMinor());
		}
	}
	class IgnoreComparator implements Comparator<CreditLog> {
		public int compare(CreditLog rec1, CreditLog rec2) {
			return rec1.isIgnore().compareTo(rec2.isIgnore());
		}
	}
	class ActionComparator implements Comparator<CreditLog> {
	    public int compare(CreditLog rec1, CreditLog rec2) {
	        return rec1.getAction().compareTo(rec2.getAction());
	    }
	}
	class TimeStampComparator implements Comparator<CreditLog> {
	    public int compare(CreditLog rec1, CreditLog rec2) {
	        return (int)(rec1.getTimeStamp() - rec2.getTimeStamp());
	    }
	}
	
	@Override
	public boolean isMatch(CreditLog item, CreditLog match) {
		if (match.getStore() != null){
			if (!match.getStore().equals(item.getStore())) return false;
		}
		if (match.getDate() != null){
			if (!match.getDate().equals(item.getDate())) return false;
		}
		if (match.getAmount() != null){
			if (!match.getAmount().getCents().equals(item.getAmount().getCents())) return false;
		}
		if (match.getLocation() != null){
			if (!match.getLocation().equals(item.getLocation())) return false;
		}
		if (match.getMajor() != null){
			if (!match.getMajor().equals(item.getMajor())) return false;
		}
		if (match.getMinor() != null){
			if (!match.getMinor().equals(item.getMinor())) return false;
		}
		if (match.isIgnore()!= null){
			if (!match.isIgnore().equals(item.isIgnore())) return false;
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
	public CreditLog copy(CreditLog item){
		Currency c = Currency.createCents(item.getAmount().getCents());
		return  new CreditLog(item.getTimeStamp(),
				item.getAction(),
				item.getStore(),
				item.getDate(),
				c,
				item.getLocation(),
				item.getMajor(),
				item.getMinor(),
				item.isIgnore());
	}

	@Override
	public void replace(CreditLog item, CreditLog value) {
		if (value.getStore() != null){
			item.setStore(value.getStore());
		}
		if (value.getDate() != null){
			item.setDate(value.getDate());
		}
		if (value.getAmount() != null){
			item.getAmount().setCents(value.getAmount().getCents());
		}
		if (value.getLocation() != null){
			item.setLocation(value.getLocation());
		}
		if (value.getMajor() != null){
			item.setMajor(value.getMajor());
		}
		if (value.getMinor() != null){
			item.setMinor(value.getMinor());
		}
		if (value.isIgnore() != null){
			item.setIgnore(value.isIgnore());
		}
		if (value.getAction() != null){
			item.setAction(value.getAction());
		}
		if (value.getTimeStamp() != null){
			item.setTimeStamp(value.getTimeStamp());
		}
		
	}

	@Override
	public Iterator<CreditLog> iterator() {
		return getIterator();
	}
}
