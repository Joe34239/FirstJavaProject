package com.carbone.credit;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.carbone.dataset.LogAction;
import com.carbone.main.Global;
import com.carbone.utils.Currency;
import com.carbone.utils.CustomException;

public class JUnitCreditLogTable {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test() {
		CreditLogTable t = new CreditLogTable("First");
		System.out.println("\nBasic Test");
		long time = System.currentTimeMillis();
		t.add(new CreditLog(time,LogAction.APPEND,"Store1",1000L,Currency.create("23.45"),"Loc1","Major1","Minor1",true));
		t.add(new CreditLog(time,LogAction.DELETE,"Store2",500L,Currency.create("-23.45"),"Loc2","Major2","Minor2",false));
		assertTrue (t.getSize()==2);
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
		System.out.println("\nFile Test");
		CreditLogTable t = new CreditLogTable("Second");
		long time = System.currentTimeMillis();
		t.add(new CreditLog(time,LogAction.APPEND,"Store1",1000L,Currency.create("23.45"),"Loc1","Major1","Minor1",true));
		t.add(new CreditLog(time,LogAction.DELETE,"Store2",500L,Currency.create("-23.45"),"Loc2","Major2","Minor2",false));
		t.create(Global.TEST_PATH + "temp.csv");
		System.out.println(t.toString());
		t.dumpTable();
		t.open(Global.TEST_PATH + "temp.csv");
		System.out.println(t.toString());
		t.dumpTable();
	}
	
	@Test
	public void testSort() {
		CreditLogTable t = new CreditLogTable("Third");
		System.out.println("\nSort Test");
		long time = System.currentTimeMillis();
		t.add(new CreditLog(time+1000,LogAction.APPEND,"Store1",1000L,Currency.create("23.45"),"Loc1","Major1","Minor1",true));
		t.add(new CreditLog(time+2000,LogAction.APPEND,"Store2",1000L,Currency.create("23.45"),"Loc1","Major1","Minor1",true));
		t.add(new CreditLog(time,LogAction.APPEND,"Store3",1000L,Currency.create("23.45"),"Loc1","Major1","Minor1",true));
		t.add(new CreditLog(time-1000,LogAction.APPEND,"Store4",1000L,Currency.create("23.45"),"Loc1","Major1","Minor1",true));
		t.add(new CreditLog(time+3000,LogAction.APPEND,"Store5",1000L,Currency.create("23.45"),"Loc1","Major1","Minor1",true));
		try {
			t.sort(t.getComparator("TimeStamp"));
		} catch (CustomException e) {
			e.printStackTrace();
		}
		t.dumpTable();
	}
	
	@Test
	public void testContains(){
		CreditLogTable t = new CreditLogTable("Contains");
		System.out.println("\nContains Test");
		long time = System.currentTimeMillis();
		t.add(new CreditLog(time,LogAction.APPEND,"Store1",1000L,Currency.create("23.45"),"Loc1","Major1","Minor1",true));
		t.add(new CreditLog(time,LogAction.APPEND,"Store2",1000L,Currency.create("23.45"),"Loc1","Major1","Minor1",true));
		t.add(new CreditLog(time,LogAction.APPEND,"Store3",1000L,Currency.create("23.45"),"Loc1","Major1","Minor1",true));
		t.add(new CreditLog(time+3000,LogAction.APPEND,"Store4",1000L,Currency.create("23.45"),"Loc1","Major1","Minor1",true));
		t.add(new CreditLog(time+3000,LogAction.DELETE,"Store5",1000L,Currency.create("23.45"),"Loc1","Major1","Minor1",true));
		CreditLog m1 = new CreditLog();
		CreditLog m2 = new CreditLog();
		CreditLog m3 = new CreditLog();
		CreditLog m4 = new CreditLog();
		CreditLog m5 = new CreditLog();
		CreditLog m6 = new CreditLog();
		m1.setTimeStamp(0L);
		m2.setTimeStamp(time);
		m3.setAction(LogAction.APPEND);
		m4.setStore("Store5");
		m5.setTimeStamp(time+3000);
		m5.setAction(LogAction.DELETE);
		ArrayList<Integer> result;
		result = t.contains(m1);
		dmp_arraylist(result,"m1[]");
		result = t.contains(m2);
		dmp_arraylist(result,"m2[0,1,2]");
		result = t.contains(m3);
		dmp_arraylist(result,"m3[0,1,2,3]");
		result = t.contains(m4);
		dmp_arraylist(result,"m4[4]");
		result = t.contains(m5);
		dmp_arraylist(result,"m5[3,4]");
		result = t.contains(m6);
		dmp_arraylist(result,"m6[0,1,2,3,4]");
		
		System.out.println("\nTest Fetch");
		ArrayList<CreditLog> first = t.fetchFirst(m3);
		dmp_arraylistCat(first,"First");
		ArrayList<CreditLog> all = t.fetchAll(m3);
		dmp_arraylistCat(all,"All");
		
		System.out.println("\nTest Replace");
		CreditLog value = new CreditLog();
		value.setTimeStamp(time + 4000);
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
		Iterator<CreditLog> z;
		z = t.getIterator();
		while(z.hasNext()) {
			CreditLog element = z.next();
			System.out.println(element.getStore());
		}
		for (Credit zz : t){
			System.out.println(zz.getStore());
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
	private void dmp_arraylistCat(ArrayList<CreditLog> r,String name){
		System.out.println("<<" + name + ">>");	
		for (CreditLog c : r){
			System.out.println(c.toString());	
		}	
	}
}
