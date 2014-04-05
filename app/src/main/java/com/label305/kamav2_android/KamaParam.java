package com.label305.kamav2_android;

public class KamaParam {

    public static final String URLPARAM = "?";
    public static final String URLPARAMCONCAT = "&";
    public static final String APIKEYPARAM = "apikey";

    public static final String RESPONSE = "response";

    private KamaParam() {
    }

    public enum AuthenticationType {
        NONE, OAUTH2, APIKEY, OAUTHANDKEY
    }
}
