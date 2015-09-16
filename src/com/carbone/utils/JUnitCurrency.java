package com.carbone.utils;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class JUnitCurrency {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test() {
		Currency a;
		Currency aZero = Currency.create();
		assertTrue(aZero.getCents() == 0);
		aZero.setCents(-1000);
		assertTrue(aZero.getCents() == -1000);
		a = Currency.createCents(-1000);
		assertTrue(a.getCents() == -1000);
		System.out.println(Currency.create("1.00").toString());
		System.out.println(Currency.create("-1.00").toString());
		System.out.println(Currency.create("0.01").toString());
		System.out.println(Currency.create("-0.01").toString());
		Currency aDollar = Currency.create("1.00");
		Currency result = Currency.create();
		result.setCents(aDollar.getCents()/3);
		System.out.println(result.toString());
		result.setCents((-aDollar.getCents())/3);
		System.out.println(result.toString());
		result.setCents((-aDollar.getCents()*10)/3);
		System.out.println(result.toString());
		System.out.println(Currency.create("XXX").toString());
		System.out.println(Currency.create("0.004").toString());
		System.out.println(Currency.create("-0.004").toString());
		System.out.println(Currency.create("0.005").toString());
		System.out.println(Currency.create("-0.006").toString());
		
	}

}
