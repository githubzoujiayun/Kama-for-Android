package com.label305.kama.parser.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.label305.kama.exceptions.JsonKamaException;
import com.label305.kama.parser.MyJsonParser;

import junit.framework.TestCase;

import org.junit.Assert;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@SuppressWarnings({"UnusedDeclaration", "AccessingNonPublicFieldOfAnotherObject"})
public class MyJsonParserTest extends TestCase {

    private static final String JSON_SINGLE = "{ \"integer\":4}";
    private static final String JSON_SINGLE_TITLE = "{\"title\":{\"integer\":4}}";
    private static final String JSON_SINGLE_WRONG_TITLE = "{\"wrong_title\":{\"integer\":4}}";
    private static final String JSON_LIST = "[{\"integer\":4}]";
    private static final String JSON_LIST_TITLE = "{\"title\":[{\"integer\":4}]}";
    private static final String JSON_LIST_WRONG_TITLE = "{\"wrong_title\":[{\"integer\":4}]}";

    private static final String JSON_MISFORMAT = "\"integer\":4";

    private static final String TITLE = "title";
    private static final Class<ParseObject> RETURN_TYPE = ParseObject.class;

    private MyJsonParser<ParseObject> mMyJsonParser;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mMyJsonParser = new MyJsonParser<>(RETURN_TYPE);
    }

    /* Normal behavior tests */

    public void testParseObject() throws JsonKamaException {
        ParseObject parseObject = mMyJsonParser.parseObject(JSON_SINGLE, null);
        assertThat(parseObject.mInteger, is(4));
    }

    public void testParseObjectWithTitle() throws JsonKamaException {
        ParseObject parseObject = mMyJsonParser.parseObject(JSON_SINGLE_TITLE, TITLE);
        assertThat(parseObject.mInteger, is(4));
    }

    public void testParseObjectsList() throws JsonKamaException {
        List<ParseObject> parseObjects = mMyJsonParser.parseObjectsList(JSON_LIST, null);

        assertThat(parseObjects, hasSize(greaterThan(0)));
        assertThat(parseObjects.get(0).mInteger, is(4));
    }

    public void testParseObjectsListWithTitle() throws JsonKamaException {
        List<ParseObject> parseObjects = mMyJsonParser.parseObjectsList(JSON_LIST_TITLE, TITLE);

        assertThat(parseObjects, hasSize(greaterThan(0)));
        assertThat(parseObjects.get(0).mInteger, is(4));
    }

    /* Wrong behavior */

    public void testParseObjectWithMissingTitle() {
        try {
            mMyJsonParser.parseObject(JSON_SINGLE, TITLE);
            Assert.fail("Missing Exception");
        } catch (JsonKamaException ignored) {
            /* Success */
        }
    }

    public void testParseObjectWithExtraTitle() {
        try {
            mMyJsonParser.parseObject(JSON_SINGLE_TITLE, null);
            Assert.fail("Missing Exception");
        } catch (JsonKamaException ignored) {
            /* Success */
        }
    }

    public void testParseObjectWithWrongTitle() {
        try {
            mMyJsonParser.parseObject(JSON_SINGLE_WRONG_TITLE, TITLE);
            Assert.fail("Missing Exception");
        } catch (JsonKamaException ignored) {
            /* Success */
        }
    }

    public void testParseObjectsListWithMissingTitle() {
        try {
            mMyJsonParser.parseObjectsList(JSON_LIST, TITLE);
            Assert.fail("Missing Exception");
        } catch (JsonKamaException ignored) {
            /* Success */
        }
    }

    public void testParseObjectsListWithExtraTitle() {
        try {
            mMyJsonParser.parseObjectsList(JSON_LIST_TITLE, null);
            Assert.fail("Missing Exception");
        } catch (JsonKamaException ignored) {
            /* Success */
        }
    }

    public void testParseObjectsListWithWrongTitle() {
        try {
            mMyJsonParser.parseObjectsList(JSON_LIST_WRONG_TITLE, TITLE);
            Assert.fail("Missing Exception");
        } catch (JsonKamaException ignored) {
            /* Success */
        }
    }

    public void testParseMisformattedObject() {
        try {
            mMyJsonParser.parseObject(JSON_MISFORMAT, null);
            Assert.fail("Missing Exception");
        } catch (JsonKamaException ignored) {
            /* Success */
        }
    }

    public void testParseWrongObject() {
        try {
            new MyJsonParser<>(WrongObject.class).parseObject(JSON_SINGLE, null);
            Assert.fail("Missing Exception");
        } catch (JsonKamaException ignored) {
            /* Success */
        }
    }

    private static class ParseObject {

        @JsonProperty("integer")
        private int mInteger;

    }

    private static class WrongObject {
        @JsonProperty("number")
        private int mInteger;
    }
}
