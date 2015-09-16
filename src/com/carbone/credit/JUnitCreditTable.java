package com.carbone.credit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.carbone.main.Global;
import com.carbone.utils.Currency;
import com.carbone.utils.CustomException;

public class JUnitCreditTable {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test() {
		CreditTable t = new CreditTable("First");
		t.add(Credit.createCredit("Store1",1000L,Currency.create("23.45"),"Loc1","Major1","Minor1",true));
		t.add(Credit.createCredit("Store2",500L,Currency.create("-23.45"),"Loc2","Major2","Minor2",false));
		assertTrue (t.getSize()==2);
		assertTrue (t.getItem(0).getStore().equals("Store1"));
		assertTrue (t.getItem(1).getStore().equals("Store2"));
		assertTrue (t.getItem(0).getDate().equals(1000L));
		assertTrue (t.getItem(1).getDate().equals(500L));
		assertTrue (t.getItem(0).getAmount().getCents().equals(Currency.create("23.45").getCents()));
		assertTrue (t.getItem(1).getAmount().getCents().equals(Currency.create("-23.45").getCents()));
		assertTrue (t.getItem(0).getLocation().equals("Loc1"));
		assertTrue (t.getItem(1).getLocation().equals("Loc2"));
		assertTrue (t.getItem(0).getMajor().equals("Major1"));
		assertTrue (t.getItem(1).getMajor().equals("Major2"));
		assertTrue (t.getItem(0).getMinor().equals("Minor1"));
		assertTrue (t.getItem(1).getMinor().equals("Minor2"));
		assertTrue (t.getItem(0).isIgnore());
		assertTrue (!t.getItem(1).isIgnore());

		String[] s = t.getNames();
		StringBuilder sb = new StringBuilder("Names:");
		for (String ss : s){
			sb.append(ss + ",");
		}
		System.out.println(sb.substring(0,sb.length()-1).toString());
		
		System.out.println("Write & Read");
		CreditTable t1 = new CreditTable("Second");
		t1.add(Credit.createCredit("Store1",100000000L,Currency.create("23.45"),"Loc1","Major1","Minor1",true));
		t1.add(Credit.createCredit("Store2",500000000L,Currency.create("-23.45"),"Loc2","Major2","Minor2",false));
		t1.create(Global.TEST_PATH + "temp.csv");
		System.out.println(t1.toString());
		t1.dumpTable();
		t1.open(Global.TEST_PATH + "temp.csv");
		System.out.println(t1.toString());
		t1.dumpTable();
		
		CreditTable t2 = new CreditTable("Third");
		System.out.println("Sort Test");
		t2.add(Credit.createCredit("Store3",800000000L,Currency.create("23.45"),"Loc1","Major4","Minor1",true));
		t2.add(Credit.createCredit("Store4",500000000L,Currency.create("-23.45"),"Loc2","Major3","Minor2",false));
		t2.add(Credit.createCredit("Store1",100000000L,Currency.create("0"),"Loc3","Major2","Minor1",true));
		t2.add(Credit.createCredit("Store2",900000000L,Currency.create("100.00"),"Loc4","Major1","Minor2",false));
		try {
			t2.sort(t2.getComparator("Store"));
		} catch (CustomException e) {
			e.printStackTrace();
		}
		t2.dumpTable();
		try {
			t2.sort(t2.getComparator("Date"));
		} catch (CustomException e) {
			e.printStackTrace();
		}
		t2.dumpTable();
		try {
			t2.sort(t2.getComparator("Amount"));
		} catch (CustomException e) {
			e.printStackTrace();
		}
		t2.dumpTable();
		try {
			t2.sort(t2.getComparator("Location"));
		} catch (CustomException e) {
			e.printStackTrace();
		}
		t2.dumpTable();
		try {
			t2.sort(t2.getComparator("Major"));
		} catch (CustomException e) {
			e.printStackTrace();
		}
		t2.dumpTable();
		try {
			t2.sort(t2.getComparator("Minor"));
		} catch (CustomException e) {
			e.printStackTrace();
		}
		t2.dumpTable();
		try {
			t2.sort(t2.getComparator("Ignore"));
		} catch (CustomException e) {
			e.printStackTrace();
		}
		t2.dumpTable();
		
		System.out.println("Contains Test");
		Credit m1 = Credit.createCredit();
		Credit m2 = Credit.createCredit();
		Credit m3 = Credit.createCredit();
		Credit m4 = Credit.createCredit();
		Credit m5 = Credit.createCredit();
		Credit m6 = Credit.createCredit();
		Credit m7 = Credit.createCredit();
		Credit m8 = Credit.createCredit();	//None
		Credit m9 = Credit.createCredit("Store2",900000000L,Currency.create("100.00"),"Loc4","Major1","Minor2",false);	//All
		Credit m10 = Credit.createCredit();	//Fail
		m1.setStore("Store1");
		m2.setDate(100000000L);
		m3.setAmount(Currency.create("0"));
		m4.setLocation("Loc1");
		m5.setMajor("Major1");
		m6.setMinor("Minor1");
		m7.setIgnore(false);
		m10.setLocation("Loc99");
		ArrayList<Integer> result;
		result = t2.contains(m1);
		dmp_arraylist(result,"m1[2]");
		result = t2.contains(m2);
		dmp_arraylist(result,"m2[2]");
		result = t2.contains(m3);
		dmp_arraylist(result,"m3[2]");
		result = t2.contains(m4);
		dmp_arraylist(result,"m4[3]");
		result = t2.contains(m5);
		dmp_arraylist(result,"m5[0]");
		result = t2.contains(m6);
		dmp_arraylist(result,"m6[2,3]");
		result = t2.contains(m7);
		dmp_arraylist(result,"m7[0,1]");
		result = t2.contains(m8);
		dmp_arraylist(result,"m8[0,1,2,3]");
		result = t2.contains(m9);
		dmp_arraylist(result,"m9[0]");
		result = t2.contains(m10);
		dmp_arraylist(result,"m10[]");
		
		System.out.println("\nTest Fetch");
		ArrayList<Credit> first = t2.fetchFirst(m7);
		dmp_item(first,"First");
		ArrayList<Credit> all = t2.fetchAll(m7);
		dmp_item(all,"All");
		
		System.out.println("\nTest Replace");
		Credit value = Credit.createCredit();
		value.setMinor("CHANGED");
		int cnt = t2.replaceFirst(m6, value);
		System.out.println("Count:" + cnt);
		t2.dumpTable();

		m6.setMinor("Minor2");
		value.setMinor("CHANGED AGAIN");
		cnt = t2.replaceAll(m6, value);
		System.out.println("Count:" + cnt);
		t2.dumpTable();

		System.out.println("\nTest Iterator");
		Iterator<Credit> z;
		z = t2.getIterator();
		while(z.hasNext()) {
			Credit element = z.next();
			System.out.println(element.getStore());
		}
		for (Credit zz : t){
			System.out.println(zz.getStore());
		}
		
		System.out.println("\nTest Delete");
		t2.clear();
		t2.add(Credit.createCredit("Store3",800000000L,Currency.create("23.45"),"Loc1","Major4","Minor1",true));
		t2.add(Credit.createCredit("Store4",500000000L,Currency.create("-23.45"),"Loc2","Major3","Minor2",false));
		t2.add(Credit.createCredit("Store1",100000000L,Currency.create("0"),"Loc3","Major2","Minor1",true));
		t2.add(Credit.createCredit("Store2",900000000L,Currency.create("100.00"),"Loc4","Major1","Minor2",false));
		t2.dumpTable();
		cnt = t2.deleteFirst(m6);
		System.out.println("Count:" + cnt);
		t2.dumpTable();
		m6.setMinor("Minor1");
		cnt = t2.deleteAll(m6);	
		System.out.println("Count:" + cnt);
		t2.dumpTable();
	}
	
	private void dmp_arraylist(ArrayList<Integer> r,String name){
		System.out.println(name + ":" + r.toString());		
	}
	private void dmp_item(ArrayList<Credit> r,String name){
		System.out.println("<<" + name + ">>");	
		for (Credit c : r){
			System.out.println(c.toString());	
		}	
	}

}
