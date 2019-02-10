package com.optima.nisp.utility;

public class StringUtils {

	private static int CIF_KEY_LENGTH = 20;
	private static int ACC_NO_LENGTH = 19;
	
	public static String normalizeCifKey(String cifKey){
		if (cifKey == null) {
			return null;
		}
		
		if (cifKey.length() > CIF_KEY_LENGTH) {
			return cifKey.substring(cifKey.length()-CIF_KEY_LENGTH, CIF_KEY_LENGTH);
		}
		
		String tCifKey = cifKey;
		for (int i=cifKey.length();i<CIF_KEY_LENGTH;i++) {
			tCifKey = "0" + tCifKey;
		}
		return tCifKey;
	}
	
}
