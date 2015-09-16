package com.carbone.storeId;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.carbone.main.Global;
import com.carbone.utils.CustomException;

public class JUnitStoreIdTable {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test() {
		StoreIdTable t = new StoreIdTable("First");
		System.out.println("testCreditMapTable called");
		t.add(new StoreId("OldName1","NewName1"));
		t.add(new StoreId("OldName2","NewName2"));
		assertTrue (t.getSize()==2);
		assertTrue (t.getItem(0).getOldName().equals("OldName1"));
		assertTrue (t.getItem(1).getOldName().equals("OldName2"));
		assertTrue (t.getItem(0).getNewName().equals("NewName1"));
		assertTrue (t.getItem(1).getNewName().equals("NewName2"));

		String[] s = t.getNames();
		StringBuilder sb = new StringBuilder("Names:");
		for (String ss : s){
			sb.append(ss + ",");
		}
		System.out.println(sb.substring(0,sb.length()-1).toString());
		
		System.out.println("Write & Read");
		StoreIdTable t1 = new StoreIdTable("Second");
		t1.add(new StoreId("OldName1","NewName1"));
		t1.add(new StoreId("OldName2","NewName2"));
		t1.create(Global.TEST_PATH + "temp.csv");
		System.out.println(t1.toString());
		t1.dumpTable();
		t1.open(Global.TEST_PATH + "temp.csv");
		System.out.println(t1.toString());
		t1.dumpTable();
		
		StoreIdTable t2 = new StoreIdTable("Third");
		System.out.println("Sort Test");
		t2.add(new StoreId("OldName3","NewName1"));
		t2.add(new StoreId("OldName4","NewName2"));
		t2.add(new StoreId("OldName1","NewName1"));
		t2.add(new StoreId("OldName2","NewName2"));
		try {
			t2.sort(t2.getComparator("OldName"));
		} catch (CustomException e) {
			e.printStackTrace();
		}
		t2.dumpTable();
		
		StoreIdTable t3 = new StoreIdTable("Contains");
		System.out.println("Contains Test");
		t3.add(new StoreId("OldName3","NewName3"));
		t3.add(new StoreId("OldName4","NewName3"));
		t3.add(new StoreId("OldName1","NewName3"));
		t3.add(new StoreId("OldName2","NewName3"));
		t3.add(new StoreId("OldName3","NewName1"));
		t3.dumpTable();
		StoreId m1 = new StoreId();
		StoreId m2 = new StoreId();
		StoreId m3 = new StoreId();
		StoreId m6 = new StoreId();
		m1.setOldName("OldName5");
		m2.setOldName("OldName4");
		m3.setNewName("NewName3");
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
		StoreId value = new StoreId();
		value.setNewName("CHANGED");
		ArrayList<StoreId> first = t3.fetchFirst(m3);
		dmp_item(first,"First");
		ArrayList<StoreId> all = t3.fetchAll(m3);
		dmp_item(all,"All");
		
		System.out.println("\nTest Replace");
		int cnt = t3.replaceFirst(m3, value);
		System.out.println("Count:" + cnt);
		t3.dumpTable();
		dmp_item(first,"First");
		value.setNewName("CHANGED AGAIN");
		cnt = t3.replaceAll(m3, value);
		System.out.println("Count:" + cnt);
		t3.dumpTable();
		dmp_item(all,"All");
		
		System.out.println("\nTest Iterator");
		Iterator<StoreId> z;
		z = t3.getIterator();
		while(z.hasNext()) {
			StoreId element = z.next();
			System.out.println(element.getOldName());
		}
		for (StoreId zz : t){
			System.out.println(zz.getOldName());
		};
		
		System.out.println("\nTest Delete");
		t3.clear();
		t3.add(new StoreId("OldName3","NewName3"));
		t3.add(new StoreId("OldName4","NewName3"));
		t3.add(new StoreId("OldName1","NewName3"));
		t3.add(new StoreId("OldName2","NewName3"));
		t3.add(new StoreId("OldName3","NewName1"));
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
	private void dmp_item(ArrayList<StoreId> r,String name){
		System.out.println("<<" + name + ">>");	
		for (StoreId c : r){
			System.out.println(c.toString());	
		}	
	}

}
