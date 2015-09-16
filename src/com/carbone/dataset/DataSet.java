package com.carbone.dataset;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import com.carbone.main.Global;
import com.carbone.utils.CustomException;
import com.carbone.utils.TimeStamp;
//  TODO Need to implement Dirty count as a new subclass
public abstract class DataSet<T> implements Iterable<T>{
	protected final ArrayList<T> mData;
	protected final String [] mNames;
	protected boolean mIsOpen;
	protected boolean mIsReadOnly;
	protected String mFileName;
	protected boolean mBackup;
	private final String mDBname;
	private final CSVentryType [] mCSVtypes;


	public DataSet(String [] names, CSVentryType [] types, String DBname){
		mNames = names;
		mCSVtypes = types;
		mDBname = DBname;
		mData = new ArrayList<T>();
		mIsOpen = false;
	}
	
	public abstract String makeRow(T row);
	public abstract T parseRow(String[] tokens) throws CustomException;
	public abstract Comparator<T> getComparator(String name) throws CustomException;
	public abstract boolean isMatch(T item, T match);
	public abstract T copy(T item);
	public abstract void replace(T item, T value);
	
	protected String getDBname() {
		return mDBname;
	}	
	public String [] getNames() {
		return mNames;
	}
	public CSVentryType [] getCSVtypes() {
		return mCSVtypes;
	}
	public int getSize(){
		return mData.size();
	}
	
	public Iterator<T> getIterator(){
		return mData.iterator();
	}
	
	public void add(T newRow){
		mData.add(newRow);
	}	
	public void clear(){
		mData.clear();
	}
	public T getItem(int index){
	if (index < 0 | index >= mData.size())
		throw new IndexOutOfBoundsException("CSV Table Index Error");
	return mData.get(index);
	}
	
	public void open(String fileName){open(fileName,false,false);}
	public void openWithBackup(String fileName){open(fileName,false,true);}
	public void openReadOnly(String fileName){open(fileName,true,false);}
	protected void open(String fileName,boolean readOnly, boolean backup){
		mIsOpen = true;
		mIsReadOnly = readOnly;
		mFileName = fileName;
		mBackup = backup;
		if (mBackup){
			// TODO this should be saved in the backup directory
	        File source = new File(fileName);
	        String out = fileName.substring(0, fileName.length()-4) + "_" +
	        		TimeStamp.toString(System.currentTimeMillis(), Global.TIME_STAMP_FILE_FORMAT) + 
	        		fileName.substring(fileName.length()-4);
	        File dest = new File(out);
	        try{
	        	Files.copy(source.toPath(), dest.toPath());
	        }
	        catch (IOException e){
	        	e.printStackTrace();
	        }
		}
		try {
			readCsvFile(fileName);
		} catch (IOException e) {
			System.exit(0); //Stop execution in its tracks!!!!!!!!!
		}
	}	
	public void create(String fileName){
		// TODO add header and writw
		saveCsvFile(fileName);
		mIsOpen = false; // Needs to be re-opened for close to write it again
		mFileName = null;
	}
	
	public void close(){
		if (mIsOpen & !mIsReadOnly){
			saveCsvFile(mFileName);
		}
		mIsOpen = false;
		mFileName = null;
	}
	
	public void save(){
		if (mIsOpen & !mIsReadOnly){
			saveCsvFile(mFileName);
		}
	}
	public ArrayList<Integer> contains (T match){
		ArrayList<Integer> results = new ArrayList<>();
		for (int i = 0; i <  mData.size(); i++){
			if (isMatch(mData.get(i),match)){
				results.add(i);
			}
		}
		return results;
	}
	public int deleteAll (T match){return delete(match,true);}
	public int deleteFirst (T match){return delete(match,false);}
	public int delete (T match,boolean allFlag){
		ArrayList<Integer> results = contains(match);
		int i; // For weird remove behavior
		if (allFlag){
			for (int j = results.size()-1; j >= 0; j--){
				i = results.get(j);
				mData.remove(i);
			}
			return results.size();
			
		} else {
			if (results.size() > 0){
				i = results.get(0);
				mData.remove(i);
				return 1;
				
			} else return 0;	
		}
	}
	public ArrayList<T> fetchAll (T match){return fetch(match,true);}
	public ArrayList<T> fetchFirst (T match){return fetch(match,false);}
	public ArrayList<T> fetch (T match, boolean allFlag){
		ArrayList<T> results = new ArrayList<>();
		ArrayList<Integer> idx = contains(match);
		for (int j = 0; j < idx.size(); j++){
			results.add(copy(mData.get(idx.get(j))));
			if (!allFlag) break;
		}
		return results;
	}
	public int replaceAll (T match,T value){return replace(match,value,true);}
	public int replaceFirst (T match,T value){return replace(match,value, false);}
	private int replace (T match,T value,boolean allFlag){
		ArrayList<Integer> results = contains(match);
		if (allFlag){
			for (int j = results.size()-1; j >= 0; j--){
				replace(mData.get(results.get(j)), value);
			}
			return results.size();
			
		} else {
			if (results.size() > 0){
				replace(mData.get(results.get(0)), value);
				return 1;
				
			} else return 0;	
		}
	}

	public void sort(Comparator<T> sortKey){
		Collections.sort(mData, sortKey);
	}
	protected void saveCsvFile(String sFileName)
	{
		try
		{
			FileWriter writer = new FileWriter(sFileName);
			
			boolean firstTime = true;
			StringBuilder sb = new StringBuilder();
			for (String ss : mNames){
				if (!firstTime) sb.append(",");
				sb.append(ss);
				firstTime = false;
			}
			writer.append(sb + "\n");
			
			for (int i = 0; i < mData.size(); i++){
				T row = mData.get(i);
				writer.append(makeRow(row)); 
			}


			writer.flush();
			writer.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		} 
	}
	protected void readCsvFile(String sFileName) throws IOException
	{
		boolean firstLine = true;
        BufferedReader fileReader = null;       
        final String DELIMITER = ",";
        mData.clear();
        try
        {
            String line = "";
            //Create the file reader
            fileReader = new BufferedReader(new FileReader(sFileName));
             
            //Read the file line by line
            while ((line = fileReader.readLine()) != null)
            {
                //Get all tokens available in line
                String[] tokens = line.split(DELIMITER);

                if (firstLine){
                	if (tokens.length != mNames.length) 
                		throw new CustomException("CSV File does not match template");
                	for (int i = 0; i < tokens.length; i++){
                		if (!tokens[i].equals(mNames[i])) 
                			throw new CustomException("CSV File does not match template");
                	}
                	
                } else {
                	mData.add(parseRow(tokens));
                }
                
                firstLine = false;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(0); //Stop execution in its tracks!!!!!!!!!
        }
        finally
        {
            try {
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
	}
	
	public void dumpTable(){ dumpLines(0);}
		
	public void dumpLines(int lines){
		System.out.print(toString()+"\n");
		for (int i = 0; i < mData.size(); i++){
			T row = mData.get(i);
			System.out.print("[" + i + "]" + makeRow(row)); 
			lines--;
			if (lines == 0) return;
		}
	}
	public static String CSVoutTimeStamp(long time){
		return TimeStamp.toString(time, Global.TIME_STAMP_CSV_FORMAT);
	}	
	public static String CSVoutInt(int val){	
		String s = String.valueOf(val/100) + "." + String.valueOf(val%100) ;
		return s;
	}	

	public String toString(){
		StringBuilder s = new StringBuilder(getDBname());
		s.append("{" + (mIsOpen?"open":"closed") + (mIsReadOnly?",readOnly}":"}") );
		s.append("[" + mData.size() +']');
		for (String c : mNames){
			s.append(c + ',');
		}
		return s.substring(0, s.length()-1).toString();
	}	
}
