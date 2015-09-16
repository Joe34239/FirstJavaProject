package com.carbone.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Stripper {
	/**
	 * MATCH
	 * 	Starts with "#",0 or more 'digits', 'space'
	 *  Ends with 3 'digit's, "-" 0 or more 'AnyChars'
	 *  Ends with 4 'digit's, 0 or more 'AnyChars'
	 *  Ends with 'space', 1 or more 'digit's
	 *  Ends with "-X"
	 *  Ends with "#", 0 or more 'AnyChar's
	 *  Ends with 0 or more 'space's, "-"
	 *  Ends with 0 or more 'space's
	 */
	private final static Pattern junkre = Pattern.compile(
			"(^#\\d* )|(\\d\\d\\d-.*$)|(\\d\\d\\d\\d.*$)|(\\s\\d+$)|(-X$)|(#.*$)|(\\s*-$)|(\\s*$)"
			);
	/**
	 * MATCH
	 * 	Ends with 1 or more 'alpha's, 1 or more digits
	 * 	Ends with 0 or more 'spaces'
	 */
	private final static Pattern junkre1 = Pattern.compile(
			"([a-zA-Z]+\\d+$)|(\\s*$)"
			);
	/**
	 * MATCH
	 * 	 Ends with 1 or more 'spaces, 1 or more 'alpha's, 1 or more 'spaces', 1 or more 'alpha's
	 *   (ie City and State - does not work for 2 word cities)
	 */
	private final static Pattern pdfjunk = Pattern.compile(
			"(\\s+[a-zA-Z]+\\s+[a-zA-Z]+$)"
			);
	/**
	 * MATCH
	 *  Ends with "-X" (ie appended prefix to guarantee unique credit keys)
	 * 	
	 */
	private final static Pattern pdfjunk2 = Pattern.compile(
			"(-X$)"
			);
	/**
	 * MATCH
	 * 	Ends with 1 or more 'spaces', one of these {SAN, SALT LAKE, SAN DIEGO, BUFFALO, NEW, AGOURA}
	 */
	private final static Pattern pdfjunk1 = Pattern.compile(
			"(\\s+SAN$)|(\\s+SALT LAKE$)|(\\s+SAN DIEGO$)|(\\s+NORTH$)|(\\s+BUFFALO$)|(\\s+NEW$)|(\\s+AGOURA$)"
			);
	
	public static String pdfStrip(String inp) throws CustomException{
		Matcher regexMatcher;
		String outp = inp;
		outp = outp.replaceAll("\\s+$", "");
		regexMatcher = pdfjunk.matcher(outp);
		outp = regexMatcher.replaceAll("");
		int cnt = 100;
		String s;
		while (true){
			regexMatcher = pdfjunk2.matcher(outp);
			s = regexMatcher.replaceAll("");
			regexMatcher = pdfjunk1.matcher(s);
			s = regexMatcher.replaceAll("");
			if (s.equals(outp)) break;
			outp = s;
			if (cnt-- <= 0) throw new CustomException("Stripper Taking Too Long");
		}
		outp = outp.replace("'", "").replaceAll("\\s+$","");
		outp = outp.replace(",", " ");
		if (outp.equals(""))outp = inp;
		outp = outp.replaceAll("&amp;","&");
		return outp;
	}
	public static String creditStrip(String inp) throws CustomException{
		Matcher regexMatcher;
		String s;
		String outp = inp;
		int cnt = 100;
		while (true){
			regexMatcher = junkre.matcher(outp);
			s = regexMatcher.replaceAll("");
			regexMatcher = junkre1.matcher(s);
			s = regexMatcher.replaceAll("");
			if (s.equals(outp)) break;
			outp = s;
			if (cnt-- <= 0) throw new CustomException("Stripper Taking Too Long");
		}
		outp = outp.replace("'", "").replaceAll("\\s+$","");
		outp = outp.replace(",", " ");
		if (outp.equals(""))outp = inp;
		return outp;
	}
	public static String checkStrip(String inp){
		/*
		 *   Here is the python code
		 *   	s = desc.rstrip() #Strip blanks at end
         *		while True:
         *			ss = self.pdfjunk2.sub('', s)
         *			if ss == s:
         *				break
         *			s = ss
         *		s = s.rstrip() #Strip blanks at end
         *		return s
		 */
		StringBuilder outp = new StringBuilder(inp);
		// TODO check results against Python
		return outp.toString();
	}
}
