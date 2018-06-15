package org.ufrn.framework.util;

import javax.print.attribute.standard.RequestingUserName;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class JsonExtractUtil {

	public static String extractValue(String json, String key) throws JsonParseException {
		if (json != null && !json.equals("")) {
			JsonParser parser = new JsonParser();
			JsonObject object = parser.parse(json).getAsJsonObject();
			return object.get(key).getAsString();
        } else {
        	throw new JsonParseException("Json is formatted incorrectly");
        }
	}
}
