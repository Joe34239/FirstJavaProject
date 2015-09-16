package com.carbone.category;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.carbone.dataset.LogAction;
import com.carbone.main.Global;
import com.carbone.utils.CustomException;

public class JUnitCategoryLogTable {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test() {
		CategoryLogTable t = new CategoryLogTable("First");
		System.out.println("testCategoryTransactionTable called");
		long time = System.currentTimeMillis();
		t.add(new CategoryLog(time,LogAction.APPEND,"one"));
		t.add(new CategoryLog(time,LogAction.DELETE,"two"));
		assertTrue (t.getSize()==2);
		assertTrue (t.getItem(0).getName().equals("one"));
		assertTrue (t.getItem(1).getName().equals("two"));
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
		CategoryLogTable t = new CategoryLogTable("Second");
		long time = System.currentTimeMillis();
		t.add(new CategoryLog(time,LogAction.APPEND,"one"));
		t.add(new CategoryLog(time,LogAction.DELETE,"two"));
		t.create(Global.TEST_PATH +  "temp.csv");
		System.out.println(t.toString());
		t.dumpTable();
		t.open(Global.TEST_PATH + "temp.csv");
		System.out.println(t.toString());
		t.dumpTable();
	}
	
	@Test
	public void testSort() {
		CategoryLogTable t = new CategoryLogTable("Third");
		System.out.println("Sort Test");
		long time = System.currentTimeMillis();
		t.add(new CategoryLog(time+1000,LogAction.APPEND,"one"));
		t.add(new CategoryLog(time+2000,LogAction.APPEND,"two"));
		t.add(new CategoryLog(time,LogAction.APPEND,"three"));
		t.add(new CategoryLog(time-1000,LogAction.APPEND,"four"));
		t.add(new CategoryLog(time+3000,LogAction.APPEND,"five"));
		try {
			t.sort(t.getComparator("TimeStamp"));
		} catch (CustomException e) {
			e.printStackTrace();
		}
		t.dumpTable();
	}
	
	@Test
	public void testContains(){
		CategoryLogTable t = new CategoryLogTable("Contains");
		System.out.println("Contains Test");
		long time = System.currentTimeMillis();
		t.add(new CategoryLog(time,LogAction.APPEND,"one"));
		t.add(new CategoryLog(time,LogAction.APPEND,"two"));
		t.add(new CategoryLog(time,LogAction.APPEND,"three"));
		t.add(new CategoryLog(time+3000,LogAction.APPEND,"four"));
		t.add(new CategoryLog(time+3000,LogAction.DELETE,"five"));
		CategoryLog m1 = new CategoryLog();
		CategoryLog m2 = new CategoryLog();
		CategoryLog m3 = new CategoryLog();
		CategoryLog m4 = new CategoryLog();
		CategoryLog m5 = new CategoryLog();
		CategoryLog m6 = new CategoryLog();
		m1.setTimeStamp(0L);
		m2.setTimeStamp(time);
		m3.setAction(LogAction.APPEND);
		m4.setName("five");
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
		CategoryLog value = new CategoryLog();
		value.setTimeStamp(time + 4000);
		ArrayList<CategoryLog> first = t.fetchFirst(m3);
		dmp_arraylistCat(first,"First");
		ArrayList<CategoryLog> all = t.fetchAll(m3);
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
		Iterator<CategoryLog> z;
		z = t.getIterator();
		while(z.hasNext()) {
			CategoryLog element = z.next();
			System.out.println(element.getName());
		}
		for (CategoryLog zz : t){
			System.out.println(zz.getName());
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
	private void dmp_arraylistCat(ArrayList<CategoryLog> r,String name){
		System.out.println("<<" + name + ">>");	
		for (CategoryLog c : r){
			System.out.println(c.toString());	
		}	
	}
}
