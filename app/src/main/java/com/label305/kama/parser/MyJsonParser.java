package com.label305.kama.parser;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.label305.kama.exceptions.JsonKamaException;

import java.io.IOException;
import java.util.List;

public class MyJsonParser<ReturnType> {

    private final ObjectMapper mObjectMapper = new ObjectMapper();

    private final Class<ReturnType> mReturnTypeClass;

    public MyJsonParser(final Class<ReturnType> returnTypeClass) {
        mReturnTypeClass = returnTypeClass;
    }

    public ReturnType parseObject(final String responseString, final String objTitle) throws JsonKamaException {
        ReturnType result;
        JsonParser jsonParser = getJsonParserFromResponse(responseString);

//        if (ReturnType.class != Void.class) {
        try {
            result = getJsonObject(jsonParser, objTitle);
        } catch (JsonParseException e) {
            throw new JsonKamaException(e);
        } catch (JsonMappingException e) {
            throw new JsonKamaException(e);
        } catch (IOException e) {
            throw new JsonKamaException(e);
        }
//        }
        return result;
    }

    public List<ReturnType> parseObjectsList(final String responseString, final String objTitle) throws JsonKamaException {
        List<ReturnType> result;
        JsonParser jsonParser = getJsonParserFromResponse(responseString);

//        if (retType != null) {
        try {
            result = getJsonObjectsList(jsonParser, objTitle);
        } catch (JsonParseException e) {
            throw new JsonKamaException(e);
        } catch (JsonMappingException e) {
            throw new JsonKamaException(e);
        } catch (IOException e) {
            throw new JsonKamaException(e);
        }
//        }
        return result;
    }


    protected JsonParser getJsonParserFromResponse(final String responseString) throws JsonKamaException {
        JsonParser result;

        JsonFactory jsonFactory = new JsonFactory();
        try {
            result = jsonFactory.createParser(responseString);
        } catch (JsonParseException e) {
            throw new JsonKamaException(e);
        } catch (IOException e) {
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
