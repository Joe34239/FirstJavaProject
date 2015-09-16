package com.carbone.creditMap;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import com.carbone.dataset.CSVentryType;
import com.carbone.dataset.DataSet;
import com.carbone.utils.CustomException;
// TODO Need to use Dirty superclass, Label in tostring, return arraylist of dirty
public class CreditMapTable extends DataSet<CreditMap> implements Iterable<CreditMap>{
	
	final static String [] colNames = {
		"Tag",
		"Major",
		"Minor"
	};
	
	final static CSVentryType [] colTypes = {
		CSVentryType.STRING,
		CSVentryType.STRING,
		CSVentryType.STRING
	};
	
	public CreditMapTable (String tableName){
		super(colNames, colTypes, tableName + "<CreditMap>");
	}
	
	@Override
	public String makeRow(CreditMap row){
		return row.getTag() + "," + row.getMajor() + "," + row.getMinor() + "\n";
	}
	
	@Override
	public CreditMap parseRow(String [] tokens) throws CustomException{
		if (tokens.length != colNames.length) throw new CustomException("CSV File does not match template");
		return new CreditMap(tokens[0],tokens[1],tokens[2]);
	}
	
	@Override
	public Comparator<CreditMap> getComparator(String name) throws CustomException{
		int idx = Arrays.asList(colNames).indexOf(name);
		switch(idx){
		case 0:		//Tag
			return new TagComparator();
		case 1:		//Major
			return new MajorComparator();
		case 2:		//Minor
			return new MinorComparator();
		default:
			throw new CustomException("Illegal Compare Key");
		}
	}
	class TagComparator implements Comparator<CreditMap> {
		public int compare(CreditMap rec1, CreditMap rec2) {
			return rec1.getTag().compareTo(rec2.getTag());
		}
	}
	class MajorComparator implements Comparator<CreditMap> {
		public int compare(CreditMap rec1, CreditMap rec2) {
			return rec1.getMajor().compareTo(rec2.getMajor());
		}
	}
	class MinorComparator implements Comparator<CreditMap> {
		public int compare(CreditMap rec1, CreditMap rec2) {
			return rec1.getMinor().compareTo(rec2.getMinor());
		}

	}
	
	@Override
	public boolean isMatch(CreditMap item, CreditMap match) {
		// Match ALL fields in this table
		if (match.getTag() != null){
			if (!match.getTag().equals(item.getTag())) return false;
		}
		if (match.getMajor() != null){
			if (!match.getMajor().equals(item.getMajor())) return false;
		}
		if (match.getMinor() != null){
			if (!match.getMinor().equals(item.getMinor())) return false;
		}
		return true;
	}
	@Override
	public CreditMap copy(CreditMap item){
		return new CreditMap(item.getTag(), item.getMajor(), item.getMinor());
	}

	@Override
	public void replace(CreditMap item, CreditMap value) {
		if (value.getTag() != null){
			item.setTag(value.getTag());
		}
		if (value.getMajor() != null){
			item.setMajor(value.getMajor());
		}
		if (value.getMinor() != null){
			item.setMinor(value.getMinor());
		}
	}

	@Override
	public Iterator<CreditMap> iterator() {
		return getIterator();
	}

}
