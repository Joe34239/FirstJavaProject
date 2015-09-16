package com.carbone.creditMap;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.carbone.main.Global;
import com.carbone.utils.CustomException;

public class JUnitCreditMapTable {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test() {
		CreditMapTable t = new CreditMapTable("First");
		System.out.println("testCreditMapTable called");
		t.add(new CreditMap("Tag1","Major1","Minor1"));
		t.add(new CreditMap("Tag2","Major2","Minor2"));
		assertTrue (t.getSize()==2);
		assertTrue (t.getItem(0).getTag().equals("Tag1"));
		assertTrue (t.getItem(1).getTag().equals("Tag2"));
		assertTrue (t.getItem(0).getMajor().equals("Major1"));
		assertTrue (t.getItem(1).getMajor().equals("Major2"));
		assertTrue (t.getItem(0).getMinor().equals("Minor1"));
		assertTrue (t.getItem(1).getMinor().equals("Minor2"));

		String[] s = t.getNames();
		StringBuilder sb = new StringBuilder("Names:");
		for (String ss : s){
			sb.append(ss + ",");
		}
		System.out.println(sb.substring(0,sb.length()-1).toString());
		
		System.out.println("Write & Read");
		CreditMapTable t1 = new CreditMapTable("Second");
		t1.add(new CreditMap("Tag1","Major1","Minor1"));
		t1.add(new CreditMap("Tag2","Major2","Minor2"));
		t1.create(Global.TEST_PATH + "temp.csv");
		System.out.println(t1.toString());
		t1.dumpTable();
		t1.open(Global.TEST_PATH + "temp.csv");
		System.out.println(t1.toString());
		t1.dumpTable();
		
		CreditMapTable t2 = new CreditMapTable("Third");
		System.out.println("Sort Test");
		t2.add(new CreditMap("Tag3","Major1","Minor1"));
		t2.add(new CreditMap("Tag4","Major2","Minor2"));
		t2.add(new CreditMap("Tag1","Major1","Minor1"));
		t2.add(new CreditMap("Tag2","Major2","Minor2"));
		try {
			t2.sort(t2.getComparator("Tag"));
		} catch (CustomException e) {
			e.printStackTrace();
		}
		t2.dumpTable();
		
		CreditMapTable t3 = new CreditMapTable("Contains");
		System.out.println("Contains Test");
		t3.add(new CreditMap("Tag3","Major1","Minor3"));
		t3.add(new CreditMap("Tag4","Major2","Minor3"));
		t3.add(new CreditMap("Tag1","Major3","Minor3"));
		t3.add(new CreditMap("Tag2","Major4","Minor3"));
		t3.add(new CreditMap("Tag3","Major5","Minor1"));
		t3.dumpTable();
		CreditMap m1 = new CreditMap();
		CreditMap m2 = new CreditMap();
		CreditMap m3 = new CreditMap();
		CreditMap m6 = new CreditMap();
		m1.setTag("Tag5");
		m2.setTag("Tag4");
		m3.setMinor("Minor3");
//		m4.setName("five");
//		m5.setTimeStamp(time+3000);
//		m5.setAction(TransactionAction.DELETE);
		ArrayList<Integer> result;
		result = t3.contains(m1);
		dmp_arraylist(result,"m1");
		result = t3.contains(m2);
		dmp_arraylist(result,"m2");
		result = t3.contains(m3);
		dmp_arraylist(result,"m3");
		result = t3.contains(m6);
		dmp_arraylist(result,"m6");
		
		System.out.println("\nTest Fetch");
		CreditMap value = new CreditMap();
		value.setMinor("CHANGED");
		ArrayList<CreditMap> first = t3.fetchFirst(m3);
		dmp_item(first,"First");
		ArrayList<CreditMap> all = t3.fetchAll(m3);
		dmp_item(all,"All");
		
		System.out.println("\nTest Replace");
		int cnt = t3.replaceFirst(m3, value);
		System.out.println("Count:" + cnt);
		t3.dumpTable();
		dmp_item(first,"First");
		value.setMinor("CHANGED AGAIN");
		cnt = t3.replaceAll(m3, value);
		System.out.println("Count:" + cnt);
		t3.dumpTable();
		dmp_item(all,"All");
		
		System.out.println("\nTest Iterator");
		Iterator<CreditMap> z;
		z = t3.getIterator();
		while(z.hasNext()) {
			CreditMap element = z.next();
			System.out.println(element.getTag());
		}
		for (CreditMap zz : t){
			System.out.println(zz.getTag());
		}
		
		System.out.println("\nTest Delete");
		t3.clear();
		t3.add(new CreditMap("Tag3","Major1","Minor3"));
		t3.add(new CreditMap("Tag4","Major2","Minor3"));
		t3.add(new CreditMap("Tag1","Major3","Minor3"));
		t3.add(new CreditMap("Tag2","Major4","Minor3"));
		t3.add(new CreditMap("Tag3","Major5","Minor1"));
		t3.dumpTable();
		cnt = t3.deleteFirst(m3);
		System.out.println("Count:" + cnt);
		t3.dumpTable();
		cnt = t3.deleteAll(m3);	
		System.out.println("Count:" + cnt);
		t3.dumpTable();
	}
	
	private void dmp_arraylist(ArrayList<Integer> r,String name){
		System.out.println(name + ":" + r.toString());		
	}
	private void dmp_item(ArrayList<CreditMap> r,String name){
		System.out.println("<<" + name + ">>");	
		for (CreditMap c : r){
			System.out.println(c.toString());	
		}	
	}

}
