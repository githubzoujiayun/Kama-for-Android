package com.label305.kama;

public class KamaParam {

    public static final String URLPARAM = "?";
    public static final String URLPARAMCONCAT = "&";
    public static final String APIKEYPARAM = "apikey";

    public static final String RESPONSE = "response";
    public static final String ACCEPT = "Accept";
    public static final String APPLICATION_JSON = "application/json";
    public static final String APPLICATION_KAMA = "application/vnd.kama-v1+json";

    private KamaParam() {
    }

    public enum AuthenticationType {
        NONE, OAUTH2, APIKEY, OAUTHANDKEY
    }
}
