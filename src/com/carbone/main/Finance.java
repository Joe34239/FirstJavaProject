package com.carbone.main;


import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

import com.carbone.category.CategoryLogTable;
import com.carbone.category.CategoryTable;
import com.carbone.credit.CreditEditor;
import com.carbone.credit.CreditLogTable;
import com.carbone.credit.CreditTable;
import com.carbone.credit.CreditUpRev;
import com.carbone.creditMap.CreditMapEditor;
import com.carbone.creditMap.CreditMapLogTable;
import com.carbone.creditMap.CreditMapTable;
import com.carbone.creditMap.CreditMapUpRev;
import com.carbone.storeId.*;
import com.carbone.utils.CustomException;
import com.carbone.utils.Log;

public class Finance {
	private final static String TAG = "Finance";

	// List of stores	
	private static StoreIdTable mStoreId = new StoreIdTable("Store Id");
	// Initial changes to stores from Python Data
	private static StoreIdLogTable mStoreIdINILog = new StoreIdLogTable("INIT Store Id Log");
	// Changes to stores
	private static StoreIdLogTable mStoreIdLog= new StoreIdLogTable("Store Id Log");

	// List of Majors	
	private static CategoryTable mMajor = new CategoryTable("Majors");
	// Initial changes to Majors from Python Data
	private static CategoryLogTable mMajorINILog = new CategoryLogTable("INIT Majors Log");
	// Changes to Majors
	private static CategoryLogTable mMajorLog= new CategoryLogTable("Majors Log");

	// List of Minors	
	private static CategoryTable mMinor = new CategoryTable("Minors");
	// Initial changes to Minors from Python Data
	private static CategoryLogTable mMinorINILog = new CategoryLogTable("INIT Minors Log");
	// Changes to Minors
	private static CategoryLogTable mMinorLog= new CategoryLogTable("Minors Log");

	// List of CreditMaps	
	private static CreditMapTable mCreditMap = new CreditMapTable("CreditMaps");
	// Initial changes to CreditMaps from Python Data
	private static CreditMapLogTable mCreditMapINILog = new CreditMapLogTable("INIT CreditMaps Log");
	// Changes to CreditMaps
	private static CreditMapLogTable mCreditMapLog= new CreditMapLogTable("CreditMaps Log");
	
	// Credit Card Data	
	private static CreditTable mCredit = new CreditTable("Credit");
	// Initial changes to Credit from Python Data
	private static CreditLogTable mCreditINILog = new CreditLogTable("INIT Credit Log");
	// Changes to CreditMaps
	private static CreditLogTable mCreditLog= new CreditLogTable("Credit Log");
	
	// Credit Card Data	
	private static CategoryTable mStores = new CategoryTable("Stores");


	// DataSet change factories
	private static StoreIdEditor mStoreEdit;
	private static CreditMapEditor mCreditMapEdit;
	private static CreditEditor mCreditEdit;

	public static void main(String[] args) {
		// TODO we need to handle 4 cases (No Init, Init Credit, Init Check, ALL)
		// create a scanner so we can read the command-line input
		Scanner scanner = new Scanner(System.in);
		Log.i(TAG,"Spending Analysis");
		Log.i(TAG,Global.MY_PATH);

		mStoreEdit= new StoreIdEditor();
		mCreditMapEdit = new CreditMapEditor();
		
		System.out.print("Enter INIT to create from Python data>");
		// get their input as a String
		String cmd = scanner.nextLine();
		Boolean fromPython =  cmd.equals("INIT");
		try{
			initializeDatabase(fromPython);
			initializeCommon(fromPython);
			getStoreId().openReadOnly(Global.STORE_ID);	// StoreID for application is opened ReadOnly
			Log.i(TAG,getStoreId().toString());
			Log.i(TAG,getStoreIdLog().toString());
			getMajor().openReadOnly(Global.MAJOR);		// Major for application is opened ReadOnly	
			Log.i(TAG,getMajor().toString());
			getMinor().openReadOnly(Global.MINOR);		// Minor for application is opened ReadOnly
			Log.i(TAG,getMinor().toString());
			
			initializeCreditMapDatabase(fromPython);
			getCreditMap().openReadOnly(Global.CREDIT_MAP);
			Log.i(TAG,getCreditMap().toString());
			Log.i(TAG,getCreditMapLog().toString());
			
			initializeCreditDatabase(fromPython);
		}
		catch  (IOException e) {
			Log.f(TAG, e.getMessage());
			scanner.close();
			return;
		}
		catch  (CustomException e) {
			Log.f(TAG, e.getMessage());
			scanner.close();
			return;
		}

		scanner.close();
		
		Log.i(TAG,"Entering Command Loop");
//		GuiMain.main(null);
		return;
	}

	private static void initializeDatabase(boolean fromPython) throws IOException, CustomException{
		if (fromPython){

			boolean validateGolden;
			Scanner scanner = new Scanner(System.in);
			Log.i(TAG,"Initializing new database from scratch. All current data will be lost\n");
			System.out.print("Enter GO FOR IT to proceed>");
			// get their input as a String
			String cmd = scanner.nextLine();
			scanner.close();
			if (cmd.equals("GO FOR IT")|cmd.equalsIgnoreCase("g")){
				Log.i(TAG,"Deleting all files in CurrentDatabase and copying in initial CSV files");
				FileUtils.cleanDirectory(new File(Global.CURRENT_PATH));
				//    shutil.copy ('StoreIdINI.csv','StoreId.csv') 
				FileUtils.copyFile(new File(Global.INIT_PATH + "StoreIdINI.csv"), new File(Global.STORE_ID)); // Rename
				FileUtils.copyFile(new File(Global.INIT_PATH + "StoreIdLogINI.csv"), new File(Global.CURRENT_PATH+ "StoreIdLogINI.csv"));
				FileUtils.copyFile(new File(Global.INIT_PATH + "MajorINI.csv"), new File(Global.MAJOR));
				FileUtils.copyFile(new File(Global.INIT_PATH + "MinorINI.csv"), new File(Global.MINOR));
				FileUtils.copyFile(new File(Global.INIT_PATH + "CreditMapINI.csv"), new File(Global.CURRENT_PATH + "CreditMapINI.csv"));
				FileUtils.copyFile(new File(Global.INIT_PATH + "CreditMapLogINI _V2.csv"), new File(Global.CURRENT_PATH + "CreditMapLogINI.csv")); // Rename
				FileUtils.copyFile(new File(Global.INIT_PATH + "InitialPdfINI.csv"), new File(Global.CURRENT_PATH + "InitialPdfINI.csv"));
				FileUtils.copyFile(new File(Global.INIT_PATH + "ActivityINI.csv"), new File(Global.CURRENT_PATH + "ActivityINI.csv"));
				validateGolden = Validator.fileCompare(Global.MAJOR, Global.GOLDEN_MAJOR, "Major", "Golden");
				if (!validateGolden) throw new CustomException("Mismatch: Golden Major");
				validateGolden = Validator.fileCompare(Global.MINOR, Global.GOLDEN_MINOR, "Minor", "Golden");
				if (!validateGolden) throw new CustomException("Mismatch: Golden Minor");
			} else {
				scanner.close();
				throw new CustomException("Initialization Aborted");
			}
		}
		return;
	}
	public static void initializeCommon(boolean fromPython) throws CustomException{
		int validateErrors;
		boolean validateGolden;

		if (fromPython){
			getStoreId().open(Global.STORE_ID);
			mStoreIdINILog.openReadOnly(Global.CURRENT_PATH+ "StoreIdLogINI.csv");
			int changes = mStoreEdit.applyLog(getStoreId(), mStoreIdINILog);
			Log.i(TAG,changes + " changes applied to StoreId from StoreIdINI");
			getStoreId().fixOldnamesAndDeleteDups();	// Make sure names use newest Stripper
			getStoreId().save();
			getStoreIdLog().create(Global.STORE_ID_LOG);
			Log.i(TAG,getStoreId().toString());
			Log.i(TAG,getStoreIdLog().toString());
			validateGolden = Validator.fileCompare(Global.STORE_ID, Global.GOLDEN_STORE_ID, "StoreId", "Golden");
			if (!validateGolden) throw new CustomException("Mismatch: Golden StoreId");
		} else {
			getStoreIdLog().open(Global.STORE_ID_LOG);
			getStoreIdLog().clear();
			getStoreIdLog().close();
		}
		getStoreId().close();

		validateErrors = Validator.storeId(getStoreId());
		if (validateErrors> 0) throw new CustomException(validateErrors + " Errors validating StoreId");
		
		getMajor().openReadOnly(Global.MAJOR);
		validateErrors = Validator.category(getMajor(),"Major");
		if (validateErrors> 0) throw new CustomException(validateErrors + " Errors validating Major");
		getMajor().close();
		
		getMinor().openReadOnly(Global.MINOR);
		validateErrors = Validator.category(getMinor(),"Minor");
		if (validateErrors> 0) throw new CustomException(validateErrors + " Errors validating Minor");
		getMajor().close();
	}
	public static void initializeCreditMapDatabase (boolean fromPython) throws CustomException{
		/**
		 *     The following files are used to recreate this database
		 *     		ActivityINI     - Downloaded Credit Card Activity through Nov 2014
		 *     		CredMapINI      - The previous version's namespace mapping
		 *     		CreditMapLogINI - A cleanup file created to correct CreditMapINI
		 *     		InitialPdfINI   - The origonal version of downloaded pdf files
		 *     		MasterINI       - The full credit database
		 *     		MasterLogINI    - The previous version  of logged edits to the data base
		 *     		StoreIdINI      - The previous version of ShortName mapping
		 *     		StoreIdLogINI   - A cleanup file created to correct StoreIdINI
		 */
		int validateErrors;
		boolean validateGolden;

		if (fromPython){
			new CreditMapUpRev();
			CreditMapUpRev.upRev(Global.CURRENT_PATH + "CreditMapINI.csv",Global.CREDIT_MAP);
			getCreditMap().open(Global.CREDIT_MAP);
			mCreditMapINILog.openReadOnly(Global.CURRENT_PATH+ "CreditMapLogINI.csv");
			int changes = mCreditMapEdit.applyLog(getCreditMap(), mCreditMapINILog);
			Log.i(TAG,changes + " changes applied to CreditMap from CreditMapLogINI");
			getCreditMapLog().create(Global.CREDIT_MAP_LOG);
			getCreditMap().close();
			validateGolden = Validator.fileCompare(Global.CREDIT_MAP, Global.GOLDEN_CREDIT_MAP, "CreditMap", "Golden");
			if (!validateGolden) throw new CustomException("Mismatch: Golden CreditMap");
		} 
		getCreditMap().open(Global.CREDIT_MAP);
		validateErrors = Validator.creditMap(getCreditMap(), getStoreId(), getMajor(), getMinor());
		if (validateErrors> 0) throw new CustomException(validateErrors + " Errors validating CreditMap");
		getCreditMap().close();
		getCreditMapLog().open(Global.CREDIT_MAP_LOG);
		getCreditMapLog().clear();
		getCreditMapLog().close();
	}
	public static void initializeCreditDatabase(boolean fromPython)throws CustomException{
		
		if (fromPython){
			if (!CreditUpRev.upRevPdf(Global.CURRENT_PATH + "InitialPdfINI.csv",Global.CREDIT, Global.STORES)){
				throw new CustomException("Creating initial Credit.csv");
			}
			getCredit().open(Global.CREDIT);
			getStores().open(Global.STORES);
			if (!CreditUpRev.upRevActivity(Global.CURRENT_PATH + "ActivityINI.csv",getCredit(), getStores())){
				throw new CustomException("Creating initial Credit.csv");
			}
		}
		
	}
	public void therest(){
		/*
		 *     '''
    Credit Data Base Cleanup
    This section will post Major and Minor categories for all entrys in Credit
        1) First we use CreditMap which should post all entries except those
           cleanName stores that can have multiple entries
        2) Assume that MasterLogINI will correct most of the multiple entries since
           that is what it was for
        3) Use MasterINI to try to fix the ones that are still missing
        4) Apply CreditLogINI to credit which should end up with a clean database
        5) Verify that every Major and Minor field is valid <= MOVED TO COMMIT
    '''  
    #1)
    cnt = 0
    multiCnt = 0
    unknown = []
    multi = {}
    for i in cr:       
        if i[cr.MAJOR] == '' or i[cr.MINOR] == '':
            s = i[cr.CLEANNAME]
            if cs.contain([['Tag',s]]):
                j = cs.getItems(['Tag',s])
                if len(j) == 1:
                    #*** write in place ***
                    i[cr.MAJOR] = j[0][cs.MAJOR]
                    i[cr.MINOR] = j[0][cs.MINOR]
                    cnt += 1
                else:
                    multiCnt += 1
                    key = i[cr.DATE] + ':' + i[cr.STORE] + ':' + i[cr.AMOUNT]
                    multi[key] = [j,i[cr.CLEANNAME]]
            else:
                if not s in unknown:
                    unknown.append(s)           
    print '{0} of {1} Records Updated from CreditMap'.format(cnt,cr.getLength())
    print '{0} Records with Multiple CreditMap Entry discovered'.format(multiCnt)
    if len(unknown) > 0:
        unknown.sort()
        for i in unknown:
            print i
            print 'FATAL ERROR --> There are {0} unknown stores'.format(len(unknown))
        return
    #2)    
    mlNames = None
    mlTable = []
    mslDATE = 1
    mslAMOUNT = 3
    mslMAJOR = 5
    mslMINOR = 6
    mslCLEANNAME = 7
    with open('MasterLogINI.csv', 'rb') as csvfile:
        reader = csv.reader(csvfile, delimiter=',')
        for row in reader:
            if mlNames == None:
                mlNames = row
            else:
                # Convert Amount to float format and update cleanName
                row[mslAMOUNT] =str(float(row[mslAMOUNT]))
                row[mslCLEANNAME] = st.creditReplace(row[mslCLEANNAME])
                mlTable.append(row)
    csvfile.close() 
    extras = []
    cnt = 0
    cntSingle = 0
    for idx, i  in enumerate(mlTable):
        j = cr.fetch([['Date',i[mslDATE]],['Amount',i[mslAMOUNT]],\
        ['CleanName',i[mslCLEANNAME]]])
        if not j == None:
            if (j[cr.MAJOR] != '') or (j[cr.MINOR] != ''):
                if (j[cr.MAJOR] != i[mslMAJOR]) or (j[cr.MINOR] != i[mslMINOR]):
                    print j[cr.DATE],j[cr.STORE],j[cr.MAJOR],j[cr.MINOR],'->',\
                    i[mslMAJOR],i[mslMINOR]
                    cntSingle += 1
            cr.replace([['Date',i[mslDATE]],['Amount',i[mslAMOUNT]],\
            ['CleanName',i[mslCLEANNAME]]],[['Major',i[mslMAJOR]],['Minor',i[mslMINOR]]])
            cnt += 1
            # Note that even though we use CleanName in key to fetch from Credit
            # we use Store as part of Multi key which is unique in Credit
            key = j[cr.DATE] + ':' + j[cr.STORE] + ':' + j[cr.AMOUNT]
            if key in multi:
                del multi[key]
        else:
            extras.append(i)
    print '{0} Records with multiple choices updated from MasterLogINI'.format(cnt - cntSingle)
    print '{0} Records with single choices updated from MasterLogINI'.format(cntSingle)
    print '{0} Records with multiple choice remain'.format(len(multi))
    if len(extras) > 0:
        print 'FATAL ERROR --> There was {0} MasterLogINI records not used!'.\
        format(len(extras))
        return

    #3)
    cnt = 1
    ms = CsvTable('MasterINI')
    msMAJOR = 4
    msMINOR = 5
    updated = []
    for i in multi:
        s =str(i).split(':')
        #*** Bug here in that if more than 1 item has the same date,amount
        # this will use the first in all records of that date,amount. This is
        # required since old Store values are deprecated
        stripDotZero = re.compile(r'.0$', re.DOTALL)
        s2 = stripDotZero.sub('', s[2])
        j = ms.fetch([['Date',s[0]],['Amount',s2]])
        if j != None:
            cr.replace([['Date',s[0]],['Amount',s[2]],\
            ['Store',s[1]]],[['Major',j[msMAJOR]],['Minor',j[msMINOR]]])
            cnt += 1
            updated.append(i)
    for i in updated:
        del multi[i]
    print '{0} Records with multiple choices updated from MasterINI'.format(cnt)
    print '{0} Records with multiple choice remain'.format(len(multi))
            
    '''
    The following code was used to assist in reconstructing the origonal database
    since 440 records that should have been in masterlog were missing
    '''   
    '''     
        with open('CreditLog.csv','rb') as csvfile:
            reader = csv.reader(csvfile, delimiter=',')
            for i in reader:
                clNames = i
                break
            csvfile.close()  
        cnt = 1
        with open('TempCreditLogINI.csv', 'wb') as csvfile:
                writer = csv.writer(csvfile, delimiter=',')
                writer.writerow(clNames)
                for i in multi:
                    s =str(i).split(':')
                    writer.writerow([str(cnt),'DELETE',s[0],s[1],s[2]])
                    cnt += 1
                for i in multi:
                    s =str(i).split(':')
                    #*** Bug here in that if more than 1 item has the same date,amount
                    # this will use the first in all records of that date,amount
                    stripDotZero = re.compile(r'.0$', re.DOTALL)
                    s2 = stripDotZero.sub('', s[2])
                    j = ms.fetch([['Date',s[0]],['Amount',s2]])
                    if j == None:
                        writer.writerow([str(cnt),'APPEND',s[0],s[1],s[2],\
                        'Ignore','','',multi[i][1]])
                    else:
                        writer.writerow([str(cnt),'APPEND',s[0],s[1],s[2],\
                        'Ignore',j[4],j[5],multi[i][1]])
                    cnt += 1
                csvfile.close()
    '''
    #4)
    crlini = CsvTable('CreditLogINI')
    ExpApplyCreditLog(cr,crlini)
    #5)
    # Check Occures in Credit.commit
            
    if normal:    
        crl = CsvTable('CreditLog')
        crl.clear()
        crl.close()
    else:
        crl = CsvTable('CreditLog')
        ExpApplyCreditLog(cr,crl)
    cr.commit()
    print cr
    return
		 */
	}
	public static void backup(){
		// TODO Validate all of the databases and if no errors copy them as a new subdirectory(dated) to backup
	}

	public static StoreIdTable getStoreId() {
		return mStoreId;
	}

	public static void setStoreId(StoreIdTable mStores) {
		Finance.mStoreId = mStores;
	}

	public static StoreIdLogTable getStoreIdLog() {
		return mStoreIdLog;
	}

	public static void setStoreIdLog(StoreIdLogTable mStoresLog) {
		Finance.mStoreIdLog = mStoresLog;
	}

	public static CategoryTable getMajor() {
		return mMajor;
	}

	public static void setMajor(CategoryTable mMajor) {
		Finance.mMajor = mMajor;
	}

	public static CategoryLogTable getMajorLog() {
		return mMajorLog;
	}

	public static void setMajorLog(CategoryLogTable mMajorLog) {
		Finance.mMajorLog = mMajorLog;
	}

	public static CategoryTable getMinor() {
		return mMinor;
	}

	public static void setMinor(CategoryTable mMinor) {
		Finance.mMinor = mMinor;
	}

	public static CategoryLogTable getMinorLog() {
		return mMinorLog;
	}

	public static void setMinorLog(CategoryLogTable mMinorLog) {
		Finance.mMinorLog = mMinorLog;
	}

	public static CreditMapTable getCreditMap() {
		return mCreditMap;
	}

	public static void setCreditMap(CreditMapTable mCreditMap) {
		Finance.mCreditMap = mCreditMap;
	}

	public static CreditMapLogTable getCreditMapLog() {
		return mCreditMapLog;
	}

	public static void setCreditMapLog(CreditMapLogTable mCreditMapLog) {
		Finance.mCreditMapLog = mCreditMapLog;
	}

	public static CreditTable getCredit() {
		return mCredit;
	}

	public static void setCredit(CreditTable mCredit) {
		Finance.mCredit = mCredit;
	}

	public static CreditLogTable getCreditINILog() {
		return mCreditINILog;
	}

	public static void setCreditINILog(CreditLogTable mCreditINILog) {
		Finance.mCreditINILog = mCreditINILog;
	}

	public static CreditLogTable getCreditLog() {
		return mCreditLog;
	}

	public static void setCreditLog(CreditLogTable mCreditLog) {
		Finance.mCreditLog = mCreditLog;
	}

	public static CategoryTable getStores() {
		return mStores;
	}

	public static void setStores(CategoryTable mStores) {
		Finance.mStores = mStores;
	}

}
