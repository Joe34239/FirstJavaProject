package com.carbone.credit;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import com.carbone.dataset.CSVentryType;
import com.carbone.dataset.DataSet;
import com.carbone.main.Global;
import com.carbone.utils.Currency;
import com.carbone.utils.CustomException;
import com.carbone.utils.TimeStamp;
// TODO Need to use Dirty superclass, Label in tostring, return arraylist of dirty
public class CreditTable extends DataSet<Credit> implements Iterable<Credit>{
	
	final static String [] colNames = {
		"Store",
		"Date",
		"Amount",
		"Location",
		"Major",
		"Minor",
		"Ignore"
	};

	final static CSVentryType [] colTypes = {
		CSVentryType.STRING,
		CSVentryType.TIMESTAMP,
		CSVentryType.CURRENCY,
		CSVentryType.STRING,
		CSVentryType.STRING,
		CSVentryType.STRING,
		CSVentryType.BOOLEAN
	};
	
	public CreditTable (String tableName){
		super(colNames, colTypes, tableName + "<Credit>");
	}
	
	@Override
	public String makeRow(Credit row){
		return row.getStore() + "," + 
				   TimeStamp.toString(row.getDate(), Global.TIME_STAMP_CREDIT_FORMAT) + "," + 
				   row.getAmount().toString() + "," + 
				   row.getLocation() + "," + 
				   row.getMajor() + "," + 
				   row.getMinor() + "," + 
				   row.isIgnore() + "\n";
	}
	
	@Override
	public Credit parseRow(String [] tokens) throws CustomException{
		if (tokens.length != colNames.length) throw new CustomException("CSV File does not match template");
		return Credit.createCredit(tokens[0],						//Store
				TimeStamp.fromString(tokens[1], Global.TIME_STAMP_CREDIT_FORMAT),	//Date
				Currency.create(tokens[2]),					//Amount
				tokens[3],									//Location
				tokens[4],									//Major
				tokens[5],									//Minor
				Boolean.parseBoolean(tokens[6]));			//Ignore
	}
	
	@Override
	public Comparator<Credit> getComparator(String name) throws CustomException{
		int idx = Arrays.asList(colNames).indexOf(name);
		switch(idx){
		case 0:		//Store
			return new StoreComparator();
		case 1:		//Date
			return new DateComparator();
		case 2:		//Amount
			return new AmountComparator();
		case 3:		//Location
			return new LocationComparator();
		case 4:		//Major
			return new MajorComparator();
		case 5:		//Minor
			return new MinorComparator();
		case 6:		//Ignore
			return new IgnoreComparator();
		default:
			throw new CustomException("Illegal Compare Key");
		}
	}
	class StoreComparator implements Comparator<Credit> {
		public int compare(Credit rec1, Credit rec2) {
			return rec1.getStore().compareTo(rec2.getStore());
		}
	}
	class DateComparator implements Comparator<Credit> {
		public int compare(Credit rec1, Credit rec2) {
			return rec1.getDate().compareTo(rec2.getDate());
		}
	}
	class AmountComparator implements Comparator<Credit> {
		public int compare(Credit rec1, Credit rec2) {
			return rec1.getAmount().getCents().compareTo(rec2.getAmount().getCents());
		}
	}
	class LocationComparator implements Comparator<Credit> {
		public int compare(Credit rec1, Credit rec2) {
			return rec1.getLocation().compareTo(rec2.getLocation());
		}
	}
	class MajorComparator implements Comparator<Credit> {
		public int compare(Credit rec1, Credit rec2) {
			return rec1.getMajor().compareTo(rec2.getMajor());
		}
	}
	class MinorComparator implements Comparator<Credit> {
		public int compare(Credit rec1, Credit rec2) {
			return rec1.getMinor().compareTo(rec2.getMinor());
		}
	}
	class IgnoreComparator implements Comparator<Credit> {
		public int compare(Credit rec1, Credit rec2) {
			return rec1.isIgnore().compareTo(rec2.isIgnore());
		}
	}
	
	@Override
	public boolean isMatch(Credit item, Credit match) {

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
		return true;
	}
	@Override
	public Credit copy(Credit item){
		Currency c = Currency.createCents(item.getAmount().getCents());
		return  Credit.createCredit(item.getStore(),
				item.getDate(),
				c,
				item.getLocation(),
				item.getMajor(),
				item.getMinor(),
				item.isIgnore());
	}

	@Override
	public void replace(Credit item, Credit value) {
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
	}

	@Override
	public Iterator<Credit> iterator() {
		return getIterator();
	}

}
