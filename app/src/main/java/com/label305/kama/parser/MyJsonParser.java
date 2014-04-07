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

public class MyJsonParser {

    private final ObjectMapper mObjectMapper = new ObjectMapper();

    public <T> T parseObject(final String responseString, final Class<T> retType, final String objTitle) throws JsonKamaException {
        T result = null;
        JsonParser jsonParser = getJsonParserFromResponse(responseString);

        if (retType != null) {
            try {
                result = getJsonObject(jsonParser, retType, objTitle);
            } catch (JsonParseException e) {
                throw new JsonKamaException(e);
            } catch (JsonMappingException e) {
                throw new JsonKamaException(e);
            } catch (IOException e) {
                throw new JsonKamaException(e);
            }
        }
        return result;
    }

    public <T> List<T> parseObjectsList(final String responseString, final Class<T> retType, final String objTitle) throws JsonKamaException {
        List<T> result = null;
        JsonParser jsonParser = getJsonParserFromResponse(responseString);

        if (retType != null) {
            try {
                result = getJsonObjectsList(jsonParser, retType, objTitle);
            } catch (JsonParseException e) {
                throw new JsonKamaException(e);
            } catch (JsonMappingException e) {
                throw new JsonKamaException(e);
            } catch (IOException e) {
                throw new JsonKamaException(e);
            }
        }
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

    private <T> T getJsonObject(final JsonParser jsonParser, final Class<? extends T> retType, final String objTitle) throws IOException, JsonKamaException {
        T result = null;
        if (retType != null) {
            JsonParser currentJsonParser = jsonParser;
            if (objTitle != null) {
                JsonNode response = mObjectMapper.readTree(currentJsonParser);
                JsonNode responseStr = response.get(objTitle);
                if (responseStr == null) {
                    throw new JsonKamaException("Unexpected jsontitle. Not found: " + objTitle);
                }
                currentJsonParser = responseStr.traverse();
            }
            result = mObjectMapper.readValue(currentJsonParser, retType);
        }
        return result;
    }

    private <T> List<T> getJsonObjectsList(final JsonParser jsonParser, final Class<? extends T> retType, final String objTitle) throws JsonKamaException, IOException {
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
        return mObjectMapper.readValue(parser, mObjectMapper.getTypeFactory().constructCollectionType(List.class, retType));
    }
}
