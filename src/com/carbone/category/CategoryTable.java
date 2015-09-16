package com.carbone.category;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import com.carbone.dataset.CSVentryType;
import com.carbone.dataset.DataSet;
import com.carbone.utils.CustomException;

public class CategoryTable extends DataSet<Category> implements Iterable<Category>{
	
	final static String [] colNames = {
		"Category"
	};
	
	final static CSVentryType [] colTypes = {
		CSVentryType.STRING
	};
	
	public CategoryTable (String tableName){
		super(colNames, colTypes, tableName + "<Category>");
	}
	
	@Override
	public String makeRow(Category row){
		return row.getName() + "\n";
	}
	
	@Override
	public Category parseRow(String [] tokens) throws CustomException{
		if (tokens.length != colNames.length) throw new CustomException("CSV File does not match template");
		return new Category(tokens[0]);
	}
	
	@Override
	public Comparator<Category> getComparator(String name) throws CustomException{
		int idx = Arrays.asList(colNames).indexOf(name);
		switch(idx){
		case 0:		//Category
			return new CategoryComparator();
		default:
			throw new CustomException("Illegal Compare Key");
		}
	}
	class CategoryComparator implements Comparator<Category> {
	    public int compare(Category rec1, Category rec2) {
	        return rec1.getName().compareTo(rec2.getName());
	    }
	}
	@Override
	public boolean isMatch(Category item, Category match) {
		if (match.getName() != null){
			if (!match.getName().equals(item.getName())) return false;
		}
		return true;
	}
	@Override
	public Category copy(Category item){
		return new Category(item.getName());
	}

	@Override
	public void replace(Category item, Category value) {
		if (value.getName() != null){
			item.setName(value.getName());
		}
		
	}

	@Override
	public Iterator<Category> iterator() {
		return getIterator();
	}

}
