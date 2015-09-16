package com.carbone.credit;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.carbone.creditMap.CreditMapLog;
import com.carbone.dataset.LogAction;
import com.carbone.main.Global;
import com.carbone.utils.Currency;
import com.carbone.utils.CustomException;

public class JUnitCreditEditor {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	    FileUtils.cleanDirectory(new File(Global.TEST_PATH));
//	    FileUtils.copyFile(new File(Global.INIT_PATH + "CreditINI.csv"), new File(Global.TEST_PATH + "Credit.csv")); // Rename
//	    FileUtils.copyFile(new File(Global.INIT_PATH + "CreditLogINI.csv"), new File(Global.TEST_PATH + "CreditLogINI.csv"));

		CreditTable st = new CreditTable("ST");
		st.add(Credit.createCredit("OldName10", 1000L,Currency.create("23.45"),"Loc1", 
				"NewName10","Minor", true));
		st.add(Credit.createCredit("OldName20", 1000L,Currency.create("23.45"),"Loc1", 
				"NewName10","Minor", true));
		st.add(Credit.createCredit("OldName30", 1000L,Currency.create("23.45"),"Loc1", 
				"NewName10","Minor", true));
		st.create(Global.TEST_PATH + "st.csv");
		CreditLogTable stl = new CreditLogTable("STL");
		long time = System.currentTimeMillis();
		stl.add(new CreditLog(time,LogAction.APPEND,
				"OldName25", 1000L,Currency.create("23.45"),"Loc1", 
				"NewName10","Minor", true));
		stl.add(new CreditLog(time,LogAction.APPEND,
				"OldName35", 1000L,Currency.create("23.45"),"Loc1", 
				"NewName10","Minor", true));
		stl.add(new CreditLog(time,LogAction.APPEND,
				"OldName45", 1000L,Currency.create("23.45"),"Loc1", 
				"NewName10","Minor", true));
		stl.add(new CreditLog(time,LogAction.APPEND,
				"OldName55", 1000L,Currency.create("23.45"),"Loc1", 
				"NewName10","Minor", true));
		stl.add(new CreditLog(time,LogAction.DELETE,
				"OldName45", 1000L,Currency.create("23.45"),"Loc1", 
				"NewName10","Minor", true));
		stl.create(Global.TEST_PATH + "stl.csv");
		CreditLogTable stl1 = new CreditLogTable("STL1");
		stl1.create(Global.TEST_PATH + "stl1.csv");
	    FileUtils.copyFile(new File(Global.TEST_PATH + "st.csv"), new File(Global.TEST_PATH + "stc.csv")); // Rename

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test() {
		CreditTable st = new CreditTable("ST");
		st.open(Global.TEST_PATH + "st.csv");
		CreditLogTable stl = new CreditLogTable("STL");
		stl.open(Global.TEST_PATH + "stl.csv");
		st.dumpTable();
		stl.dumpTable();
		CreditEditor agent = new CreditEditor();
		try {
			agent.applyLog(st, stl);
		} catch (CustomException e) {
//			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		st.dumpTable();
		
		CreditTable stc = new CreditTable("ST Change");
		stc.open(Global.TEST_PATH + "stc.csv");
		CreditLogTable stlc = new CreditLogTable("STL Change");
		stlc.create(Global.TEST_PATH + "stlc.csv");
		stlc.open(Global.TEST_PATH + "stlc.csv");
		stc.dumpTable();
		stlc.dumpTable();
		ArrayList<CreditLog> changes = new ArrayList<CreditLog> ();
		long time = System.currentTimeMillis();
		changes.add(new CreditLog(time,LogAction.APPEND,
				"OldName25", 1000L,Currency.create("23.45"),"Loc1", 
				"NewName10","Minor", true));
		changes.add(new CreditLog(time,LogAction.APPEND,
				"OldName35", 1000L,Currency.create("23.45"),"Loc1", 
				"NewName10","Minor", true));
		changes.add(new CreditLog(time,LogAction.APPEND,
				"OldName45", 1000L,Currency.create("23.45"),"Loc1", 
				"NewName10","Minor", true));
		changes.add(new CreditLog(time,LogAction.APPEND,
				"OldName55", 1000L,Currency.create("23.45"),"Loc1", 
				"NewName10","Minor", true));
		changes.add(new CreditLog(time,LogAction.DELETE,
				"OldName45", 1000L,Currency.create("23.45"),"Loc1", 
				"NewName10","Minor", true));
		try {
			agent.applyChanges(stc, stlc,changes);
		} catch (CustomException e) {
//			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		stc.dumpTable();
		stlc.dumpTable();
		ArrayList<CreditLog> changeErrors = new ArrayList<CreditLog> ();
		changeErrors.add(new CreditLog(time,LogAction.APPEND,
				"OldName55", 1000L,Currency.create("23.45"),"Loc1", 
				"NewName10","Minor", true));
		try {
			agent.applyChanges(stc, stlc,changeErrors);
		} catch (CustomException e) {
//			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		changeErrors.clear();
		changeErrors.add(new CreditLog(time,LogAction.DELETE,
				"OldName46", 1000L,Currency.create("23.45"),"Loc1", 
				"NewName10","Minor", true));
		try {
			agent.applyChanges(stc, stlc,changeErrors);
		} catch (CustomException e) {
//			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
//	@Test
//	public void testBig() {
//		CreditTable st = new CreditTable("ST");
//		st.open(Global.TEST_PATH + "Credit.csv");
//		CreditLogTable stl = new CreditLogTable("STL");
//		stl.open(Global.TEST_PATH + "CreditLogINI.csv");
//		CreditEditor agent = new CreditEditor();
//		try {
//			agent.applyLog(st, stl);
//		} catch (CustomException e) {
//			e.printStackTrace();
//		}
//		st.dumpLines(20);
//	}
}
