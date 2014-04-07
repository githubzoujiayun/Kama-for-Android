package com.label305.kama.parser.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.label305.kama.exceptions.JsonKamaException;
import com.label305.kama.parser.KamaJsonParser;

import junit.framework.TestCase;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@SuppressWarnings({"DuplicateStringLiteralInspection", "AccessingNonPublicFieldOfAnotherObject", "UnusedDeclaration"})
public class KamaJsonParserTest extends TestCase {

    private static final String JSON_SINGLE = "{\"meta\":{\"code\":200},\"response\":{ \"integer\":4}}";
    private static final String JSON_SINGLE_TITLE = "{\"meta\":{\"code\":200},\"response\":{\"title\":{\"integer\":4}}}";
    private static final String JSON_LIST = "{\"meta\":{\"code\":200},\"response\":[{\"integer\":4}]}";
    private static final String JSON_LIST_TITLE = "{\"meta\":{\"code\":200},\"response\":{\"title\":[{\"integer\":4}]}}";

    private static final String JSON_MISFORMAT = "\"integer\":4";
    private static final String JSON_MISSING_META = "{\"integer\":4}";

    private static final String TITLE = "title";
    private static final Class<ParseObject> RETURN_TYPE = ParseObject.class;

    private KamaJsonParser<ParseObject> mKamaJsonParser;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mKamaJsonParser = new KamaJsonParser<ParseObject>(RETURN_TYPE);
    }

    /* Normal behavior tests */

    public void testParseObject() throws JsonKamaException {
        ParseObject parseObject = mKamaJsonParser.parseObject(JSON_SINGLE, null);
        assertThat(parseObject.mInteger, is(4));
    }

    public void testParseObjectWithTitle() throws JsonKamaException {
        ParseObject parseObject = mKamaJsonParser.parseObject(JSON_SINGLE_TITLE, TITLE);
        assertThat(parseObject.mInteger, is(4));
    }

    public void testParseObjectsList() throws JsonKamaException {
        List<ParseObject> parseObjects = mKamaJsonParser.parseObjectsList(JSON_LIST, null);

        assertThat(parseObjects, hasSize(greaterThan(0)));
        assertThat(parseObjects.get(0).mInteger, is(4));
    }

    public void testParseObjectsListWithTitle() throws JsonKamaException {
        List<ParseObject> parseObjects = mKamaJsonParser.parseObjectsList(JSON_LIST_TITLE, TITLE);

        assertThat(parseObjects, hasSize(greaterThan(0)));
        assertThat(parseObjects.get(0).mInteger, is(4));
    }

    /* Wrong behavior tests */
    public void testParseMisformattedObject() {
        try {
            mKamaJsonParser.parseObject(JSON_MISFORMAT, null);
            fail("Missing Exception");
        } catch (JsonKamaException ignored) {
            /* Success */
        }
    }

    public void testParseMissingMeta() {
        try {
            mKamaJsonParser.parseObject(JSON_MISSING_META, null);
            fail("Missing Exception");
        } catch (JsonKamaException ignored) {
            /* Success */
        }
    }

    private static class ParseObject {

        @JsonProperty("integer")
        private int mInteger;

    }
}
