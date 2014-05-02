package com.label305.kama.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.label305.kama.NewKamaRequester;
import com.label305.kama.exceptions.KamaException;
import com.label305.kama.http.GetExecutor;
import com.label305.kama.objects.KamaObject;
import com.label305.kama.objects.MyObject;
import com.label305.kama.request.AbstractJsonRequester;
import com.label305.kama.request.JsonGetter;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.HttpURLConnection;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

public class NKRTest extends TestCase {

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



    @Mock
    GetExecutor mGetExecutor;

    @Mock
    HttpResponse mHttpResponse;

    @Mock
    HttpEntity mHttpEntity;

    @Mock
    private StatusLine mStatusLine;

    AbstractJsonRequester<KamaObject> mJsonRequester;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        MockitoAnnotations.initMocks(this);

        mJsonRequester = new JsonGetter<KamaObject>(KamaObject.class, mGetExecutor);

        when(mGetExecutor.get(anyString(), any(Map.class))).thenReturn(mHttpResponse);
        when(mHttpResponse.getEntity()).thenReturn(mHttpEntity);
        when(mHttpEntity.getContent()).thenReturn(IOUtils.toInputStream(JSON));
        when(mHttpResponse.getStatusLine()).thenReturn(mStatusLine);
        when(mStatusLine.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    }

    public void test() throws KamaException, JsonProcessingException {
        mJsonRequester.setUrl("lala");
        NewKamaRequester<MyObject> requester = new NewKamaRequester<MyObject>(mJsonRequester, MyObject.class);
        requester.setJsonTitle("MyObject");
        MyObject execute = requester.execute();
        assertThat(execute, is(not(nullValue())));
        assertThat(execute.getName(), is("Niek"));

    }
}
