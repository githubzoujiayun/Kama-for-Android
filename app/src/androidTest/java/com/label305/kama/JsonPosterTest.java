package com.label305.kama;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.label305.kama.exceptions.KamaException;
import com.label305.kama.http.PostExecutor;
import com.label305.kama.JsonPoster;
import com.label305.kama.utils.KamaParam;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.HttpURLConnection;
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

@SuppressWarnings({"unchecked", "DuplicateStringLiteralInspection", "rawtypes", "UnusedDeclaration", "AccessingNonPublicFieldOfAnotherObject"})
public class JsonPosterTest extends TestCase {

    private static final String MISSING_EXCEPTION = "Missing exception";

    private static final String URL = "URL";
    private static final String JSON_SINGLE = "{ \"integer\":4}";
    private static final String JSON_LIST = "[{\"integer\":4}]";
    private static final String JSON_LIST_TITLE = "{\"list\":[{\"integer\":4}]}";
    private static final String TITLE = "list";

    private static final Class<ParseObject> RETURN_TYPE = ParseObject.class;

    private JsonPoster<ParseObject> mJsonPoster;

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

        mJsonPoster = new JsonPoster<ParseObject>(RETURN_TYPE, mPostExecutor);

        when(mPostExecutor.post(anyString(), any(Map.class), any(HttpEntity.class))).thenReturn(mHttpResponse);
        when(mHttpResponse.getEntity()).thenReturn(mHttpEntity);
    }

    public void testPostObject() throws Exception {
        mJsonPoster.setUrl(URL);

        when(mHttpResponse.getStatusLine()).thenReturn(mStatusLine);
        when(mStatusLine.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(mHttpEntity.getContent()).thenReturn(IOUtils.toInputStream(JSON_SINGLE));

        Object result = mJsonPoster.execute();

        verify(mPostExecutor).post(eq(URL), any(Map.class), any(HttpEntity.class));

        assertThat(result, is(not(nullValue())));
        assertThat(result, is(instanceOf(RETURN_TYPE)));
        assertThat(((ParseObject) result).mInteger, is(4));
    }

    public void testPostObjectsList() throws Exception {
        mJsonPoster.setUrl(URL);

        when(mHttpResponse.getStatusLine()).thenReturn(mStatusLine);
        when(mStatusLine.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(mHttpEntity.getContent()).thenReturn(IOUtils.toInputStream(JSON_LIST));

        Object result = mJsonPoster.executeReturnsObjectsList();
        verify(mPostExecutor).post(eq(URL), any(Map.class), any(HttpEntity.class));

        assertThat(result, is(not(nullValue())));
        assertThat(result, is(instanceOf(List.class)));

        assertThat(((Collection<?>) result).size(), is(1));
        assertThat(((List<?>) result).get(0), is(instanceOf(RETURN_TYPE)));
    }

    public void testPostObjectsListWithTitle() throws Exception {
        mJsonPoster.setUrl(URL);
        mJsonPoster.setJsonTitle(TITLE);

        when(mHttpResponse.getStatusLine()).thenReturn(mStatusLine);
        when(mStatusLine.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(mHttpEntity.getContent()).thenReturn(IOUtils.toInputStream(JSON_LIST_TITLE));

        Object result = mJsonPoster.executeReturnsObjectsList();
        verify(mPostExecutor).post(eq(URL), any(Map.class), any(HttpEntity.class));

        assertThat(result, is(not(nullValue())));
        assertThat(result, is(instanceOf(List.class)));

        List<?> list = (List<?>) result;
        assertThat(list.size(), is(1));
        assertThat(list.get(0), is(instanceOf(RETURN_TYPE)));
    }

    public void testExecutePostAddsHeader() throws Exception {
        mJsonPoster.setUrl(URL);
        mJsonPoster.setJsonTitle(TITLE);

        when(mHttpResponse.getStatusLine()).thenReturn(mStatusLine);
        when(mStatusLine.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(mHttpEntity.getContent()).thenReturn(IOUtils.toInputStream(JSON_LIST_TITLE));

        mJsonPoster.executeReturnsObjectsList();

        ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mPostExecutor).post(eq(URL), mapArgumentCaptor.capture(), any(HttpEntity.class));

        Map<String, Object> usedHeaderData = mapArgumentCaptor.getValue();
        assertThat(usedHeaderData.size(), is(greaterThan(0)));
        assertThat(usedHeaderData.keySet(), contains(KamaParam.ACCEPT));
        assertThat(usedHeaderData.get(KamaParam.ACCEPT).toString(), is(KamaParam.APPLICATION_JSON));
    }

    public void testNoUrlThrowsIllegalArgumentException() throws KamaException {
        try {
            mJsonPoster.execute();
            fail(MISSING_EXCEPTION);
        } catch (IllegalArgumentException ignored) {
            /* Success */
        }
    }

    public void testNoReturnTypeClass() throws Exception {
        JsonPoster<ParseObject> jsonPoster = new JsonPoster<ParseObject>(mPostExecutor);
        jsonPoster.setUrl(URL);

        when(mHttpResponse.getStatusLine()).thenReturn(mStatusLine);
        when(mStatusLine.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(mHttpEntity.getContent()).thenReturn(IOUtils.toInputStream(JSON_SINGLE));

        ParseObject result = jsonPoster.execute();
        assertThat(result, is(nullValue()));
    }

    public void testVoidReturnType() throws Exception {
        JsonPoster<Void> jsonPoster = new JsonPoster<Void>(mPostExecutor);
        jsonPoster.setUrl(URL);

        when(mHttpResponse.getStatusLine()).thenReturn(mStatusLine);
        when(mStatusLine.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(mHttpEntity.getContent()).thenReturn(IOUtils.toInputStream(JSON_SINGLE));

        Void result = jsonPoster.execute();
        assertThat(result, is(nullValue()));
    }

    public void testNonHttpOkResult() throws Exception {
        mJsonPoster.setUrl(URL);

        when(mHttpResponse.getStatusLine()).thenReturn(mStatusLine);
        when(mStatusLine.getStatusCode()).thenReturn(HttpURLConnection.HTTP_NOT_FOUND);
        when(mHttpEntity.getContent()).thenReturn(IOUtils.toInputStream(JSON_SINGLE));

        try {
            mJsonPoster.execute();
            fail(MISSING_EXCEPTION);
        } catch (KamaException ignored) {
            /* Success */
        }
    }

    private static class ParseObject {

        @JsonProperty("integer")
        private int mInteger;

    }
}
