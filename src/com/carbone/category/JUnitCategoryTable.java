package com.carbone.category;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.carbone.main.Global;
import com.carbone.utils.CustomException;

public class JUnitCategoryTable {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
//		System.out.println("BeforeClass called");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
//		System.out.println("AfterClass called");
	}

	@Before
	public void setUp() throws Exception {
//		System.out.println("Before called");
	}

	@After
	public void tearDown() throws Exception {
//		System.out.println("After called");
	}

	@Test
	public void testToString() {
		CategoryTable t = new CategoryTable("Major");
		System.out.println("testToString called");
		t.add(new Category("one"));
		t.add(new Category("two"));
		System.out.println(t.toString());
	}

	@Test
	public void testCategoryTable() {
		CategoryTable t = new CategoryTable("Minor");
		System.out.println("testCategoryTable called");
		t.add(new Category("one"));
		t.add(new Category("two"));
		assertTrue (t.getSize()==2);
		assertTrue (t.getItem(0).getName().equals("one"));
		assertTrue (t.getItem(1).getName().equals("two"));
		String[] s = t.getNames();
		StringBuilder sb = new StringBuilder("Names:");
		for (String ss : s){
			sb.append(ss + ",");
		}
		System.out.println(sb.substring(0,sb.length()-1).toString());
		t.create(Global.TEST_PATH + "temp.csv");
		System.out.println(t.toString());
		t.openReadOnly(Global.TEST_PATH + "temp.csv");
		System.out.println(t.toString());
		t.dumpTable();

	}

	@Test
	public void testDataSet() {
		CategoryTable t = new CategoryTable("Minor");
		System.out.println("Sort Test");
		t.add(new Category("one"));
		t.add(new Category("twoABC"));
		t.add(new Category("abc"));
		t.add(new Category("AB"));
		t.add(new Category("123"));
		t.add(new Category("AAB"));
		t.add(new Category("two"));
		try {
			t.sort(t.getComparator("Category"));
		} catch (CustomException e) {
			e.printStackTrace();
		}
		t.dumpTable();

	}
}
