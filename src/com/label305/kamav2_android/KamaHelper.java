package com.label305.kamav2_android;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpResponse;

import android.content.Context;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.label305.kamav2_android.KamaParam.AUTH_TYPE;
import com.label305.kamav2_android.exceptions.KamaException_HttpResponse;
import com.label305.kamav2_android.exceptions.KamaException_Json;
import com.label305.kamav2_android.exceptions.KamaException_Not_Authorized;

public class KamaHelper extends JsonHelper {

	public KamaHelper(Context context, String appKey) {
		super(context, appKey);
	}

	@Override
	protected JsonParser getJsonParserFromResponse(HttpResponse response) throws KamaException_Json, KamaException_Not_Authorized, KamaException_HttpResponse {
		JsonParser jsonParser = super.getJsonParserFromResponse(response);
		JsonNode jsonResponse = null;

		try {
			jsonResponse = mapper.readTree(jsonParser);
			JsonNode retVal = jsonResponse.get(KamaParam.RESPONSE);
			return retVal.traverse();

		} catch (JsonParseException e) {
			throw new KamaException_Json(e);
		} catch (IOException e) {
			throw new KamaException_Json(e);
		}
	}

	@Override
	protected Map<String, String> setHeaders(Map<String, String> headerData, AUTH_TYPE authType) throws KamaException_Not_Authorized {
		Map<String, String> finalHeaderData = this.setAuthHeader(headerData, authType);

		finalHeaderData.put("Accept", "application/vnd.kama-v1+json");

		return finalHeaderData;
	}
}
