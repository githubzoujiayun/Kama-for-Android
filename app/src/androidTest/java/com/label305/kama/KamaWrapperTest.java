package com.label305.kama;

import android.test.InstrumentationTestCase;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.label305.kama.JsonGetter;
import com.label305.kama.KamaWrapper;
import com.label305.kama.exceptions.KamaException;
import com.label305.kama.http.GetExecutor;
import com.label305.kama.objects.KamaObject;
import com.label305.kama.utils.KamaParam;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings({"HardCodedStringLiteral", "PublicInnerClass", "UnusedDeclaration"})
public class KamaWrapperTest extends InstrumentationTestCase {

    private static final String JSON = "{\n" +
            "    \"meta\": {\n" +
            "        \"code\": 200,\n" +
            "        \"headers\": [],\n" +
            "        \"timestamp\": \"2014-05-02 17:13:59\"\n" +
            "    },\n" +
            "    \"response\": {\n" +
            "        \"MyObject\": {\n" +
            "            \"name\": \"Niek\"\n" +
            "        }\n" +
            "    }\n" +
            "}";
    public static final String JSON_TITLE = "MyObject";
    public static final String API_KEY = "apiKey";


    @Mock
    private GetExecutor mGetExecutor;

    @Mock
    private HttpResponse mHttpResponse;

    @Mock
    private HttpEntity mHttpEntity;

    @Mock
    private StatusLine mStatusLine;

    @Mock
    private JsonGetter<KamaObject> mJsonGetter;

    @Mock
    private KamaObject mKamaObject;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());

        MockitoAnnotations.initMocks(this);

        Map<String, Object> subMap = new HashMap<>(1);
        subMap.put("name", "Niek");

        Map<String, Object> responseMap = new HashMap<>(1);
        responseMap.put("MyObject", subMap);

        when(mJsonGetter.execute()).thenReturn(mKamaObject);
        when(mKamaObject.getResponseMap()).thenReturn(responseMap);
        when(mGetExecutor.get(anyString(), any(Map.class))).thenReturn(mHttpResponse);
        when(mHttpResponse.getEntity()).thenReturn(mHttpEntity);
        when(mHttpEntity.getContent()).thenReturn(IOUtils.toInputStream(JSON));
        when(mHttpResponse.getStatusLine()).thenReturn(mStatusLine);
        when(mStatusLine.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    }

    /**
     * Tests whether the parsing of the complete url results in a valid MyObject instance.
     */
    public void testGetMyObject() throws KamaException {
        /* Test real JSON parsing */
        mJsonGetter = new JsonGetter<>(KamaObject.class, mGetExecutor);
        mJsonGetter.setUrl("url");

        KamaWrapper<MyObject> requester = new KamaWrapper<>(getInstrumentation().getContext(), mJsonGetter, MyObject.class);
        requester.setJsonTitle(JSON_TITLE);

        MyObject myObject = requester.execute();
        assertThat(myObject, is(not(nullValue())));
        assertThat(myObject.getName(), is("Niek"));
    }

    /**
     * Tests whether the Kama headers are set.
     */
    public void testHeadersSet() throws KamaException {
        KamaWrapper<MyObject> requester = new KamaWrapper<>(getInstrumentation().getContext(), mJsonGetter, MyObject.class);
        requester.setJsonTitle(JSON_TITLE);

        requester.execute();

        verify(mJsonGetter).addHeader(KamaParam.ACCEPT, KamaParam.APPLICATION_KAMA);
        verify(mJsonGetter, never()).addHeader(eq(KamaParam.AUTHORIZATION), any());
    }

    /**
     * Tests whether the OAuth2 header is set when using OAuth2 authentication.
     */
    public void testOAuthHeaderSet() throws KamaException {
        KamaWrapper<MyObject> requester = new KamaWrapper<>(getInstrumentation().getContext(), mJsonGetter, MyObject.class);
        requester.useOAuthAuthentication();
        requester.setJsonTitle(JSON_TITLE);

        requester.execute();

        verify(mJsonGetter).addHeader(KamaParam.ACCEPT, KamaParam.APPLICATION_KAMA);
        verify(mJsonGetter).addHeader(KamaParam.AUTHORIZATION, KamaParam.OAUTH2 + "null"); /* The oauth key hasn't been set */
    }

    /**
     * Tests that no api key param is set when not using any authentication.
     */
    public void testUrlParametersNotSet() throws KamaException {
        KamaWrapper<MyObject> requester = new KamaWrapper<>(getInstrumentation().getContext(), mJsonGetter, MyObject.class);
        requester.setJsonTitle(JSON_TITLE);

        requester.execute();

        verify(mJsonGetter, never()).addUrlParameter(eq(KamaParam.APIKEYPARAM), any());
    }

    /**
     * Tests whether the api key param is set when using the api key authentication.
     */
    public void testUrlParametersSetApiKey() throws KamaException {
        KamaWrapper<MyObject> requester = new KamaWrapper<>(getInstrumentation().getContext(), mJsonGetter, MyObject.class);
        requester.useApiKeyAuthentication(API_KEY);
        requester.setJsonTitle(JSON_TITLE);

        requester.execute();

        verify(mJsonGetter).addUrlParameter(KamaParam.APIKEYPARAM, API_KEY);
    }

    /**
     * Tests whether the api key param is set when using the OAuth2 and api key authentication.
     */
    public void testUrlParametersSetOAuthAndApiKey() throws KamaException {
        KamaWrapper<MyObject> requester = new KamaWrapper<>(getInstrumentation().getContext(), mJsonGetter, MyObject.class);
        requester.useOAuthAndApiKeyAuthentication(API_KEY);
        requester.setJsonTitle(JSON_TITLE);

        requester.execute();

        verify(mJsonGetter).addUrlParameter(KamaParam.APIKEYPARAM, API_KEY);
    }

    public static class MyObject {

        @JsonProperty("name")
        private String mName;

        public String getName() {
            return mName;
        }
    }
}
