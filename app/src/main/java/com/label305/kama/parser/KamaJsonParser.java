package com.label305.kama.parser;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.label305.kama.exceptions.JsonKamaException;
import com.label305.kama.utils.KamaParam;

import java.io.IOException;

public class KamaJsonParser<ReturnType> extends MyJsonParser<ReturnType> {

    private final ObjectMapper mObjectMapper = new ObjectMapper();

    public KamaJsonParser(final Class<ReturnType> clzz) {
        super(clzz);
    }

    @Override
    protected JsonParser getJsonParserFromResponse(final String responseString) throws JsonKamaException {
        try {
            JsonParser jsonParser = super.getJsonParserFromResponse(responseString);
            JsonNode jsonResponse = mObjectMapper.readTree(jsonParser);
            JsonNode retVal = jsonResponse.get(KamaParam.RESPONSE);
            if (retVal == null) {
                throw new JsonKamaException("Unexpected jsontitle. Not found: " + KamaParam.RESPONSE);
            }
            return retVal.traverse();
        } catch (JsonParseException e) {
            throw new JsonKamaException(e);
        } catch (JsonProcessingException e) {
            throw new JsonKamaException(e);
        } catch (IOException e) {
            throw new JsonKamaException(e);
        }
    }
}
