package com.carbone.storeId;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.carbone.dataset.LogAction;
import com.carbone.main.Global;
import com.carbone.utils.CustomException;

public class JUnitStoreIdLogTable {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test() {
		StoreIdLogTable t = new StoreIdLogTable("First");
		System.out.println("testCreditMapLogTable called");
		long time = System.currentTimeMillis();
		t.add(new StoreIdLog(time,LogAction.APPEND,"OldName1","NewName1"));
		t.add(new StoreIdLog(time,LogAction.DELETE,"OldName2","NewName2"));
		assertTrue (t.getSize()==2);
		assertTrue (t.getItem(0).getOldName().equals("OldName1"));
		assertTrue (t.getItem(1).getOldName().equals("OldName2"));
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
		StoreIdLogTable t = new StoreIdLogTable("Second");
		long time = System.currentTimeMillis();
		t.add(new StoreIdLog(time,LogAction.APPEND,"OldName1","NewName1"));
		t.add(new StoreIdLog(time,LogAction.DELETE,"OldName2","NewName2"));
		t.create(Global.TEST_PATH + "temp.csv");
		System.out.println(t.toString());
		t.dumpTable();
		t.open(Global.TEST_PATH + "temp.csv");
		System.out.println(t.toString());
		t.dumpTable();
	}
	
	@Test
	public void testSort() {
		StoreIdLogTable t = new StoreIdLogTable("Third");
		System.out.println("Sort Test");
		long time = System.currentTimeMillis();
		t.add(new StoreIdLog(time+1000,LogAction.APPEND,"OldName1","NewName1"));
		t.add(new StoreIdLog(time+2000,LogAction.APPEND,"OldName2","NewName1"));
		t.add(new StoreIdLog(time,LogAction.APPEND,"OldName3","NewName1"));
		t.add(new StoreIdLog(time-1000,LogAction.APPEND,"OldName4","NewName1"));
		t.add(new StoreIdLog(time+3000,LogAction.APPEND,"OldName5","NewName1"));
		try {
			t.sort(t.getComparator("TimeStamp"));
		} catch (CustomException e) {
			e.printStackTrace();
		}
		t.dumpTable();
	}
	
	@Test
	public void testContains(){
		StoreIdLogTable t = new StoreIdLogTable("Contains");
		System.out.println("Contains Test");
		long time = System.currentTimeMillis();
		t.add(new StoreIdLog(time,LogAction.APPEND,"OldName1","NewName1"));
		t.add(new StoreIdLog(time,LogAction.APPEND,"OldName2","NewName1"));
		t.add(new StoreIdLog(time,LogAction.APPEND,"OldName3","NewName1"));
		t.add(new StoreIdLog(time+3000,LogAction.APPEND,"OldName4","NewName1"));
		t.add(new StoreIdLog(time+3000,LogAction.DELETE,"OldName5","NewName1"));
		StoreIdLog m1 = new StoreIdLog();
		StoreIdLog m2 = new StoreIdLog();
		StoreIdLog m3 = new StoreIdLog();
		StoreIdLog m4 = new StoreIdLog();
		StoreIdLog m5 = new StoreIdLog();
		StoreIdLog m6 = new StoreIdLog();
		m1.setTimeStamp(0L);
		m2.setTimeStamp(time);
		m3.setAction(LogAction.APPEND);
		m4.setOldName("OldName5");
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
		StoreIdLog value = new StoreIdLog();
		value.setTimeStamp(time + 4000);
		ArrayList<StoreIdLog> first = t.fetchFirst(m3);
		dmp_arraylistCat(first,"First");
		ArrayList<StoreIdLog> all = t.fetchAll(m3);
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
		Iterator<StoreIdLog> z;
		z = t.getIterator();
		while(z.hasNext()) {
			StoreIdLog element = z.next();
			System.out.println(element.getOldName());
		}
		for (StoreId zz : t){
			System.out.println(zz.getOldName());
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
	private void dmp_arraylistCat(ArrayList<StoreIdLog> r,String name){
		System.out.println("<<" + name + ">>");	
		for (StoreIdLog c : r){
			System.out.println(c.toString());	
		}	
	}
}
