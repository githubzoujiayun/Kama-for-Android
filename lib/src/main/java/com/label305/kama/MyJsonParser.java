package com.label305.kama;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.label305.kama.exceptions.JsonKamaException;

import java.io.IOException;
import java.util.List;

class MyJsonParser<ReturnType> {

    private final ObjectMapper mObjectMapper = new ObjectMapper();

    private final Class<ReturnType> mReturnTypeClass;

    MyJsonParser(final Class<ReturnType> returnTypeClass) {
        mReturnTypeClass = returnTypeClass;
    }

    /**
     * Parse given response string into an instance of ReturnType.
     * @param responseString the response string to parse
     * @param jsonTitle the title of the object in the json string, or {@code null} if there is no title.
     * @return the parsed ReturnType, or {@code null} if the given returnTypeClass was {@code null}  or {@code Void.class}.
     * @throws JsonKamaException when an exception was thrown parsing.
     */
    public ReturnType parseObject(final String responseString, final String jsonTitle) throws JsonKamaException, IOException {
        ReturnType result = null;
        JsonParser jsonParser = getJsonParserFromResponse(responseString);

        if (mReturnTypeClass != null && !mReturnTypeClass.equals(Void.class)) {
            try {
                result = getJsonObject(jsonParser, jsonTitle);
            } catch (JsonParseException | JsonMappingException e) {
                throw new JsonKamaException(e);
            }
        }
        return result;
    }

    /**
     * Parse given response string into a list of instances of ReturnType.
     * @param responseString the response string to parse
     * @param jsonTitle the title of the object in the json string, or {@code null} if there is no title.
     * @return the parsed list of ReturnTypes, or {@code null} if the given returnTypeClass was {@code null}  or {@code Void.class}.
     * @throws JsonKamaException when an exception was thrown parsing.
     */
    public List<ReturnType> parseObjectsList(final String responseString, final String jsonTitle) throws JsonKamaException, IOException {
        List<ReturnType> result = null;
        JsonParser jsonParser = getJsonParserFromResponse(responseString);

        if (mReturnTypeClass != null && !mReturnTypeClass.equals(Void.class)) {
            try {
                result = getJsonObjectsList(jsonParser, jsonTitle);
            } catch (JsonParseException | JsonMappingException e) {
                throw new JsonKamaException(e);
            }
        }
        return result;
    }


    protected JsonParser getJsonParserFromResponse(final String responseString) throws JsonKamaException, IOException {
        JsonParser result;

        JsonFactory jsonFactory = new JsonFactory();
        try {
            result = jsonFactory.createParser(responseString);
        } catch (JsonParseException e) {
            throw new JsonKamaException(e);
        }
        return result;
    }

    private ReturnType getJsonObject(final JsonParser jsonParser, final String objTitle) throws IOException, JsonKamaException {
        JsonParser currentJsonParser = jsonParser;
        if (objTitle != null) {
            JsonNode response = mObjectMapper.readTree(currentJsonParser);
            JsonNode responseStr = response.get(objTitle);
            if (responseStr == null) {
                throw new JsonKamaException("Unexpected jsontitle. Not found: " + objTitle);
            }
            currentJsonParser = responseStr.traverse();
        }
        return mObjectMapper.readValue(currentJsonParser, mReturnTypeClass);
    }

    private List<ReturnType> getJsonObjectsList(final JsonParser jsonParser, final String objTitle) throws JsonKamaException, IOException {
        JsonParser parser = jsonParser;
        parser.nextToken();
        if (objTitle != null) {
            JsonNode response = mObjectMapper.readTree(parser);
            JsonNode responseStr = response.get(objTitle);
            if (responseStr == null) {
                throw new JsonKamaException("Unexpected jsontitle. Not found: " + objTitle);
            }
            parser = responseStr.traverse();
        }
        return mObjectMapper.readValue(parser, mObjectMapper.getTypeFactory().constructCollectionType(List.class, mReturnTypeClass));
    }
}
