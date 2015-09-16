package com.carbone.category;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.carbone.dataset.LogAction;
import com.carbone.main.Global;
import com.carbone.utils.CustomException;

public class JUnitCategoryEditor {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	    FileUtils.cleanDirectory(new File(Global.TEST_PATH));
		CategoryTable st = new CategoryTable("ST");
		st.add(new Category("OldName10"));
		st.add(new Category("OldName20"));
		st.add(new Category("OldName30"));
		st.create(Global.TEST_PATH + "st.csv");
		CategoryLogTable stl = new CategoryLogTable("STL");
		long time = System.currentTimeMillis();
		stl.add(new CategoryLog(time,LogAction.APPEND,"OldName25"));
		stl.add(new CategoryLog(time,LogAction.APPEND,"OldName35"));
		stl.add(new CategoryLog(time,LogAction.APPEND,"OldName45"));
		stl.add(new CategoryLog(time,LogAction.APPEND,"OldName55"));
		stl.add(new CategoryLog(time,LogAction.DELETE,"OldName45"));
		stl.create(Global.TEST_PATH + "stl.csv");
		CategoryLogTable stl1 = new CategoryLogTable("STL1");
		stl1.create(Global.TEST_PATH + "stl1.csv");
	    FileUtils.copyFile(new File(Global.TEST_PATH + "st.csv"), new File(Global.TEST_PATH + "stc.csv")); // Rename

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test() {
		CategoryTable st = new CategoryTable("ST");
		st.open(Global.TEST_PATH + "st.csv");
		CategoryLogTable stl = new CategoryLogTable("STL");
		stl.open(Global.TEST_PATH + "stl.csv");
		st.dumpTable();
		stl.dumpTable();
		CategoryEditor agent = new CategoryEditor();
		try {
			agent.applyLog(st, stl);
		} catch (CustomException e) {
//			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		st.dumpTable();
		
		CategoryTable stc = new CategoryTable("ST Change");
		stc.open(Global.TEST_PATH + "stc.csv");
		CategoryLogTable stlc = new CategoryLogTable("STL Change");
		stlc.create(Global.TEST_PATH + "stlc.csv");
		stlc.open(Global.TEST_PATH + "stlc.csv");
		stc.dumpTable();
		stlc.dumpTable();
		ArrayList<CategoryLog> changes = new ArrayList<CategoryLog> ();
		long time = System.currentTimeMillis();
		changes.add(new CategoryLog(time,LogAction.APPEND,"OldName25"));
		changes.add(new CategoryLog(time,LogAction.APPEND,"OldName35"));
		changes.add(new CategoryLog(time,LogAction.APPEND,"OldName45"));
		changes.add(new CategoryLog(time,LogAction.APPEND,"OldName55"));
		changes.add(new CategoryLog(time,LogAction.DELETE,"OldName45"));
		try {
			agent.applyChanges(stc, stlc,changes);
		} catch (CustomException e) {
//			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		stc.dumpTable();
		stlc.dumpTable();
		ArrayList<CategoryLog> changeErrors = new ArrayList<CategoryLog> ();
		changeErrors.add(new CategoryLog(time,LogAction.APPEND,"OldName55"));
		try {
			agent.applyChanges(stc, stlc,changeErrors);
		} catch (CustomException e) {
//			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		changeErrors.clear();
		changeErrors.add(new CategoryLog(time,LogAction.DELETE,"OldName46"));
		try {
			agent.applyChanges(stc, stlc,changeErrors);
		} catch (CustomException e) {
//			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
}
