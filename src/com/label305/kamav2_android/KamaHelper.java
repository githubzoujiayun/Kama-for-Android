package com.label305.kamav2_android;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpResponse;

import android.content.Context;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.label305.kamav2_android.KamaParam.AUTH_TYPE;
import com.label305.kamav2_android.exceptions.HttpResponseKamaException;
import com.label305.kamav2_android.exceptions.JsonKamaException;
import com.label305.kamav2_android.exceptions.NotAuthorizedKamaException;

public class KamaHelper extends JsonHelper {

	public KamaHelper(Context context, String apiKey) {
		super(context, apiKey);
	}

	protected JsonParser getJsonParserFromResponse(HttpResponse response) throws JsonKamaException, NotAuthorizedKamaException, HttpResponseKamaException {
		JsonParser jsonParser = super.getJsonParserFromResponse(response);
		JsonNode jsonResponse = null;

		try {
			jsonResponse = mapper.readTree(jsonParser);
			JsonNode retVal = jsonResponse.get(KamaParam.RESPONSE);
			return retVal.traverse();

		} catch (JsonParseException e) {
			throw new JsonKamaException(e);
		} catch (IOException e) {
			throw new JsonKamaException(e);
		}
	}

	protected Map<String, String> setHeaders(Map<String, String> headerData, AUTH_TYPE authType) throws NotAuthorizedKamaException {
		Map<String, String> finalHeaderData = setAuthHeader(headerData, authType);
		finalHeaderData.put("Accept", "application/vnd.kama-v1+json");
		return finalHeaderData;
	}
}
