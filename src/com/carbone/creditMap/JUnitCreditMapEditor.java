package com.carbone.creditMap;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.carbone.dataset.LogAction;
import com.carbone.main.Global;
import com.carbone.utils.CustomException;

public class JUnitCreditMapEditor {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	    FileUtils.cleanDirectory(new File(Global.TEST_PATH));
	    FileUtils.copyFile(new File(Global.INIT_PATH + "CreditMapTST.csv"), new File(Global.TEST_PATH + "CreditMap.csv")); // Rename
	    FileUtils.copyFile(new File(Global.INIT_PATH + "CreditMapLogINI.csv"), new File(Global.TEST_PATH + "CreditMapLogINI.csv"));

		CreditMapTable st = new CreditMapTable("ST");
		st.add(new CreditMap("OldName10","NewName10","Minor"));
		st.add(new CreditMap("OldName20","NewName20","Minor"));
		st.add(new CreditMap("OldName30","NewName30","Minor"));
		st.create(Global.TEST_PATH + "st.csv");
		CreditMapLogTable stl = new CreditMapLogTable("STL");
		long time = System.currentTimeMillis();
		stl.add(new CreditMapLog(time,LogAction.APPEND,"OldName25","NewName1","Minor"));
		stl.add(new CreditMapLog(time,LogAction.APPEND,"OldName35","NewName1","Minor"));
		stl.add(new CreditMapLog(time,LogAction.APPEND,"OldName45","NewName1","Minor"));
		stl.add(new CreditMapLog(time,LogAction.APPEND,"OldName55","NewName1","Minor"));
		stl.add(new CreditMapLog(time,LogAction.DELETE,"OldName45","NewName1","Minor"));
		stl.create(Global.TEST_PATH + "stl.csv");
		CreditMapLogTable stl1 = new CreditMapLogTable("STL1");
		stl1.create(Global.TEST_PATH + "stl1.csv");
	    FileUtils.copyFile(new File(Global.TEST_PATH + "st.csv"), new File(Global.TEST_PATH + "stc.csv")); // Rename

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test() {
		CreditMapTable st = new CreditMapTable("ST");
		st.open(Global.TEST_PATH + "st.csv");
		CreditMapLogTable stl = new CreditMapLogTable("STL");
		stl.open(Global.TEST_PATH + "stl.csv");
		st.dumpTable();
		stl.dumpTable();
		CreditMapEditor agent = new CreditMapEditor();
		try {
			agent.applyLog(st, stl);
		} catch (CustomException e) {
//			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		st.dumpTable();
		
		CreditMapTable stc = new CreditMapTable("ST Change");
		stc.open(Global.TEST_PATH + "stc.csv");
		CreditMapLogTable stlc = new CreditMapLogTable("STL Change");
		stlc.create(Global.TEST_PATH + "stlc.csv");
		stlc.open(Global.TEST_PATH + "stlc.csv");
		stc.dumpTable();
		stlc.dumpTable();
		ArrayList<CreditMapLog> changes = new ArrayList<CreditMapLog> ();
		long time = System.currentTimeMillis();
		changes.add(new CreditMapLog(time,LogAction.APPEND,"OldName25","NewName1","Minor"));
		changes.add(new CreditMapLog(time,LogAction.APPEND,"OldName35","NewName1","Minor"));
		changes.add(new CreditMapLog(time,LogAction.APPEND,"OldName45","NewName1","Minor"));
		changes.add(new CreditMapLog(time,LogAction.APPEND,"OldName55","NewName1","Minor"));
		changes.add(new CreditMapLog(time,LogAction.DELETE,"OldName45","NewName1","Minor"));
		try {
			agent.applyChanges(stc, stlc,changes);
		} catch (CustomException e) {
//			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		stc.dumpTable();
		stlc.dumpTable();
		ArrayList<CreditMapLog> changeErrors = new ArrayList<CreditMapLog> ();
		changeErrors.add(new CreditMapLog(time,LogAction.APPEND,"OldName55","NewName1","Minor"));
		try {
			agent.applyChanges(stc, stlc,changeErrors);
		} catch (CustomException e) {
//			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		changeErrors.clear();
		changeErrors.add(new CreditMapLog(time,LogAction.DELETE,"OldName46","NewName1","Minor"));
		try {
			agent.applyChanges(stc, stlc,changeErrors);
		} catch (CustomException e) {
//			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void testBig() {
		CreditMapTable st = new CreditMapTable("ST");
		st.open(Global.TEST_PATH + "CreditMap.csv");
		CreditMapLogTable stl = new CreditMapLogTable("STL");
		stl.open(Global.TEST_PATH + "CreditMapLogINI.csv");
//		st.dumpTable();
//		stl.dumpTable();
		CreditMapEditor agent = new CreditMapEditor();
		try {
			agent.applyLog(st, stl);
		} catch (CustomException e) {
//			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		st.dumpLines(20);
	}
}
