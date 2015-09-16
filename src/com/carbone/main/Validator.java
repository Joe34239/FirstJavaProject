package com.carbone.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import com.carbone.category.Category;
import com.carbone.category.CategoryTable;
import com.carbone.creditMap.CreditMap;
import com.carbone.creditMap.CreditMapTable;
import com.carbone.storeId.StoreId;
import com.carbone.storeId.StoreIdTable;
import com.carbone.utils.Log;

public class Validator {
	protected final static String TAG = "Validator";
	protected static BufferedReader fileReader1 = null; 
	protected static BufferedReader fileReader2 = null;

	public static int storeId(StoreIdTable storeIdTable){
		int errors = 0;
		//Validate that all OldNames are unique
		//Validate that all NewNames are not blank
		HashMap<String,Integer> dups = new HashMap<String, Integer>();

		// Check for duplicate entries
		int entries = 0;
		for (Iterator<StoreId> iterator1 = storeIdTable.iterator(); iterator1.hasNext();){
			StoreId item1 = iterator1.next();
			entries++;
			String line = item1.getOldName();
			if (!dups.containsKey(line)){
				dups.put(line, entries);
			} else {
				errors++;
				Log.e(TAG, "Duplicate Oldname Field" + line + "(" + entries + "," + dups.get(line) + ")");
			}
			if (item1.getNewName().equals("")){
				Log.e(TAG, "Blank New Name Field" + line + "(" + entries + "," + dups.get(line) + ")");
			}

		}

		if (errors == 0){
			Log.i(TAG, "StoreId has no errors");
		}
		return errors;

	}

	public static int category(CategoryTable categoryTable, String name){
		int errors = 0;
		HashMap<String,Integer> dups = new HashMap<String, Integer>();

		// Check for duplicate entries
		int entries = 0;
		for (Iterator<Category> iterator1 = categoryTable.iterator(); iterator1.hasNext();){
			Category item1 = iterator1.next();
			entries++;
			String line = item1.getName();
			if (!dups.containsKey(line)){
				dups.put(line, entries);
			} else {
				errors++;
				Log.e(TAG, "Duplicate Category Entry" + line + "(" + entries + "," + dups.get(line) + ")");
			}

		}

		if (errors == 0){
			Log.i(TAG, name + " has no errors");
		}
		return errors;
	}

	public static int creditMap(
			CreditMapTable creditMapTable, 
			StoreIdTable stores, 
			CategoryTable major, 
			CategoryTable minor){
		int errors = 0;
		HashMap<String,Integer> dups = new HashMap<String, Integer>();

		// Check for duplicate entries
		int entries = 0;
		for (Iterator<CreditMap> iterator1 = creditMapTable.iterator(); iterator1.hasNext();){
			CreditMap item1 = iterator1.next();
			entries++;
			String line = item1.getTag() + ":" + item1.getMajor() + ":" + item1.getMinor();
			if (!dups.containsKey(line)){
				dups.put(line, entries);
			} else {
				errors++;
				Log.e(TAG, "Duplicate CreditMap Entry" + line + "(" + entries + "," + dups.get(line) + ")");
			}
			//Check that Major and Minor exists
			if (!Global.EMPTY.equals(item1.getMajor())){
				Category match = new Category(item1.getMajor());
				if (major.contains(match).isEmpty()){
					Log.w(TAG, "Line " + entries + ": Major [" + item1.getMajor() + "] missing in Major");
				}
			}
			if (!Global.EMPTY.equals(item1.getMinor())){
				Category match1 = new Category(item1.getMinor());
				if (minor.contains(match1).isEmpty()){
					Log.w(TAG, "Line " + entries + ": Minor [" + item1.getMinor() + "] missing in Minor");
				}
			}
			//Check that entry is in StorID as a newName TODO is this true???
//			StoreId matchStore = new StoreId();
//			matchStore.setNewName(item1.getTag());
//			if (stores.contains(matchStore).isEmpty()){
//				errors++;
//				Log.e(TAG, "Line " + entries + ": Tag [" + item1.getTag() + "] missing in Stores");
//			}
		}
		// TODO Check that newName entry in StorID has at least one entry in CreditMap
		CreditMap match = new CreditMap();
		for (StoreId store : stores){
			match.setTag(store.getNewName());
			if (creditMapTable.contains(match).isEmpty()){
				Log.w(TAG, "Store [" + store.getNewName() + "] never used in Creditmap");
			}
			
		}
		if (errors == 0){
			Log.i(TAG, "CreditMap has no errors");
		}
		return errors;

	}

	public static boolean fileCompare(String file1, String file2, String name1, String name2){

		try
		{
			fileReader1 = new BufferedReader(new FileReader(file1));
			fileReader2 = new BufferedReader(new FileReader(file2));
			String line1,line2;
			while ((line1 = fileReader1.readLine()) != null){
				if((line2 = fileReader2.readLine()) == null){
					closeFiles();  
					Log.e(TAG,name2 + " is missing line(s)");
					return false;
				}
				if (!line1.equals(line2)){
					closeFiles();
					Log.e(TAG,"Mismatch: "+ name1 + "=>" + line1);
					Log.e(TAG,"Mismatch: "+ name2 + " =>" + line2);
					return false;
				}
			}
			if (fileReader2.readLine() != null){
				closeFiles();
				Log.e(TAG,name1 + " is missing line(s)");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return closeFiles();
	}
	private static boolean closeFiles(){
		try {
			fileReader1.close();
			fileReader2.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} 
		return true;
	}
}
