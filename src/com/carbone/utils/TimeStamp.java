package com.carbone.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class TimeStamp {
	
	public static String toString(long time, String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);    
		Date resultdate = new Date(time);
		return sdf.format(resultdate);
	}

	public static long fromString(String input, String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date parseDate = sdf.parse(input);
            return parseDate.getTime();
        } 
        catch (ParseException e) {
            ;
        }
		return 0;
	}

}
