package com.carbone.main;

import javax.swing.JFileChooser;

public final class Global {
	public final static String EMPTY = "BLANK";
	
	//TimeStamp Column in any Log File
	public final static String TIME_STAMP_CSV_FORMAT = "yyyy MMM dd HH:mm:ss";
	//TimeStamp suffix on backup file
	public final static String TIME_STAMP_FILE_FORMAT = "yyyy_MMM_dd_HH_mm_ss";
	//Date format in Credit files
	public final static String TIME_STAMP_CREDIT_FORMAT = "mm/dd/yyyy";
	
	public final static String MY_PATH = 
			new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + "\\Finance\\";
	public final static String INIT_PATH = MY_PATH + "InitialData\\";
	public final static String CURRENT_PATH = MY_PATH + "CurrentData\\";
	public final static String TEST_PATH = MY_PATH + "TestDirectory\\";
	public final static String GOLDEN_PATH = MY_PATH + "GoldFiles\\";
	public final static String BACKUP_PATH = MY_PATH + "Backup\\";
	
	public final static boolean VERBOSE = true;
	
	public final static String STORE_ID = CURRENT_PATH + "StoreId.csv";
	public final static String STORE_ID_LOG = CURRENT_PATH + "StoreIdLog.csv";
	public final static String MAJOR = CURRENT_PATH + "Major.csv";
	public final static String MAJOR_LOG = CURRENT_PATH + "MajorLog.csv";
	public final static String MINOR = CURRENT_PATH + "Minor.csv";
	public final static String MINOR_LOG = CURRENT_PATH + "MinorLog.csv";
	public final static String CREDIT_MAP = CURRENT_PATH + "CreditMap.csv";
	public final static String CREDIT_MAP_LOG = CURRENT_PATH + "CreditMapLog.csv";
	public final static String CREDIT = CURRENT_PATH + "Credit.csv";
	public final static String CREDIT_LOG = CURRENT_PATH + "CreditLog.csv";
	public final static String STORES = CURRENT_PATH + "Stores.csv";
	
	public final static String GOLDEN_STORE_ID = GOLDEN_PATH + "StoreId_2.csv";
	public final static String GOLDEN_MAJOR = GOLDEN_PATH + "Major.csv";
	public final static String GOLDEN_MINOR = GOLDEN_PATH + "Minor.csv";
	public final static String GOLDEN_CREDIT_MAP = GOLDEN_PATH + "CreditMap.csv";
	public final static String GOLDEN_CREDIT = GOLDEN_PATH + "Credit.csv";
}
