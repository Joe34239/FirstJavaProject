package com.carbone.creditMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import com.carbone.main.Finance;
import com.carbone.storeId.StoreIdTable;
import com.carbone.utils.CustomException;

public final class CreditMapUpRev {

	public static boolean upRev(String inFile, String outFile){

		// This transform performs the following:
		// 1) removes Words and Ignore columns.
		// 2) detects any duplicates
		// 3) calls creditReplace which:
		//    a) performs Regex reductions on name
		//	  b) applies StoredId transformation
		
		StoreIdTable st = Finance.getStoreId();

		BufferedReader fileReader = null; 
		FileWriter writer = null;
		final String DELIMITER = ",";

		try
		{
			fileReader = new BufferedReader(new FileReader(inFile));			
			writer = new FileWriter(outFile);
			String line;
			Integer count = 0;	//line count, not really used
			HashMap<String,Integer> dups = new HashMap<String, Integer>();
			
			//Read the file line by line
			while ((line = fileReader.readLine()) != null)
			{
				//Get all tokens available in line
				String[] tokens = line.split(DELIMITER);

				if (tokens.length != 5) 
					throw new CustomException("CreditMapINI File does not match template");
				
				// This will also try to lookup "Tag" from the title line, which returns "Tag"
				String out = st.creditLookup(tokens[0])  + "," + tokens[3]  +  "," + tokens[4] + "\n";
				if (!dups.containsKey(out)){
					count++;
					dups.put(out, count);
					writer.append(out);
				}
			}
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
		return true;
	}

}
