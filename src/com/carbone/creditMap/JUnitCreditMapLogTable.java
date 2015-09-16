package com.carbone.creditMap;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.carbone.dataset.LogAction;
import com.carbone.main.Global;
import com.carbone.utils.CustomException;

public class JUnitCreditMapLogTable {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test() {
		CreditMapLogTable t = new CreditMapLogTable("First");
		System.out.println("testCreditMapLogTable called");
		long time = System.currentTimeMillis();
		t.add(new CreditMapLog(time,LogAction.APPEND,"Tag1","Major1","Major1"));
		t.add(new CreditMapLog(time,LogAction.DELETE,"Tag2","Major2","Major2"));
		assertTrue (t.getSize()==2);
		assertTrue (t.getItem(0).getTag().equals("Tag1"));
		assertTrue (t.getItem(1).getTag().equals("Tag2"));
		assertTrue (t.getItem(0).getAction() == LogAction.APPEND);
		assertTrue (t.getItem(1).getAction() == LogAction.DELETE);
		assertTrue (t.getItem(0).getTimeStamp() == time);
		String[] s = t.getNames();
		StringBuilder sb = new StringBuilder("Names:");
		for (String ss : s){
			sb.append(ss + ",");
		}
		System.out.println(sb.substring(0,sb.length()-1).toString());
	}
	
	@Test
	public void test1() {
		System.out.println("Test1");
		CreditMapLogTable t = new CreditMapLogTable("Second");
		long time = System.currentTimeMillis();
		t.add(new CreditMapLog(time,LogAction.APPEND,"Tag1","Major1","Major1"));
		t.add(new CreditMapLog(time,LogAction.DELETE,"Tag2","Major2","Major2"));
		t.create(Global.TEST_PATH + "temp.csv");
		System.out.println(t.toString());
		t.dumpTable();
		t.open(Global.TEST_PATH + "temp.csv");
		System.out.println(t.toString());
		t.dumpTable();
	}
	
	@Test
	public void testSort() {
		CreditMapLogTable t = new CreditMapLogTable("Third");
		System.out.println("Sort Test");
		long time = System.currentTimeMillis();
		t.add(new CreditMapLog(time+1000,LogAction.APPEND,"Tag1","Major1","Major1"));
		t.add(new CreditMapLog(time+2000,LogAction.APPEND,"Tag2","Major1","Major1"));
		t.add(new CreditMapLog(time,LogAction.APPEND,"Tag3","Major1","Major1"));
		t.add(new CreditMapLog(time-1000,LogAction.APPEND,"Tag4","Major1","Major1"));
		t.add(new CreditMapLog(time+3000,LogAction.APPEND,"Tag5","Major1","Major1"));
		try {
			t.sort(t.getComparator("TimeStamp"));
		} catch (CustomException e) {
			e.printStackTrace();
		}
		t.dumpTable();
	}
	
	@Test
	public void testContains(){
		CreditMapLogTable t = new CreditMapLogTable("Contains");
		System.out.println("Contains Test");
		long time = System.currentTimeMillis();
		t.add(new CreditMapLog(time,LogAction.APPEND,"Tag1","Major1","Major1"));
		t.add(new CreditMapLog(time,LogAction.APPEND,"Tag2","Major1","Major1"));
		t.add(new CreditMapLog(time,LogAction.APPEND,"Tag3","Major1","Major1"));
		t.add(new CreditMapLog(time+3000,LogAction.APPEND,"Tag4","Major1","Major1"));
		t.add(new CreditMapLog(time+3000,LogAction.DELETE,"Tag5","Major1","Major1"));
		CreditMapLog m1 = new CreditMapLog();
		CreditMapLog m2 = new CreditMapLog();
		CreditMapLog m3 = new CreditMapLog();
		CreditMapLog m4 = new CreditMapLog();
		CreditMapLog m5 = new CreditMapLog();
		CreditMapLog m6 = new CreditMapLog();
		m1.setTimeStamp(0L);
		m2.setTimeStamp(time);
		m3.setAction(LogAction.APPEND);
		m4.setTag("Tag5");
		m5.setTimeStamp(time+3000);
		m5.setAction(LogAction.DELETE);
		ArrayList<Integer> result;
		result = t.contains(m1);
		dmp_arraylist(result,"m1");
		result = t.contains(m2);
		dmp_arraylist(result,"m2");
		result = t.contains(m3);
		dmp_arraylist(result,"m3");
		result = t.contains(m4);
		dmp_arraylist(result,"m4");
		result = t.contains(m5);
		dmp_arraylist(result,"m5");
		result = t.contains(m6);
		dmp_arraylist(result,"m6");
		
		System.out.println("\nTest Fetch");
		CreditMapLog value = new CreditMapLog();
		value.setTimeStamp(time + 4000);
		ArrayList<CreditMapLog> first = t.fetchFirst(m3);
		dmp_arraylistCat(first,"First");
		ArrayList<CreditMapLog> all = t.fetchAll(m3);
		dmp_arraylistCat(all,"All");
		
		System.out.println("\nTest Replace");
		int cnt = t.replaceFirst(m3, value);
		System.out.println("Count:" + cnt);
		t.dumpTable();
		dmp_arraylistCat(first,"First");
		value.setTimeStamp(time + 5000);
		cnt = t.replaceAll(m3, value);
		System.out.println("Count:" + cnt);
		t.dumpTable();
		dmp_arraylistCat(all,"All");
		
		System.out.println("\nTest Iterator");
		Iterator<CreditMapLog> z;
		z = t.getIterator();
		while(z.hasNext()) {
			CreditMapLog element = z.next();
			System.out.println(element.getTag());
		}
		for (CreditMap zz : t){
			System.out.println(zz.getTag());
		}
		
		System.out.println("\nTest Delete");
		cnt = t.deleteFirst(m3);
		System.out.println("Count:" + cnt);
		t.dumpTable();
		cnt = t.deleteAll(m3);	
		System.out.println("Count:" + cnt);
		t.dumpTable();
	}
	
	private void dmp_arraylist(ArrayList<Integer> r,String name){
		System.out.println(name + ":" + r.toString());		
	}
	private void dmp_arraylistCat(ArrayList<CreditMapLog> r,String name){
		System.out.println("<<" + name + ">>");	
		for (CreditMapLog c : r){
			System.out.println(c.toString());	
		}	
	}
}
