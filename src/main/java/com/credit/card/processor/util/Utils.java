package com.credit.card.processor.util;

import java.util.UUID;
import java.util.regex.Pattern;

public class Utils {
	public static String getUniqueID(){
		return UUID.randomUUID().toString();
	}

	public static boolean fileNameMatcher(String fileName){
		String regex1 = "^transaction[0-9]{4}.*";
		String regex2 = "^reference[0-9]{4}.*";
		return (Pattern.matches(regex1, fileName)|| Pattern.matches(regex2,fileName));
	}
}
