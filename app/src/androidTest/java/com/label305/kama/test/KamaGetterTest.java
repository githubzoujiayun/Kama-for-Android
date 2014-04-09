package com.label305.kama.test;

import android.test.AndroidTestCase;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.label305.kama.KamaGetter;
import com.label305.kama.exceptions.KamaException;
import com.label305.kama.objects.KamaError;
import com.label305.kama.utils.KamaParam;
import com.label305.stan.http.GetExecutor;
import com.label305.stan.http.StatusCodes;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.startsWith;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings({"unchecked", "UnusedDeclaration"})
public class KamaGetterTest extends AndroidTestCase {

    private static final String API_KEY = "kljahhsflkahjsdf";
    private static final String URL = "URL";

    private static final String JSON_SINGLE = "{\"meta\":{\"code\":200},\"response\":{ \"integer\":4}}";
    private static final String JSON_ERROR = "{\"meta\":{\"code\":404},\"response\":null}";

    private static final Class<ParseObject> RETURN_TYPE = ParseObject.class;


    private KamaGetter<ParseObject> mKamaGetter;

    @Mock
    private GetExecutor mGetExecutor;

    @Mock
    private HttpResponse mHttpResponse;

    @Mock
    private HttpEntity mHttpEntity;

    @Mock
    private StatusLine mStatusLine;


    @Override
    protected void setUp() throws Exception {
        super.setUp();

        MockitoAnnotations.initMocks(this);

        mKamaGetter = new KamaGetter<ParseObject>(RETURN_TYPE, getContext(), API_KEY, mGetExecutor);

        when(mGetExecutor.get(anyString(), any(Map.class))).thenReturn(mHttpResponse);
        when(mHttpResponse.getEntity()).thenReturn(mHttpEntity);
    }

    /* Correct executions have already been tested in JsonGetter. One is enough. */
    public void testGetObject() throws Exception {
        mKamaGetter.setUrl(URL);
        mKamaGetter.setAuthType(KamaParam.AuthenticationType.APIKEY);

        when(mHttpResponse.getStatusLine()).thenReturn(mStatusLine);
        when(mStatusLine.getStatusCode()).thenReturn(StatusCodes.HTTP_OK);
        when(mHttpEntity.getContent()).thenReturn(IOUtils.toInputStream(JSON_SINGLE));

        Object result = mKamaGetter.execute();

        verify(mGetExecutor).get(startsWith(URL), any(Map.class));

        assertThat(result, is(not(nullValue())));
        assertThat(result, is(instanceOf(RETURN_TYPE)));
        assertThat(((ParseObject) result).mInteger, is(4));
    }

    /* Test KamaError */
    public void testGetKamaError() throws Exception {
        mKamaGetter.setUrl(URL);
        mKamaGetter.setAuthType(KamaParam.AuthenticationType.APIKEY);

        when(mHttpResponse.getStatusLine()).thenReturn(mStatusLine);
        when(mStatusLine.getStatusCode()).thenReturn(StatusCodes.HTTP_NOT_FOUND);
        when(mHttpEntity.getContent()).thenReturn(IOUtils.toInputStream(JSON_ERROR));

        try {
            mKamaGetter.execute();
            fail("Missing Exception");
        } catch (KamaException e) {
            verify(mGetExecutor).get(startsWith(URL), any(Map.class));

            Object kamaError = e.getKamaError();
            assertThat(kamaError, is(not(nullValue())));
            assertThat(kamaError, is(instanceOf(KamaError.class)));
            assertThat(((KamaError) kamaError).getStatusCode(), is(StatusCodes.HTTP_NOT_FOUND));
        }
    }

    private static class ParseObject {

        @JsonProperty("integer")
        private int mInteger;

    }
}
