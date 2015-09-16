package com.carbone.utils;
//  TODO add cache
public final class Log {
	public static void i(String TAG, String info){
		System.out.println("INFO---->[" + TAG + "]" + info);
	}
	public static void w(String TAG, String warning){
		System.out.println("WARNING->[" + TAG + "]" + warning);
	}
	public static void e(String TAG, String error){
		System.out.println("ERROR--->[" + TAG + "]" + error);
	}
	public static void f(String TAG, String error){
		System.out.println("FATAL--->[" + TAG + "]" + error);
	}

}
