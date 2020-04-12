package com.credit.card.processor.util;

import com.credit.card.processor.constants.Constant;

import java.util.UUID;
import java.util.regex.Pattern;

public class Utils {
	public static String getUniqueID(){
		return UUID.randomUUID().toString();
	}
}
