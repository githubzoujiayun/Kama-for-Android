package com.label305.kamav2_android;

public class KamaParam {

	protected static String APPKEY = "";

	public static String AUTHURL = "";
	
	public static String URLPARAM = "?";
	
	public static enum AUTH_TYPE {NONE, OAUTH2, APIKEY, OAUTHANDKEY};
	
	public static String getApiKey() {
		return "apikey=" + APPKEY;
	}
	
}
