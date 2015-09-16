package com.carbone.storeId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import com.carbone.dataset.CSVentryType;
import com.carbone.dataset.DataSet;
import com.carbone.utils.CustomException;
import com.carbone.utils.Stripper;

public class StoreIdTable extends DataSet<StoreId> implements Iterable<StoreId>{

	final static String [] colNames = {
		"OldName",
		"NewName"
	};

	final static CSVentryType [] colTypes = {
		CSVentryType.STRING,
		CSVentryType.STRING,
	};

	public StoreIdTable (String tableName){
		super(colNames, colTypes, tableName + "<StoreId>");
	}

	@Override
	public String makeRow(StoreId row){
		return row.getOldName() + "," + row.getNewName() +  "\n";
	}

	@Override
	public StoreId parseRow(String [] tokens) throws CustomException{
		if (tokens.length != colNames.length) throw new CustomException("CSV File does not match template");
		return new StoreId(tokens[0],tokens[1]);
	}

	@Override
	public Comparator<StoreId> getComparator(String name) throws CustomException{
		int idx = Arrays.asList(colNames).indexOf(name);
		switch(idx){
		case 0:		//OldName
			return new OldNameComparator();
		case 1:		//NewName
			return new NewNameComparator();
		default:
			throw new CustomException("Illegal Compare Key");
		}
	}
	class OldNameComparator implements Comparator<StoreId> {
		public int compare(StoreId rec1, StoreId rec2) {
			return rec1.getOldName().compareTo(rec2.getOldName());
		}
	}
	class NewNameComparator implements Comparator<StoreId> {
		public int compare(StoreId rec1, StoreId rec2) {
			return rec1.getNewName().compareTo(rec2.getNewName());
		}
	}

	@Override
	public boolean isMatch(StoreId item, StoreId match) {
		if (match.getOldName() != null){
			if (!match.getOldName().equals(item.getOldName())) return false;
		}
		if (match.getNewName() != null){
			if (!match.getNewName().equals(item.getNewName())) return false;
		}
		return true;
	}
	@Override
	public StoreId copy(StoreId item){
		return new StoreId(item.getOldName(), item.getNewName());
	}

	@Override
	public void replace(StoreId item, StoreId value) {
		if (value.getOldName() != null){
			item.setOldName(value.getOldName());
		}
		if (value.getNewName() != null){
			item.setNewName(value.getNewName());
		}
	}

	@Override
	public Iterator<StoreId> iterator() {
		return getIterator();
	}

	public String creditLookup(String inp) throws CustomException{
		String stripped = Stripper.creditStrip(inp);
		StoreId match = new StoreId();
		match.setOldName(stripped);
		ArrayList<Integer> item = this.contains(match);
		switch (item.size()){
		case 0:
			return stripped;
		case 1:
			return this.getItem(item.get(0)).getNewName();
		default:
			throw new CustomException("Multiple StoreId's match credit Tag{"+ match.getOldName()+"}");
		}
	}

	public String checkLookup(String inp) throws CustomException{
		String stripped = Stripper.checkStrip(inp);
		StoreId match = new StoreId();
		match.setOldName(stripped);
		ArrayList<Integer> item = this.contains(match);
		switch (item.size()){
		case 0:
			return stripped;
		case 1:
			return this.getItem(item.get(0)).getNewName();
		default:
			throw new CustomException("Multiple StoreId's match check Tag{"+ match.getOldName()+"}");
		}
	}

	public void fixOldnamesAndDeleteDups()throws CustomException{
		Integer count = 0;	//line count, not really used
		HashMap<String,Integer> dups = new HashMap<String, Integer>();
		for (Iterator<StoreId> iterator = mData.iterator(); iterator.hasNext();){
			StoreId item = iterator.next();
			String strippedName = Stripper.creditStrip(item.getOldName());
			if (!dups.containsKey(strippedName)){
				count++;
				dups.put(strippedName, count);
				item.setOldName(strippedName);
			} else{
				iterator.remove();
			}
		}
		for (Iterator<StoreId> iterator1 = mData.iterator(); iterator1.hasNext();){
			StoreId item1 = iterator1.next();
			if (item1.getOldName().equals(item1.getNewName())){
				iterator1.remove();
			}
		}
	}

}
