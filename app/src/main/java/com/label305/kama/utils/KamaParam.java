package com.label305.kama.utils;

public class KamaParam {

    public static final String URLPARAM = "?";
    public static final String URLPARAMCONCAT = "&";
    public static final String APIKEYPARAM = "apikey";

    public static final String RESPONSE = "response";
    public static final String ACCEPT = "Accept";
    public static final String APPLICATION_JSON = "application/json";

    @SuppressWarnings("HardcodedFileSeparator")
    public static final String APPLICATION_KAMA = "application/vnd.kama-v1+json";
    public static final String META = "meta";
    public static final String AUTHORIZATION = "Authorization";
    public static final String OAUTH2 = "OAuth2 ";

    private KamaParam() {
    }

    @SuppressWarnings("InnerClassFieldHidesOuterClassField")
    public enum AuthenticationType {
        NONE, OAUTH2, APIKEY, OAUTHANDKEY
    }
}
