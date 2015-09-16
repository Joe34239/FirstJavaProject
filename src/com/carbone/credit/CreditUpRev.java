package com.carbone.credit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.carbone.category.Category;
import com.carbone.category.CategoryTable;
import com.carbone.creditMap.CreditMap;
import com.carbone.creditMap.CreditMapTable;
import com.carbone.main.Finance;
import com.carbone.main.Global;
import com.carbone.storeId.StoreIdTable;
import com.carbone.utils.Currency;
import com.carbone.utils.CustomException;
import com.carbone.utils.Log;
import com.carbone.utils.Stripper;
import com.carbone.utils.TimeStamp;

public class CreditUpRev {
	public final static String TAG = "CreditUpRev";
	
	public static boolean upRevPdf(String inFile, String outFile, String storesFileName){
		/**
		 *	The following transformations are applied to create Credit from Initial PDF
		 *		Read in Initial PDF and ...
		 *  		a) Create Location from normal and PDF cleanup
		 *  		b) Negate amount and make sure it is a float representation
		 *  		c) Set Major and Minor to BLANK and Ignore to FALSE
		 *		Write out initial Credit data base
		 *      Verify that every Location encountered is in CreditMap
		 */
		StoreIdTable st = Finance.getStoreId();
		CreditMapTable cm = Finance.getCreditMap();

		BufferedReader fileReader = null; 
		FileWriter writer = null;
		CategoryTable stores = new CategoryTable("Stores");
		stores.create(storesFileName);
		stores.open(storesFileName);
		final String DELIMITER = ",";
		HashMap<String,Integer> storeDups = new HashMap<String, Integer>();
		HashMap<String,Integer> transDups = new HashMap<String, Integer>();
		CreditMap match = new CreditMap();
		Date earliestDate= new Date();
		String earliestStore="";
		Currency earliestAmount = Currency.create();
		Date latestDate=new Date();
		String latestStore="";
		Currency latestAmount = Currency.create();
		Integer count = 0;	//output line count
		int errors = 0;
		Integer inpCount = 0; //Input line count

		try
		{
			fileReader = new BufferedReader(new FileReader(inFile));			
			writer = new FileWriter(outFile);
			String line;
			writer.append("Store,Date,Amount,Location,Major,Minor,Ignore\n");

			//Read the file line by line
			while ((line = fileReader.readLine()) != null)
			{
				//Get all tokens available in line
				String[] tokens = line.split(DELIMITER);
				inpCount++;

				if (!(tokens.length == 7 | tokens.length == 4))
					throw new CustomException("InitialPdfINI File does not match template");
				if (tokens.length == 4){
					// The 7 is the first line
					String location = st.creditLookup(Stripper.pdfStrip(tokens[1]));
					if (!storeDups.containsKey(location)){
						count++;
						storeDups.put(location, count);
					}
					Double amount = -Double.parseDouble(tokens[2]);
					tokens[2] = String.format("%.2f",amount);

					String creditKey = tokens[1] + tokens[0] + tokens[2];	// key is Store + Date + Amount
					while (transDups.containsKey(creditKey)){
						Log.i(TAG, "Duplicate keys [" + creditKey + "] lines -> " + transDups.get(creditKey) + "," + inpCount + ". Adding -X to name");
						tokens[1] = tokens[1] + "-X";
						creditKey = tokens[1] + tokens[0] + tokens[2];	// key is Store + Date + Amount
					}
					transDups.put(creditKey, inpCount);
					writer.append(tokens[1] + "," +		//Store
							tokens[0] + "," +			//date
							tokens[2] + "," +			//amount
							location + "," + 			//location
							"BLANK" + "," +				//major
							"BLANK" + "," +				//minor
							tokens[3] +					//ignore
							"\n");
					
					if (inpCount == 2){
						earliestDate = new Date(TimeStamp.fromString(tokens[0],Global.TIME_STAMP_CREDIT_FORMAT));
						earliestStore = tokens[1];
						earliestAmount = Currency.create(tokens[2]);
						latestDate = new Date(TimeStamp.fromString(tokens[0],Global.TIME_STAMP_CREDIT_FORMAT));
						latestStore = tokens[1];
						latestAmount = Currency.create(tokens[2]);
					} else {
						if (earliestDate.after( new Date(TimeStamp.fromString(tokens[0],Global.TIME_STAMP_CREDIT_FORMAT)))){
							earliestDate = new Date(TimeStamp.fromString(tokens[0],Global.TIME_STAMP_CREDIT_FORMAT));
							earliestStore = tokens[1];
							earliestAmount = Currency.create(tokens[2]);
						}
						if (latestDate.before(new Date(TimeStamp.fromString(tokens[0],Global.TIME_STAMP_CREDIT_FORMAT)))){
							latestDate = new Date(TimeStamp.fromString(tokens[0],Global.TIME_STAMP_CREDIT_FORMAT));
							latestStore = tokens[1];
							latestAmount = Currency.create(tokens[2]);
						}
					}
				}
			}
			for (String key : storeDups.keySet()){
				stores.add(new Category(key));
				match.setTag(key);
				if (cm.contains(match).size() == 0){
					Log.e(TAG, "pdfStore not in CreditMap:[" + key + "] line ->" + inpCount);
					errors++;
				}
			}
			if (errors > 0) return false;
			stores.sort(stores.getComparator("Category"));
			stores.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally
		{
			try {
				fileReader.close();
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		Log.i(TAG, "There are " + inpCount + " lines in Initial PDF data");
		Log.i(TAG, "There are " + count + " unique store categories in Initial PDF data");
		Log.i(TAG, "Start: " + TimeStamp.toString(
				earliestDate.getTime(), Global.TIME_STAMP_CREDIT_FORMAT)+ " " + earliestStore + " " + earliestAmount.toString());
		Log.i(TAG, "  End: " + TimeStamp.toString(
				latestDate.getTime(), Global.TIME_STAMP_CREDIT_FORMAT)+ " " + latestStore + " " + latestAmount.toString());
		return true;
	}

	public static boolean upRevActivity(String inFile, CreditTable charges, CategoryTable stores){
		/**
		 * 	The following transformations are applied to create Credit from  ActivityINI
		 *	3)Read in ActivityINI and ...
		 *  	a) Skip "PAYMENT" rows
		 *  	b) Create Location from normal cleanup
		 *  	c) Negate amount and make sure it is a float representation
		 *	4)Verify that every Location encountered is in CreditMap
		 *	5)Post new activity to credit data base. This depends on not matching
		 *		date, amount and Location, (not store)
		 *	6)Make sure all Store Names are unique, appending '-X... as needed
		 */
		
		StoreIdTable st = Finance.getStoreId();
		CreditMapTable cm = Finance.getCreditMap();

		BufferedReader fileReader = null; 
		final String DELIMITER = ",";
		HashMap<String,Integer> dups = new HashMap<String, Integer>();
		CreditMap match = new CreditMap();
		Integer count = 0;	//output line count
		Integer inpCount = 0; //Input line count
		ArrayList<Credit> activities = new ArrayList<Credit>();
		Category storeMatch = new Category();
		Long earliestDate=0L;
		String earliestStore="";
		Currency earliestAmount = Currency.create();
		Long latestDate=0L;
		String latestStore="";
		Currency latestAmount = Currency.create();
		try
		{
			fileReader = new BufferedReader(new FileReader(inFile));			
			String line;

			//Read the file line by line
			while ((line = fileReader.readLine()) != null)
			{
				//Get all tokens available in line
				String[] tokens = line.split(DELIMITER);
				inpCount++;
				if (tokens.length < 5)
					throw new CustomException("ActivityINI File does not match template");
				if ((inpCount > 1) & (!tokens[0].equals("PAYMENT"))){

					Credit credit = Credit.createCredit();
					credit.setAmount(Currency.create(tokens[tokens.length-1])); // TODO do we need to fix floats
					credit.setDate(TimeStamp.fromString(tokens[1],Global.TIME_STAMP_CREDIT_FORMAT));
					credit.setIgnore(false);
					credit.setMajor(Global.EMPTY);
					credit.setMinor(Global.EMPTY);
					credit.setStore(tokens[3]);
					for (int i = 4; i < tokens.length-1; i++){
						credit.setStore(credit.getStore() + " " + tokens[i]);
					}
					credit.setStore(credit.getStore().replaceAll("\"", ""));
					String location = st.creditLookup(credit.getStore());
					if (!dups.containsKey(location)){
						count++;
						dups.put(location, inpCount);
					}
					credit.setLocation(location);
					// Check and correct for duplicate keys
					Credit creditKey = credit.makeKey();		// key is Store + Date + Amount
					while (!charges.contains(creditKey).isEmpty()){
						Log.i(TAG, "Duplicate keys [" + creditKey.toString() + "] lines -> " + inpCount + ". Adding -X to name");
						credit.setStore(credit.getStore() + "-X");
						creditKey = credit.makeKey();			// key is Store + Date + Amount
					}
					
					activities.add(credit);
					
					if (inpCount == 2){
						earliestDate = credit.getDate();
						earliestStore = credit.getStore();
						earliestAmount.setCents(credit.getAmount().getCents());
						latestDate = credit.getDate();
						latestStore = credit.getStore();
						latestAmount.setCents(credit.getAmount().getCents());
					} else {
						if (earliestDate > credit.getDate()){
							earliestDate = credit.getDate();
							earliestStore = credit.getStore();
							earliestAmount.setCents(credit.getAmount().getCents());
						}
						if (latestDate < credit.getDate()){
							latestDate = credit.getDate();
							latestStore = credit.getStore();
							latestAmount.setCents(credit.getAmount().getCents());
						}
					}
				}
			}
			int errors = 0;
			for (String key : dups.keySet()){
				storeMatch.setName(key);
				if (stores.contains(storeMatch).isEmpty()){
					stores.add(new Category(key));
				}
				match.setTag(key);
				if (cm.contains(match).size() == 0){
					Log.e(TAG, "activity Store not in CreditMap:[" + key + "] line ->" + dups.get(key));
					errors++;
				}
			}
			if (errors > 0) return false;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally
		{
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		Log.i(TAG, "There are " + count + " unique store categories in Initial Activity data");
		try {
			stores.sort(stores.getComparator("Category"));
		} catch (CustomException e) {
			e.printStackTrace();
		}
		stores.save();
		Log.i(TAG, "There are " + inpCount + " lines in Initial Activity data");
		Log.i(TAG, "There are " + stores.getSize() + " unique store categories in complete data set");
		Log.i(TAG, "Start: " + TimeStamp.toString(earliestDate, Global.TIME_STAMP_CREDIT_FORMAT)+ " " + earliestStore + " " + earliestAmount.toString());
		Log.i(TAG, "  End: " + TimeStamp.toString(latestDate, Global.TIME_STAMP_CREDIT_FORMAT)+ " " + latestStore + " " + latestAmount.toString());
		
		charges.save();
		return true;
	}
}
