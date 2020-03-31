package com.credit.card.processor.util;

import java.util.UUID;

public class Utils {
	public static String getUniqueID(){
		return UUID.randomUUID().toString();
	}
}
