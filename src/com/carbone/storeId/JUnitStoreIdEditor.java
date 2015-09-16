package com.carbone.storeId;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.carbone.dataset.LogAction;
import com.carbone.main.Global;
import com.carbone.utils.CustomException;

public class JUnitStoreIdEditor {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	    FileUtils.cleanDirectory(new File(Global.TEST_PATH));
	    FileUtils.copyFile(new File(Global.INIT_PATH + "StoreIdINI.csv"), new File(Global.TEST_PATH + "StoreId.csv")); // Rename
	    FileUtils.copyFile(new File(Global.INIT_PATH + "StoreIdLogINI.csv"), new File(Global.TEST_PATH + "StoreIdLogINI.csv"));
		StoreIdTable st = new StoreIdTable("ST");
		st.add(new StoreId("OldName10","NewName10"));
		st.add(new StoreId("OldName20","NewName20"));
		st.add(new StoreId("OldName30","NewName30"));
		st.create(Global.TEST_PATH + "st.csv");
		StoreIdLogTable stl = new StoreIdLogTable("STL");
		long time = System.currentTimeMillis();
		stl.add(new StoreIdLog(time,LogAction.APPEND,"OldName25","NewName1"));
		stl.add(new StoreIdLog(time,LogAction.APPEND,"OldName35","NewName1"));
		stl.add(new StoreIdLog(time,LogAction.APPEND,"OldName45","NewName1"));
		stl.add(new StoreIdLog(time,LogAction.APPEND,"OldName55","NewName1"));
		stl.add(new StoreIdLog(time,LogAction.DELETE,"OldName45","NewName1"));
		stl.create(Global.TEST_PATH + "stl.csv");
		StoreIdLogTable stl1 = new StoreIdLogTable("STL1");
		stl1.create(Global.TEST_PATH + "stl1.csv");
	    FileUtils.copyFile(new File(Global.TEST_PATH + "st.csv"), new File(Global.TEST_PATH + "stc.csv")); // Rename

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test() {
		StoreIdTable st = new StoreIdTable("ST");
		st.open(Global.TEST_PATH + "st.csv");
		StoreIdLogTable stl = new StoreIdLogTable("STL");
		stl.open(Global.TEST_PATH + "stl.csv");
		st.dumpTable();
		stl.dumpTable();
		StoreIdEditor agent = new StoreIdEditor();
		try {
			agent.applyLog(st, stl);
		} catch (CustomException e) {
//			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		st.dumpTable();
		
		StoreIdTable stc = new StoreIdTable("ST Change");
		stc.open(Global.TEST_PATH + "stc.csv");
		StoreIdLogTable stlc = new StoreIdLogTable("STL Change");
		stlc.create(Global.TEST_PATH + "stlc.csv");
		stlc.open(Global.TEST_PATH + "stlc.csv");
		stc.dumpTable();
		stlc.dumpTable();
		ArrayList<StoreIdLog> changes = new ArrayList<StoreIdLog> ();
		long time = System.currentTimeMillis();
		changes.add(new StoreIdLog(time,LogAction.APPEND,"OldName25","NewName1"));
		changes.add(new StoreIdLog(time,LogAction.APPEND,"OldName35","NewName1"));
		changes.add(new StoreIdLog(time,LogAction.APPEND,"OldName45","NewName1"));
		changes.add(new StoreIdLog(time,LogAction.APPEND,"OldName55","NewName1"));
		changes.add(new StoreIdLog(time,LogAction.DELETE,"OldName45","NewName1"));
		try {
			agent.applyChanges(stc, stlc,changes);
		} catch (CustomException e) {
//			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		stc.dumpTable();
		stlc.dumpTable();
		ArrayList<StoreIdLog> changeErrors = new ArrayList<StoreIdLog> ();
		changeErrors.add(new StoreIdLog(time,LogAction.APPEND,"OldName55","NewName1"));
		try {
			agent.applyChanges(stc, stlc,changeErrors);
		} catch (CustomException e) {
//			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		changeErrors.clear();
		changeErrors.add(new StoreIdLog(time,LogAction.DELETE,"OldName46","NewName1"));
		try {
			agent.applyChanges(stc, stlc,changeErrors);
		} catch (CustomException e) {
//			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void testBig() {
		StoreIdTable st = new StoreIdTable("ST");
		st.open(Global.TEST_PATH + "StoreId.csv");
		StoreIdLogTable stl = new StoreIdLogTable("STL");
		stl.open(Global.TEST_PATH + "StoreIdLogINI.csv");
//		st.dumpTable();
//		stl.dumpTable();
		StoreIdEditor agent = new StoreIdEditor();
		try {
			agent.applyLog(st, stl);
		} catch (CustomException e) {
			e.printStackTrace();
		}
		st.dumpLines(20);
	}

}
