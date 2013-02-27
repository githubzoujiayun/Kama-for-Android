package com.label305.kamav2_android;

public class KamaParam {

	public static String APPKEY = "";
	
	public static String URLPARAM = "?";
	
	public static enum AUTH_TYPE {NONE, OAUTH2, APIKEY, OAUTHANDKEY};
	
	public static final String RESPONSE = "response";
	
	public static String getApiKey() {
		return "apikey=" + APPKEY;
	}
	
}
