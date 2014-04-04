package com.label305.kama.json.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.label305.kama.KamaParam;
import com.label305.kama.exceptions.KamaException;
import com.label305.kama.json.JsonPostExecutor;
import com.label305.stan.http.PostExecutor;
import com.label305.stan.http.StatusCodes;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings({"unchecked", "rawtypes", "DuplicateStringLiteralInspection"})
public class JsonPostExecutorTest extends TestCase {

    private static final String MISSING_EXCEPTION = "Missing exception";

    private static final String URL = "URL";
    private static final String JSON_SINGLE = "{ \"integer\":4}";
    private static final String JSON_LIST = "[{\"integer\":4}]";
    private static final String JSON_LIST_TITLE = "{\"list\":[{\"integer\":4}]}";
    private static final String TITLE = "list";

    private static final Class<ParseObject> RETURN_TYPE = ParseObject.class;


    private JsonPostExecutor mJsonPostExecutor;

    @Mock
    private PostExecutor mPostExecutor;

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

        mJsonPostExecutor = new JsonPostExecutor(mPostExecutor);

        when(mPostExecutor.post(anyString(), any(Map.class), any(HttpEntity.class))).thenReturn(mHttpResponse);
        when(mHttpResponse.getEntity()).thenReturn(mHttpEntity);
    }

    public void testPostObject() throws Exception {
        mJsonPostExecutor.setUrl(URL);
        mJsonPostExecutor.setReturnTypeClass(RETURN_TYPE);

        when(mHttpResponse.getStatusLine()).thenReturn(mStatusLine);
        when(mStatusLine.getStatusCode()).thenReturn(StatusCodes.HTTP_OK);
        when(mHttpEntity.getContent()).thenReturn(IOUtils.toInputStream(JSON_SINGLE));

        Object result = mJsonPostExecutor.execute();

        verify(mPostExecutor).post(eq(URL), any(Map.class), any(HttpEntity.class));

        assertThat(result, is(not(nullValue())));
        assertThat(result, is(instanceOf(RETURN_TYPE)));
        assertThat(((ParseObject) result).mInteger, is(4));
    }

    public void testPostObjectsList() throws Exception {
        mJsonPostExecutor.setUrl(URL);
        mJsonPostExecutor.setReturnTypeClass(RETURN_TYPE);

        when(mHttpResponse.getStatusLine()).thenReturn(mStatusLine);
        when(mStatusLine.getStatusCode()).thenReturn(StatusCodes.HTTP_OK);
        when(mHttpEntity.getContent()).thenReturn(IOUtils.toInputStream(JSON_LIST));

        Object result = mJsonPostExecutor.executeReturnsObjectsList();
        verify(mPostExecutor).post(eq(URL), any(Map.class), any(HttpEntity.class));

        assertThat(result, is(not(nullValue())));
        assertThat(result, is(instanceOf(List.class)));

        assertThat(((Collection<?>) result).size(), is(1));
        assertThat(((List<?>) result).get(0), is(instanceOf(RETURN_TYPE)));
    }

    public void testPostObjectsListWithTitle() throws Exception {
        mJsonPostExecutor.setUrl(URL);
        mJsonPostExecutor.setReturnTypeClass(RETURN_TYPE);
        mJsonPostExecutor.setJsonTitle(TITLE);

        when(mHttpResponse.getStatusLine()).thenReturn(mStatusLine);
        when(mStatusLine.getStatusCode()).thenReturn(StatusCodes.HTTP_OK);
        when(mHttpEntity.getContent()).thenReturn(IOUtils.toInputStream(JSON_LIST_TITLE));

        Object result = mJsonPostExecutor.executeReturnsObjectsList();
        verify(mPostExecutor).post(eq(URL), any(Map.class), any(HttpEntity.class));

        assertThat(result, is(not(nullValue())));
        assertThat(result, is(instanceOf(List.class)));

        List<?> list = (List<?>) result;
        assertThat(list.size(), is(1));
        assertThat(list.get(0), is(instanceOf(RETURN_TYPE)));
    }

    public void testExecutePostAddsHeader() throws Exception {
        mJsonPostExecutor.setUrl(URL);
        mJsonPostExecutor.setReturnTypeClass(RETURN_TYPE);
        mJsonPostExecutor.setJsonTitle(TITLE);

        when(mHttpResponse.getStatusLine()).thenReturn(mStatusLine);
        when(mStatusLine.getStatusCode()).thenReturn(StatusCodes.HTTP_OK);
        when(mHttpEntity.getContent()).thenReturn(IOUtils.toInputStream(JSON_LIST_TITLE));

        mJsonPostExecutor.executeReturnsObjectsList();

        ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mPostExecutor).post(eq(URL), mapArgumentCaptor.capture(), any(HttpEntity.class));

        Map<String, Object> usedHeaderData = mapArgumentCaptor.getValue();
        assertThat(usedHeaderData.size(), is(greaterThan(0)));
        assertThat(usedHeaderData.keySet(), contains(KamaParam.ACCEPT));
        assertThat(usedHeaderData.get(KamaParam.ACCEPT).toString(), is(KamaParam.APPLICATION_JSON));
    }

    public void testNoReturnTypeClass() throws Exception {
        mJsonPostExecutor.setUrl(URL);

        when(mHttpResponse.getStatusLine()).thenReturn(mStatusLine);
        when(mStatusLine.getStatusCode()).thenReturn(StatusCodes.HTTP_OK);
        when(mHttpEntity.getContent()).thenReturn(IOUtils.toInputStream(JSON_SINGLE));

        Object result = mJsonPostExecutor.execute();

        verify(mPostExecutor).post(eq(URL), any(Map.class), any(HttpEntity.class));

        assertThat(result, is(nullValue()));
    }

    public void testNoUrlThrowsIllegalArgumentException() throws KamaException {
        mJsonPostExecutor.setReturnTypeClass(RETURN_TYPE);
        try {
            mJsonPostExecutor.execute();
            fail(MISSING_EXCEPTION);
        } catch (IllegalArgumentException ignored) {
            /* Success */
        }
    }


    public void testNonHttpOkResult() throws Exception {
        mJsonPostExecutor.setUrl(URL);
        mJsonPostExecutor.setReturnTypeClass(RETURN_TYPE);

        when(mHttpResponse.getStatusLine()).thenReturn(mStatusLine);
        when(mStatusLine.getStatusCode()).thenReturn(StatusCodes.HTTP_NOT_FOUND);
        when(mHttpEntity.getContent()).thenReturn(IOUtils.toInputStream(JSON_SINGLE));

        try {
            mJsonPostExecutor.execute();
            fail(MISSING_EXCEPTION);
        } catch (KamaException ignored) {
            /* Success */
        }
    }

    @SuppressWarnings("PublicField")
    public static class ParseObject {

        @JsonProperty("integer")
        public int mInteger;

    }
}
